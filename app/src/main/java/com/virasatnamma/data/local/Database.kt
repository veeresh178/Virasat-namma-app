package com.virasatnamma.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room Database for Virasat-Namma
 * Manages local storage of heritage sites and visit records
 */
@Database(
    entities = [SiteEntity::class, CheckInEntity::class],
    version = 1,
    exportSchema = false
)
abstract class VirasatDatabase : RoomDatabase() {
    
    abstract fun siteDao(): SiteDao
    abstract fun checkInDao(): CheckInDao
    
    companion object {
        @Volatile
        private var instance: VirasatDatabase? = null
        
        fun getDatabase(context: Context): VirasatDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    VirasatDatabase::class.java,
                    "virasat_namma_db"
                ).build().also { instance = it }
            }
    }
}
