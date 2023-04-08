package com.example.mapapp.ui.mapscreen


import android.location.Location
import kotlinx.coroutines.flow.Flow


interface LocationClient {
    fun getLocationUpdates(interval: Long) : Flow<Location>
}