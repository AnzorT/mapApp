package com.example.mapapp.ui.mapscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mapapp.MainActivity
import com.example.mapapp.R
import com.example.mapapp.databinding.FragmentMapBinding
import com.example.mapapp.hasLocationPermission
import com.example.mapapp.isLocationServiceRunning
import com.example.mapapp.ui.mapscreen.room.UserLocations
import com.example.mapapp.ui.mapscreen.room.applications.LocationApplication
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment() {
    private val viewModel: MapViewModel by activityViewModels {
        MapViewModelFactory(((activity as MainActivity).application as LocationApplication).repository)
    }
    lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        subscribeObserver()
        viewModel.getLastLocationsList()
    }

    private fun setupView() {
        context?.apply {
            if(hasLocationPermission() && !isLocationServiceRunning()) {
                viewModel.startLocationService(requireActivity())
            }
        }
        viewModel.bindLocationServiceToViewModel(requireActivity())
    }

    private fun subscribeObserver() {
        viewModel.lastLocationsListLiveData.observe(viewLifecycleOwner) { locationsList ->
            Log.d("MyTesting","${locationsList.size}")
            setGoogleMaps(locationsList)
        }
    }

    private fun setGoogleMaps(lastLocations: List<UserLocations>) {
        val supportMapFragment = childFragmentManager
            .findFragmentById(R.id.fragment_container_view_google_map) as SupportMapFragment?
        supportMapFragment?.apply {
            getMapAsync { googleMap ->
                googleMap.clear()
                lastLocations.forEach {
                    val markerOptions = MarkerOptions()
                    markerOptions.position(LatLng(it.latitude,it.longitude))
                    googleMap.addMarker(markerOptions)
                }
            }
        }
    }

    override fun onDestroy() {
//        viewModel.stopLocationService(requireActivity())
        super.onDestroy()
    }
}