package com.example.mapapp.ui.mapscreen.services.locationservice.model

import android.os.Binder
import com.example.mapapp.ui.mapscreen.services.locationservice.LocationService

class LocationBinder(private val service: LocationService) : Binder() {
    fun getService(): LocationService {
        return service
    }
}