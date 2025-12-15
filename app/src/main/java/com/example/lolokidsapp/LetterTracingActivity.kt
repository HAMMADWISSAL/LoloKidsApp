package com.example.lolokidsapp

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.lolokidsapp.database.AppDatabase
import com.example.lolokidsapp.database.LetterProgress
import com.example.lolokidsapp.utils.TextToSpeechHelper
import com.example.lolokidsapp.views.DrawingView
import kotlinx.coroutines.launch

/**
 * Activity for tracing letters with sound
 */
class LetterTracingActivity : AppCompatActivity() {

    private lateinit var tvLetter: TextView
    private lateinit var tvLetterName: TextView
    private lateinit var drawingView: DrawingView
    private lateinit var btnBack: ImageButton
    private lateinit var btnSound: ImageButton
    private lateinit var btnClear: CardView
    private lateinit var btnRepeat: CardView

    private var mediaPlayer: MediaPlayer? = null
    private var letterChar: String = ""
    private var letterName: String = ""
    private var letterType: String = ""

    private val database by lazy { AppDatabase.getDatabase(this) }
    private lateinit var ttsHelper: TextToSpeechHelper

    companion object {
        const val EXTRA_LETTER_CHAR = "letter_char"
        const val EXTRA_LETTER_NAME = "letter_name"
        const val EXTRA_LETTER_TYPE = "letter_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_tracing)

        // Get data from intent
        letterChar = intent.getStringExtra(EXTRA_LETTER_CHAR) ?: ""
        letterName = intent.getStringExtra(EXTRA_LETTER_NAME) ?: ""
        letterType = intent.getStringExtra(EXTRA_LETTER_TYPE) ?: ""

        // Initialize TTS
        ttsHelper = TextToSpeechHelper(this)

        initializeViews()
        setupClickListeners()

        // Play sound after a short delay to ensure TTS is ready
        tvLetter.postDelayed({
            playLetterSound()
        }, 500)
    }

    private fun initializeViews() {
        tvLetter = findViewById(R.id.tvLetter)
        tvLetterName = findViewById(R.id.tvLetterName)
        drawingView = findViewById(R.id.drawingView)
        btnBack = findViewById(R.id.btnBack)
        btnSound = findViewById(R.id.btnSound)
        btnClear = findViewById(R.id.btnClear)
        btnRepeat = findViewById(R.id.btnRepeat)

        // Set letter display
        tvLetter.text = letterChar
        tvLetterName.text = letterName

        // Set the letter as a placeholder in the drawing view
        drawingView.setPlaceholderLetter(letterChar)
        drawingView.setPlaceholderAlpha(80) // Semi-transparent (0-255)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnSound.setOnClickListener {
            playLetterSound()
        }

        btnClear.setOnClickListener {
            drawingView.clear()
        }

        btnRepeat.setOnClickListener {
            playLetterSound()
            saveProgress()
        }
    }

    private fun playLetterSound() {
        // Release previous media player
        mediaPlayer?.release()
        mediaPlayer = null

        try {
            // Try to load custom sound from raw folder first
            val soundFileName = when (letterType) {
                "ARABIC" -> "arabic_${letterChar.replace(" ", "_").lowercase()}"
                "FRENCH" -> "french_${letterChar.lowercase()}"
                else -> null
            }

            var soundResId = 0
            if (soundFileName != null) {
                soundResId = resources.getIdentifier(soundFileName, "raw", packageName)
            }

            // If custom sound not found, try generic letter sound
            if (soundResId == 0) {
                val genericName = "letter_${letterChar.lowercase()}"
                soundResId = resources.getIdentifier(genericName, "raw", packageName)
            }

            // If still no sound found, use text-to-speech or beep
            if (soundResId != 0) {
                // Custom sound found
                mediaPlayer = MediaPlayer.create(this, soundResId)
                mediaPlayer?.setOnCompletionListener {
                    it.release()
                }
                mediaPlayer?.start()
                Toast.makeText(this, "🔊 $letterName", Toast.LENGTH_SHORT).show()
            } else {
                // No custom sound - create a simple beep using MediaPlayer
                playBeepSound()
                Toast.makeText(this, "🔊 $letterName (Default sound)", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, getString(R.string.sound_not_available), Toast.LENGTH_SHORT).show()
        }
    }

    private fun playBeepSound() {
        try {
            // Use Text-to-Speech to pronounce the letter
            when (letterType) {
                "ARABIC" -> {
                    ttsHelper.speakArabic(letterName)
                }

                "FRENCH" -> {
                    ttsHelper.speakFrench(letterChar)
                }

                else -> {
                    ttsHelper.speak(letterChar)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Last resort: try system notification sound
            try {
                val notificationUri = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI
                mediaPlayer = MediaPlayer.create(this, notificationUri)
                mediaPlayer?.setVolume(0.5f, 0.5f)
                mediaPlayer?.setOnCompletionListener {
                    it.release()
                }
                mediaPlayer?.start()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun saveProgress() {
        lifecycleScope.launch {
            try {
                val existing = database.letterProgressDao().getProgress(letterChar, letterType)
                if (existing != null) {
                    // Update existing progress
                    val updated = existing.copy(
                        timesCompleted = existing.timesCompleted + 1,
                        lastCompletedTime = System.currentTimeMillis()
                    )
                    database.letterProgressDao().updateProgress(updated)
                } else {
                    // Create new progress
                    val progress = LetterProgress(
                        character = letterChar,
                        letterType = letterType,
                        timesCompleted = 1,
                        lastCompletedTime = System.currentTimeMillis()
                    )
                    database.letterProgressDao().insertProgress(progress)
                }

                runOnUiThread {
                    Toast.makeText(
                        this@LetterTracingActivity,
                        getString(R.string.great_job),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        ttsHelper.shutdown()
    }

    override fun onPause() {
        super.onPause()
        // Stop any ongoing speech or sound
        ttsHelper.stop()
        mediaPlayer?.pause()
    }
}
