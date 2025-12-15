package com.example.lolokidsapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lolokidsapp.adapters.LetterAdapter
import com.example.lolokidsapp.models.Letter
import com.example.lolokidsapp.models.LetterType
import com.example.lolokidsapp.utils.AlphabetLoader

/**
 * Activity to display list of letters
 */
class AlphabetListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvTitle: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var letterType: LetterType

    companion object {
        const val EXTRA_LETTER_TYPE = "letter_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alphabet_list)

        // Get letter type from intent
        val typeString = intent.getStringExtra(EXTRA_LETTER_TYPE) ?: "FRENCH"
        letterType = LetterType.valueOf(typeString)

        initializeViews()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewLetters)
        tvTitle = findViewById(R.id.tvTitle)
        btnBack = findViewById(R.id.btnBack)

        // Set title based on letter type
        tvTitle.text = if (letterType == LetterType.ARABIC) {
            getString(R.string.arabic_alphabet)
        } else {
            getString(R.string.french_alphabet)
        }
    }

    private fun setupRecyclerView() {
        // Load letters from JSON
        val letters = AlphabetLoader.loadLetters(this, letterType)

        // Setup RecyclerView with grid layout
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = LetterAdapter(letters) { letter ->
            openLetterTracing(letter)
        }
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun openLetterTracing(letter: Letter) {
        val intent = Intent(this, LetterTracingActivity::class.java).apply {
            putExtra(LetterTracingActivity.EXTRA_LETTER_CHAR, letter.character)
            putExtra(LetterTracingActivity.EXTRA_LETTER_NAME, letter.name)
            putExtra(LetterTracingActivity.EXTRA_LETTER_TYPE, letter.type.name)
        }
        startActivity(intent)
    }
}
