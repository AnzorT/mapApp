package com.example.mapapp.ui.mapscreen

import android.app.Application
import android.content.Intent
import android.location.Location
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mapapp.LocationLiveData
import com.example.mapapp.repository.location.room.LastLocation
import com.example.mapapp.repository.location.room.LastLocationDatabase
import com.example.mapapp.repository.location.room.LocationDAO
import com.example.mapapp.ui.mapscreen.services.LocationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapViewModel(application: Application) : ViewModel() {
    private val locationDao: LocationDAO = LastLocationDatabase.getInstance(application).LocationDAO()
//    private val _userLastLocationsMutableSharedFlow = MutableSharedFlow<List<LastLocation>>()
//    val userLastLocationsFlow = _userLastLocationsMutableSharedFlow.asSharedFlow()
val liveData = MutableLiveData<List<LastLocation>>()

    init {
        viewModelScope.launch {
            LocationLiveData.userLocationFlow.collectLatest { lastLocation ->
                addLocation(lastLocation)
                liveData.postValue(locationDao.getAll())
//                _userLastLocationsMutableSharedFlow.emit(locationDao.getAll())
            }
        }
    }


    private fun addLocation(location: LastLocation) {
        liveData.value?.apply {
            if(size >= 20) {
                locationDao.deleteItemById(this[0].id)
            }
        }
        locationDao.addLocation(location)
    }



    fun startLocationService(requireActivity: FragmentActivity) {
        Intent(requireActivity, LocationService::class.java).apply {
            requireActivity.startService(this)
        }
    }
}

class MapViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(application) as T
    }
}