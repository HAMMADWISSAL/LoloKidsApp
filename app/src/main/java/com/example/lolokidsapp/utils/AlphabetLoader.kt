package com.example.lolokidsapp.utils

import android.content.Context
import com.example.lolokidsapp.models.Letter
import com.example.lolokidsapp.models.LetterType
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException

/**
 * Utility class to load alphabet data from JSON file
 */
object AlphabetLoader {

    private var cachedArabicLetters: List<Letter>? = null
    private var cachedFrenchLetters: List<Letter>? = null

    /**
     * Load letters from JSON file
     */
    fun loadLetters(context: Context, type: LetterType): List<Letter> {
        // Return cached data if available
        if (type == LetterType.ARABIC && cachedArabicLetters != null) {
            return cachedArabicLetters!!
        }
        if (type == LetterType.FRENCH && cachedFrenchLetters != null) {
            return cachedFrenchLetters!!
        }

        return try {
            val jsonString = loadJsonFromAssets(context, "alphabets.json")
            val gson = Gson()
            val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)

            val letters = when (type) {
                LetterType.ARABIC -> {
                    val arabicArray = jsonObject.getAsJsonArray("arabic")
                    arabicArray.map { element ->
                        val obj = element.asJsonObject
                        Letter(
                            character = obj.get("character").asString,
                            name = obj.get("name").asString,
                            type = LetterType.ARABIC,
                            position = obj.get("position").asInt
                        )
                    }
                }

                LetterType.FRENCH -> {
                    val frenchArray = jsonObject.getAsJsonArray("french")
                    frenchArray.map { element ->
                        val obj = element.asJsonObject
                        Letter(
                            character = obj.get("character").asString,
                            name = obj.get("name").asString,
                            type = LetterType.FRENCH,
                            position = obj.get("position").asInt
                        )
                    }
                }
            }

            // Cache the data
            if (type == LetterType.ARABIC) {
                cachedArabicLetters = letters
            } else {
                cachedFrenchLetters = letters
            }

            letters
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Load JSON file from assets
     */
    private fun loadJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}
