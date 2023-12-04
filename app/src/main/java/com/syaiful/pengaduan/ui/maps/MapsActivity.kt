package com.syaiful.pengaduan.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.core.app.ActivityCompat
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.model.Constant
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.syaiful.pengaduan.databinding.ActivityAddpengaduanBinding
import com.syaiful.pengaduan.databinding.ActivityMapslokasiBinding
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapslokasiBinding

    private lateinit var googleMap1: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private val marker = MarkerOptions()
    private lateinit var currentLatLng : LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapslokasiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.tvBgmap.text="Location"
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setData()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    fun setData(){
        binding.ivKembalimap.setOnClickListener {
            onBackPressed()
        }

        binding.actionSavemap.setOnClickListener {
            finish()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap1 = map
        googleMap1.uiSettings.isZoomControlsEnabled = true
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        googleMap1.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this){location ->
            if(location != null) {
                lastLocation = location
                if (Constant.LATITUDE.isEmpty()) {
                    currentLatLng = LatLng(location.latitude, location.longitude)
                    googleMap1.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))

                    Constant.LATITUDE = location.latitude.toString()
                    Constant.LONGITUDE = location.longitude.toString()

                    mapText(location.latitude, location.longitude)

                } else {
                    currentLatLng =
                        LatLng(Constant.LATITUDE.toDouble(), Constant.LONGITUDE.toDouble())
                    googleMap1.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }

                marker.position(currentLatLng)
                googleMap1.addMarker(marker)

            }
        }

        googleMap1.setOnMapClickListener { latLng ->
            marker.position(latLng)
            marker.title(latLng.latitude.toString() + " : " + latLng.longitude.toString())

            Constant.LATITUDE = latLng.latitude.toString()
            Constant.LONGITUDE = latLng.longitude.toString()

            mapText(latLng.latitude, latLng.longitude)

            googleMap1.clear()
            googleMap1.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap1.addMarker(marker)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maps, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (R.layout.toolbarmap) {
//            R.id.action_savemap -> {
//                finish()
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    fun mapText(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            if (addressList != null && addressList.size > 0) {
                Constant.AREA =
                    addressList[0].subLocality + ", " + addressList[0].locality  + ", " + addressList[0].subAdminArea + ", " + addressList[0].adminArea
               addressList[0].adminArea
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}