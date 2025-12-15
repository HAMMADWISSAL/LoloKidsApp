package com.example.lolokidsapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.lolokidsapp.models.LetterType

/**
 * Main menu activity for Kids Learning App
 */
class MainActivity : AppCompatActivity() {

    private lateinit var btnArabic: CardView
    private lateinit var btnFrench: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        btnArabic = findViewById(R.id.btnArabic)
        btnFrench = findViewById(R.id.btnFrench)
    }

    private fun setupClickListeners() {
        btnArabic.setOnClickListener {
            openAlphabetList(LetterType.ARABIC)
        }

        btnFrench.setOnClickListener {
            openAlphabetList(LetterType.FRENCH)
        }
    }

    private fun openAlphabetList(letterType: LetterType) {
        val intent = Intent(this, AlphabetListActivity::class.java).apply {
            putExtra(AlphabetListActivity.EXTRA_LETTER_TYPE, letterType.name)
        }
        startActivity(intent)
    }
}