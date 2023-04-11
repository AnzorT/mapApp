package com.example.mapapp.ui.mapscreen.room.applications

import android.app.Application
import com.example.mapapp.ui.mapscreen.repository.UserLastLocationRepository
import com.example.mapapp.ui.mapscreen.room.LastLocationDatabase

class LocationApplication : Application() {
    val database by lazy {LastLocationDatabase.getDatabase(this)}
    val repository by lazy {UserLastLocationRepository(database.LocationDao())}
}