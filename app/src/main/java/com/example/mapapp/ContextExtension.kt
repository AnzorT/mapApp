package com.example.mapapp

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.mapapp.ui.mapscreen.services.LocationService

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

fun Context.isLocationServiceRunning() : Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (serviceInfo in activityManager.getRunningServices(Int.MAX_VALUE)) {
        if (LocationService::class.java.name.equals(serviceInfo.service.className)) {
            return true
        }
    }
    return false
}