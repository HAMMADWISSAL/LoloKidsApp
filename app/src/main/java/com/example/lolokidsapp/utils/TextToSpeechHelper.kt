package com.example.lolokidsapp.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

/**
 * Helper class for Text-to-Speech functionality
 */
class TextToSpeechHelper(context: Context) {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
            }
        }
    }

    /**
     * Speak the given text
     */
    fun speak(text: String, language: Locale = Locale.US) {
        if (isInitialized) {
            tts?.language = language
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    /**
     * Speak Arabic letter
     */
    fun speakArabic(letterName: String) {
        if (isInitialized) {
            // Try to use Arabic locale
            val arabicLocale = Locale("ar")
            val result = tts?.setLanguage(arabicLocale)

            if (result == TextToSpeech.LANG_AVAILABLE || result == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                tts?.speak(letterName, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                // Fallback to English pronunciation
                tts?.language = Locale.US
                tts?.speak(letterName, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    /**
     * Speak French letter
     */
    fun speakFrench(letterName: String) {
        if (isInitialized) {
            // Try to use French locale
            val frenchLocale = Locale.FRENCH
            val result = tts?.setLanguage(frenchLocale)

            if (result == TextToSpeech.LANG_AVAILABLE || result == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                tts?.speak(letterName, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                // Fallback to English
                tts?.language = Locale.US
                tts?.speak(letterName, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    /**
     * Stop speaking
     */
    fun stop() {
        tts?.stop()
    }

    /**
     * Release resources
     */
    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }

    /**
     * Check if TTS is initialized
     */
    fun isReady(): Boolean = isInitialized
}
