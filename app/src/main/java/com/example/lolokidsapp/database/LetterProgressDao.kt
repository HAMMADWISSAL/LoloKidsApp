package com.example.lolokidsapp.database

import androidx.room.*

/**
 * DAO for letter progress operations
 */
@Dao
interface LetterProgressDao {

    @Query("SELECT * FROM letter_progress WHERE character = :character AND letterType = :type")
    suspend fun getProgress(character: String, type: String): LetterProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: LetterProgress)

    @Update
    suspend fun updateProgress(progress: LetterProgress)

    @Query("SELECT * FROM letter_progress WHERE letterType = :type ORDER BY lastCompletedTime DESC")
    suspend fun getAllProgressByType(type: String): List<LetterProgress>

    @Query("DELETE FROM letter_progress")
    suspend fun clearAll()
}
