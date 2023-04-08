package com.example.mapapp.ui.mapscreen

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mapapp.MainActivity
import com.example.mapapp.R
import com.example.mapapp.databinding.FragmentMapBinding
import com.example.mapapp.hasLocationPermission
import com.example.mapapp.isLocationServiceRunning
import com.example.mapapp.repository.location.room.LastLocation
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment() {
    private lateinit var viewModel: MapViewModel
    lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewModelFactory = MapViewModelFactory(application = (activity as MainActivity).application)
        viewModel = ViewModelProvider(this, viewModelFactory)[MapViewModel::class.java]
//        ActivityCompat.requestPermissions(
//            activity as MainActivity,
//            arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ),
//            0
//        )
        setupView()
        subscribeObserver()
    }

    private fun setupView() {
        context?.apply {
            if(hasLocationPermission() && !isLocationServiceRunning()) {
                viewModel.startLocationService(requireActivity())
            } else {
                Log.d("LocationServiceRunning:", "service is already running")
            }
        }
    }

    private fun subscribeObserver() {
        viewModel.liveData.observe(viewLifecycleOwner) {
            setGoogleMaps(it)
        }
    }

    private fun setGoogleMaps(lastLocations: List<LastLocation>) {
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
}