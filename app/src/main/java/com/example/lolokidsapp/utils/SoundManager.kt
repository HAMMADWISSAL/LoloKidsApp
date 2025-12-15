package com.example.lolokidsapp.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

/**
 * Utility class to manage sound playback
 */
class SoundManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    /**
     * Play sound from raw resource
     * @param resourceId The resource ID of the sound file
     */
    fun playSound(resourceId: Int) {
        release()
        try {
            mediaPlayer = MediaPlayer.create(context, resourceId)
            mediaPlayer?.setOnCompletionListener {
                release()
            }
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Play sound from URI
     * @param uri The URI of the sound file
     */
    fun playSound(uri: Uri) {
        release()
        try {
            mediaPlayer = MediaPlayer.create(context, uri)
            mediaPlayer?.setOnCompletionListener {
                release()
            }
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Play default notification sound
     */
    fun playDefaultSound() {
        val soundUri = android.media.RingtoneManager.getDefaultUri(
            android.media.RingtoneManager.TYPE_NOTIFICATION
        )
        playSound(soundUri)
    }

    /**
     * Stop and release the media player
     */
    fun release() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
        }
    }

    /**
     * Check if sound is currently playing
     */
    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
}
