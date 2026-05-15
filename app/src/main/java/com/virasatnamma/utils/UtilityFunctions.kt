package com.virasatnamma.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Location Utility Functions
 */
object LocationUtils {
    
    /**
     * Check if location permissions are granted
     */
    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Calculate distance between two coordinates using Haversine formula
     * @param lat1 Latitude of first point
     * @param lon1 Longitude of first point
     * @param lat2 Latitude of second point
     * @param lon2 Longitude of second point
     * @return Distance in kilometers
     */
    fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val earthRadiusKm = 6371.0
        
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        return earthRadiusKm * c
    }
    
    /**
     * Check if site is within nearby range (5km)
     */
    fun isNearby(distance: Double, radiusKm: Double = 5.0): Boolean {
        return distance <= radiusKm
    }
}

/**
 * String Utility Functions
 */
object StringUtils {
    
    /**
     * Truncate string to specified length with ellipsis
     */
    fun truncate(text: String, maxLength: Int): String {
        return if (text.length > maxLength) {
            text.substring(0, maxLength) + "..."
        } else {
            text
        }
    }
    
    /**
     * Parse hidden facts from pipe-separated string
     */
    fun parseHiddenFacts(factsString: String): List<String> {
        return factsString
            .split("|")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }
}

/**
 * Audio Utility Functions
 */
object AudioUtils {
    
    /**
     * Validate audio URL
     */
    fun isValidAudioUrl(url: String): Boolean {
        return url.isNotEmpty() &&
                (url.endsWith(".mp3") || 
                 url.endsWith(".wav") || 
                 url.endsWith(".m4a") ||
                 url.contains("example.com"))
    }
}

/**
 * Date/Time Utility Functions
 */
object DateTimeUtils {
    
    /**
     * Get time ago string
     */
    fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        
        return when {
            diff < 60000 -> "Just now"
            diff < 3600000 -> "${diff / 60000} minutes ago"
            diff < 86400000 -> "${diff / 3600000} hours ago"
            diff < 604800000 -> "${diff / 86400000} days ago"
            else -> "${diff / 604800000} weeks ago"
        }
    }
}
