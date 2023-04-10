package com.example.mapapp.ui.mapscreen.services.locationservice

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.mapapp.R
import com.example.mapapp.ui.mapscreen.room.LastLocationDatabase
import com.example.mapapp.ui.mapscreen.room.LocationDao
import com.example.mapapp.ui.mapscreen.room.UserLocations
import com.example.mapapp.ui.mapscreen.services.locationservice.model.DefaultLocationClient
import com.example.mapapp.ui.mapscreen.services.locationservice.model.LocationBinder
import com.example.mapapp.ui.mapscreen.services.locationservice.model.LocationClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import java.util.*


class LocationService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private val _userLastLocationsMutableSharedFlow = MutableSharedFlow<UserLocations>()
    val userLastLocationsFlow = _userLastLocationsMutableSharedFlow.asSharedFlow()
    override fun onBind(p0: Intent?): IBinder {
        return LocationBinder(this)
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        start()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking Location")
            .setContentText("We will keep monitoring your location")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
        locationClient
            .getLocationUpdates(600L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val newLocation = UserLocations(
                    Calendar.getInstance().time.toString(),
                    location.latitude,
                    location.longitude
                )
                val updatedNotification = notification.setContentText(
                    "Location: (${newLocation.latitude}, ${newLocation.longitude})"
                )
                if(isAppClosed()) {
                    Log.d("MyTesting","APPISNOTRUNNING")
                    val db: LastLocationDatabase = LastLocationDatabase.getDatabase(applicationContext)
                    val locationDao: LocationDao = db.LocationDao()
                    locationDao.getAll().apply {
                        if(size >= 20) {
                            locationDao.deleteItemById(get(0).id)
                        }
                    }
                    locationDao.addLocation(newLocation)
                } else {
                    Log.d("MyTesting","APPISRUNNING")
                    _userLastLocationsMutableSharedFlow.emit(newLocation)
                    notificationManager.notify(1, updatedNotification.build())
                }
            }.launchIn(serviceScope)
        startForeground(1, notification.build())
    }

    private fun isAppClosed(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val processes = manager.runningAppProcesses
        for (process in processes) {
            if (process.processName == "com.example.mapapp") {
                return process.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}