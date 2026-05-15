package com.virasatnamma.utils

import android.media.MediaPlayer
import java.io.IOException

/**
 * Audio Player Wrapper
 * Handles audio playback with error handling
 */
class AudioPlayerManager {
    
    private var mediaPlayer: MediaPlayer? = null
    private var isPlayingState = false
    
    /**
     * Play audio from URL with error handling
     */
    fun play(audioUrl: String, onError: (String) -> Unit = {}) {
        try {
            // Stop current playback if any
            stop()
            
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioUrl)
                setOnPreparedListener {
                    start()
                    this@AudioPlayerManager.isPlayingState = true
                }
                setOnErrorListener { mp, what, extra ->
                    onError("Error: $what, $extra")
                    false
                }
                setOnCompletionListener {
                    this@AudioPlayerManager.isPlayingState = false
                }
                prepareAsync()
            }
        } catch (e: IOException) {
            onError("Failed to load audio: ${e.message}")
        }
    }
    
    /**
     * Pause playback
     */
    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isPlayingState = false
            }
        }
    }
    
    /**
     * Resume playback
     */
    fun resume() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                isPlayingState = true
            }
        }
    }
    
    /**
     * Stop playback and release resources
     */
    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
        isPlayingState = false
    }
    
    /**
     * Check if currently playing
     */
    fun isCurrentlyPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
    
    /**
     * Get current position in milliseconds
     */
    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }
    
    /**
     * Set volume level (0f to 1f)
     */
    fun setVolume(leftVolume: Float, rightVolume: Float) {
        mediaPlayer?.setVolume(leftVolume, rightVolume)
    }
}
