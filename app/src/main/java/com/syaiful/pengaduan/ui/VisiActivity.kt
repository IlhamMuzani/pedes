package com.syaiful.pengaduan.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.databinding.ActivityVisiBinding
import com.syaiful.pengaduan.ui.login.LoginActivity
import com.syaiful.pengaduan.ui.register.RegisterActivity

class VisiActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityVisiBinding
    private lateinit var prefsManager: PrefsManager

    private var isRegisterButtonSelected = false
    private var isLoginButtonSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        prefsManager = PrefsManager(this)
        main()

        binding.tvTitle.text = "Profil Desa";

    }

    fun main() {
        binding.textViewRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
//            if (!isLoginButtonSelected) {
//                binding.btnLogin.setBackgroundResource(R.drawable.button_primary_selected)
//                isLoginButtonSelected = true
//                isRegisterButtonSelected = false
//            } else {
                startActivity(Intent(this, LoginActivity::class.java))
//            }
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapbanjar) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(-7.102910, 109.307630)
        googleMap.addMarker(MarkerOptions().position(latLng).title("Banjaranyar"))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }
}
