package com.example.mapapp.repository.location.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LastLocation(
    @PrimaryKey val id:String,
    val latitude: Double,
    val longitude: Double
)