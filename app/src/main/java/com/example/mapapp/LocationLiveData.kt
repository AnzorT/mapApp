package com.example.mapapp

import android.location.Location
import com.example.mapapp.repository.location.room.LastLocation
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object LocationLiveData {
     var pos = 0
    private val _userLocationMutableSharedFlow = MutableSharedFlow<LastLocation>()
    val userLocationFlow = _userLocationMutableSharedFlow.asSharedFlow()

    suspend fun setUserLocationMutableSharedFlow(location: LastLocation) {
        _userLocationMutableSharedFlow.emit(location)
    }
}