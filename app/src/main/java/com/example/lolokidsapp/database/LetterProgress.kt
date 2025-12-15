package com.example.lolokidsapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity to track letter learning progress
 */
@Entity(tableName = "letter_progress")
data class LetterProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val character: String,
    val letterType: String, // "ARABIC" or "FRENCH"
    val timesCompleted: Int = 0,
    val lastCompletedTime: Long = System.currentTimeMillis()
)
