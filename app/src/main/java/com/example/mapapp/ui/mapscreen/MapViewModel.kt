package com.example.mapapp.ui.mapscreen

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mapapp.ui.mapscreen.repository.UserLastLocationRepository
import com.example.mapapp.ui.mapscreen.room.UserLocations
import com.example.mapapp.ui.mapscreen.services.locationservice.LocationService
import com.example.mapapp.ui.mapscreen.services.locationservice.model.LocationBinder
import kotlinx.coroutines.launch

class MapViewModel(repository: UserLastLocationRepository) : ViewModel() {
    private val locationRepository = repository
    val lastLocationsListLiveData = MutableLiveData<List<UserLocations>>()
    private val locationForegroundServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val locationBinder = (service as LocationBinder)
            val locationForegroundService = locationBinder.getService()
            viewModelScope.launch {
                locationForegroundService.userLastLocationsFlow.collect {  location ->
                    addLocation(location)
                    getLastLocationsList()
                }
            }
        }
        override fun onServiceDisconnected(name: ComponentName?) {}
    }

    fun startLocationService(requireActivity: FragmentActivity) {
        Intent(requireActivity, LocationService::class.java).apply {
            requireActivity.startService(this)
            requireActivity.bindService(
                this,
                locationForegroundServiceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    private fun addLocation(location: UserLocations) {
        lastLocationsListLiveData.value?.apply {
            if(size >= 20) {
                locationRepository.deleteLocation(this[0].id)
            }
        }
        locationRepository.addNewLocation(location)
    }

    fun getLastLocationsList() {
        lastLocationsListLiveData.postValue(locationRepository.getUserLocationsList())
    }
}

@Suppress("UNCHECKED_CAST")
class MapViewModelFactory(private val repository: UserLastLocationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(repository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown viewModel class")
    }
}