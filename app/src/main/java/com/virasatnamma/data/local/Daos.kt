package com.virasatnamma.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO for Heritage Sites
 */
@Dao
interface SiteDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSites(sites: List<SiteEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSite(site: SiteEntity)
    
    @Query("SELECT * FROM sites ORDER BY name ASC")
    fun getAllSites(): Flow<List<SiteEntity>>
    
    @Query("SELECT * FROM sites WHERE id = :id")
    suspend fun getSiteById(id: String): SiteEntity?
    
    @Query("SELECT * FROM sites WHERE category = :category")
    fun getSitesByCategory(category: String): Flow<List<SiteEntity>>
    
    @Query("SELECT * FROM sites WHERE name LIKE :query")
    suspend fun searchSites(query: String): List<SiteEntity>
    
    @Update
    suspend fun updateSite(site: SiteEntity)
    
    @Delete
    suspend fun deleteSite(site: SiteEntity)
    
    @Query("DELETE FROM sites")
    suspend fun clearAllSites()
    
    @Query("SELECT COUNT(*) FROM sites")
    suspend fun getSiteCount(): Int
}

/**
 * Room DAO for Check-ins (Visits)
 */
@Dao
interface CheckInDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: CheckInEntity)
    
    @Query("SELECT * FROM check_ins ORDER BY timestamp DESC")
    fun getAllCheckIns(): Flow<List<CheckInEntity>>
    
    @Query("SELECT * FROM check_ins WHERE siteId = :siteId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestCheckInForSite(siteId: String): CheckInEntity?
    
    @Query("SELECT * FROM check_ins WHERE siteId = :siteId ORDER BY timestamp DESC")
    suspend fun getCheckInsForSite(siteId: String): List<CheckInEntity>
    
    @Query("SELECT DISTINCT siteId FROM check_ins")
    suspend fun getVisitedSiteIds(): List<String>
    
    @Query("SELECT COUNT(DISTINCT siteId) FROM check_ins")
    suspend fun getVisitedSitesCount(): Int
    
    @Query("SELECT * FROM check_ins WHERE synced = 0")
    suspend fun getUnsyncedCheckIns(): List<CheckInEntity>
    
    @Update
    suspend fun updateCheckIn(checkIn: CheckInEntity)
    
    @Delete
    suspend fun deleteCheckIn(checkIn: CheckInEntity)
    
    @Query("DELETE FROM check_ins")
    suspend fun clearAllCheckIns()
}
