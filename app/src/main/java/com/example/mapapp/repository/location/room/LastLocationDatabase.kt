package com.example.mapapp.repository.location.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LastLocation::class], version = 1)
abstract class LastLocationDatabase : RoomDatabase() {
    abstract fun LocationDAO() : LocationDAO

    companion object {
        @Volatile
        private var INSTANCE: LastLocationDatabase? = null

        fun getInstance(context: Context): LastLocationDatabase {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                LastLocationDatabase::class.java,
                "book_database"
            ).allowMainThreadQueries().build()
            return INSTANCE ?: synchronized(this) {
                INSTANCE = instance
                instance
            }
        }
    }
}