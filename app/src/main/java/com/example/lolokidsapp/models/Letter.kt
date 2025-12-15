package com.example.lolokidsapp.models

/**
 * Data class representing a letter
 */
data class Letter(
    val character: String,
    val name: String,
    val type: LetterType,
    val soundFileName: String? = null,
    val position: Int = 0
)
