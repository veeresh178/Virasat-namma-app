package com.virasatnamma.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Room Entity for Heritage Site
 */
@Entity(tableName = "sites")
data class SiteEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val descriptionEn: String,
    val descriptionKn: String,
    val shortDescription: String,
    val imageUrl: String,
    val audioUrl: String,
    val latitude: Double,
    val longitude: Double,
    val hiddenFacts: String,
    val category: String,
    val yearEstablished: Int,
    val visitCount: Int = 0,
    val rating: Float = 0f,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Room Entity for Check-in (Visit)
 */
@Entity(tableName = "check_ins")
data class CheckInEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val siteId: String,
    val siteName: String,
    val siteImageUrl: String = "", // Added to store image in history
    val timestamp: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)

/**
 * Data model for Heritage Site
 */
data class HeritageLocation(
    val id: String,
    val name: String,
    val descriptionEn: String,
    val descriptionKn: String,
    val shortDescription: String,
    val imageUrl: String,
    val audioUrl: String,
    val latitude: Double,
    val longitude: Double,
    val hiddenFacts: List<String>,
    val category: String,
    val yearEstablished: Int,
    val rating: Float = 0f,
    val isVisited: Boolean = false
)

/**
 * Data model for Check-in (Visit Record)
 */
data class VisitRecord(
    val id: Long,
    val siteId: String,
    val siteName: String,
    val siteImageUrl: String,
    val timestamp: Long
)

/**
 * UI State for location display
 */
data class LocationCardState(
    val location: HeritageLocation,
    val distance: Double,
    val isNearby: Boolean = distance < 5.0
)

/**
 * Digital Passport data
 */
data class DigitalPassport(
    val totalSites: Int,
    val visitedSites: Int,
    val visitPercentage: Float,
    val visits: List<VisitRecord>
)
