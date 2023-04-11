package com.example.mapapp.ui.mapscreen.repository

import com.example.mapapp.ui.mapscreen.room.UserLocations
import com.example.mapapp.ui.mapscreen.room.LocationDao

class UserLastLocationRepository(locationDao: LocationDao) {
    private val locationsDao = locationDao

    fun addNewLocation(newLocation: UserLocations) {
        locationsDao.addLocation(newLocation)
    }

    fun deleteLocation(itemId: String) {
        locationsDao.deleteItemById(itemId)
    }

    fun getUserLocationsList() : List<UserLocations> {
        return locationsDao.getAll()
    }
}