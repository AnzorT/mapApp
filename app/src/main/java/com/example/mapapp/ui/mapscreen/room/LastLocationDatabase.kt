package com.example.mapapp.ui.mapscreen.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserLocations::class], version = 1)
abstract class LastLocationDatabase : RoomDatabase() {
    abstract fun LocationDao() : LocationDao

    companion object {
        @Volatile
        private var INSTANCE: LastLocationDatabase? = null

        fun getDatabase(context: Context): LastLocationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LastLocationDatabase::class.java,
                    "locations_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}