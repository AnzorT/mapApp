package com.example.mapapp.ui.mapscreen.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class UserLocations(
    @PrimaryKey val id:String,
    val latitude: Double,
    val longitude: Double
)