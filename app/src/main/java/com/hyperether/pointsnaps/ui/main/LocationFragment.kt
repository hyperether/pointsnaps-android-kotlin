package com.hyperether.pointsnaps.ui.main

import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hyperether.pointsnaps.model.Location
import com.hyperether.pointsnaps.R
import com.hyperether.pointsnaps.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_location.*

/**
 * A simple [Fragment] subclass.
 */
class LocationFragment : BaseFragment(), OnMapReadyCallback {

    lateinit var viewModel: MainViewModel
    lateinit var fuseLocationClient: FusedLocationProviderClient
    lateinit var mMap: GoogleMap
    lateinit var latLng: LatLng

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)
        toolbar.setTitle(getString(R.string.location))
        toolbar.setNavigationIcon(resources.getDrawable(R.drawable.ic_navigation_icon))
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        val mapFragment: SupportMapFragment? =
            childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        setupObservers()
        mapFragment?.getMapAsync(this)

        acceptBtn.setOnClickListener {
            if (this::latLng.isInitialized) {
                findNavController().popBackStack()
            } else {
                createToast(getString(R.string.loc_not_set))
            }
        }

        declineBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun setupObservers() {
        viewModel.location.observe(viewLifecycleOwner, Observer {
            locationTxt.text = it.address
        })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap ?: return
        mMap = googleMap
        mMap.setOnMapLongClickListener {
            latLng = it
            redrawMarker(latLng)
        }
        setupLocation()
    }

    fun setupLocation() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        fuseLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        fuseLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.getMainLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            val location = locationResult!!.locations.get(0)
            latLng = LatLng(location.latitude, location.longitude)
            redrawMarker(latLng)
        }
    }

    fun redrawMarker(latLng: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(latLng))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
        setupAddress(latLng)
    }

    fun setupAddress(latLng: LatLng) {
        try {
            val geocoder = Geocoder(context)
            val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0)
            viewModel.setLocation(
                Location(
                    latLng.latitude,
                    latLng.longitude,
                    address.getAddressLine(0)
                )
            )
        } catch (exception: Exception) {

        }
    }


}


