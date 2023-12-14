package com.syaiful.pengaduan.ui.pengaduan

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.pengaduan.DataPengaduan
import com.syaiful.pengaduan.databinding.ActivityAddpengaduanBinding
import com.syaiful.pengaduan.data.model.kategori.ResponseKategoriList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import com.syaiful.pengaduan.ui.fragment.UserActivity
import com.syaiful.pengaduan.ui.maps.MapsActivity
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog
import com.syaiful.pengaduan.ui.utils.FileUtils
import com.syaiful.pengaduan.ui.utils.MapsHelper
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.Locale

class AddpengaduanActivity : AppCompatActivity(), AddpengaduanContract.View {
    private lateinit var binding: ActivityAddpengaduanBinding
    private var uriImage: Uri? = null
    private var pickImage = 1
    private lateinit var prefsManager: PrefsManager
    private lateinit var presenter: AddpengaduanPresenter
    private lateinit var pengaduan: DataPengaduan
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var currentLatLng : LatLng
    private lateinit var sLoading: SweetAlertDialog
    private lateinit var sError: SweetAlertDialog
    private lateinit var sSuccess: SweetAlertDialog
    private lateinit var sAlert: SweetAlertDialog

    companion object {
        const val CAMERA_PERMISSION_CODE = 1001
        const val PICK_IMAGE_REQUEST_CODE = 1002
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddpengaduanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        presenter = AddpengaduanPresenter(this)
        prefsManager = PrefsManager(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initActivity()
        initListener()
    }

    override fun onStart() {
        super.onStart()
        if (Constant.AREA != "") {
            binding.tvAlamat1.text = Constant.AREA
        }
        presenter.getKategori()
    }

    fun FileUtils.getImageUri(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            "Title",
            null
        )
        return Uri.parse(path)
    }
    override fun onDestroy() {
        super.onDestroy()
        Constant.LATITUDE = ""
        Constant.LONGITUDE = ""
    }

    override fun initActivity() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }

        binding.tvTitle.text = "Pengaduan";

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        MapsHelper.permissionMap(this, this)
        sLoading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sSuccess = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil")
        sError = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Gagal")
        sAlert = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Peringatan !")
    }

    override fun initListener() {

        spinnerpatokan()

        binding.imvImages.setOnClickListener {
            showImageSourceDialog()
        }

//        binding.BtnLokasi1.setOnClickListener {
//            startActivity(Intent(this, MapsActivity::class.java))
//        }

        binding.imvImages.setOnClickListener {
            // Open the camera directly
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, pickImage)
            }

        }

        binding.btnPengaduan.setOnClickListener {
//            if (binding.edtKategori.text!!.isEmpty()) {
//                showError("Pilih Lokasi !")
//            } else
            if (Constant.KATEGORI_ID == 0) {
                showError("Pilih Kategori Produk")
            } else if (Constant.LATITUDE == "") {
                showError("Pilih Lokasi !")
            } else if  (Constant.PATOKAN_ID == 0){
                showError("Masukkan detail alamat !")
            } else if (uriImage == null || uriImage!!.toString().isEmpty()) {
                showError("Masukkan Foto")
            } else if (binding.edtDeskripsi.text!!.isEmpty()) {
                showError("Kolom Deskripsi !")
            } else
                presenter.insertPengaduan(
                    prefsManager.prefsId,
                    Constant.KATEGORI_ID.toString(),
//                    binding.edtKategori.text.toString(),
                    binding.edtDeskripsi.text.toString(),
                    binding.tvAlamat1.text.toString(),
                    Constant.PATOKANNAME,
                    FileUtils.getFile(this, uriImage),
                    Constant.LATITUDE,
                    Constant.LONGITUDE,
                )
        }
    }

    private fun showImageSourceDialog() {
        openCamera()
    }

//    private fun openCamera() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (takePictureIntent.resolveActivity(packageManager) != null) {
//            startActivityForResult(takePictureIntent, pickImage)
//        }
//    }

    private fun openCamera() {
        // Check if location permissions are granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                CAMERA_PERMISSION_CODE
            )
            return
        }

        // Get the current location
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                currentLatLng = LatLng(location.latitude, location.longitude)

                // Store latitude and longitude in Constant variables
                Constant.LATITUDE = location.latitude.toString()
                Constant.LONGITUDE = location.longitude.toString()

                // Continue with opening the camera
                openCameraIntent()

                // Set the location information to the binding
                main()
            } else {
                // Handle the case where location is not available
                showError("Location not available")
            }
        }
    }

    private fun openCameraIntent() {
        // Open the camera
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Ensure currentLatLng is initialized before using it
            if (!::currentLatLng.isInitialized) {
                currentLatLng = LatLng(0.0, 0.0) // Provide a default value if not initialized
            }

            // Pass location information to the camera intent
            takePictureIntent.putExtra("latitude", currentLatLng.latitude)
            takePictureIntent.putExtra("longitude", currentLatLng.longitude)

            startActivityForResult(takePictureIntent, pickImage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            pickImage -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.data != null) {
                        // User selected a photo from the gallery
                        uriImage = data.data
                        binding.imvImages.setImageURI(uriImage)
                    } else {
                        // User took a photo directly from the camera
                        val imageBitmap = data?.extras?.get("data") as Bitmap
                        binding.imvImages.setImageBitmap(imageBitmap)

                        // Convert bitmap to URI and set it to uriImage
                        uriImage = FileUtils.getImageUri(this, imageBitmap)

                        // Set the location information to the binding
                        main()
                    }
                }
            }
            // Handle other request codes if needed
            else -> {
                // Handle other cases if needed
            }
        }
    }


    fun main() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Handle the case where permissions are not granted
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                if (Constant.LATITUDE.isEmpty()) {
                    currentLatLng = LatLng(location.latitude, location.longitude)

                    Constant.LATITUDE = location.latitude.toString()
                    Constant.LONGITUDE = location.longitude.toString()

                } else {
                    currentLatLng =
                        LatLng(Constant.LATITUDE.toDouble(), Constant.LONGITUDE.toDouble())
                }

                // Set the location information to the binding
                getAddressFromLocation(currentLatLng.latitude, currentLatLng.longitude)
            }
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            if (addressList != null && addressList.size > 0) {
                val address = addressList[0]
                val locality = address.locality
                val subLocality = address.subLocality
                val subAdminArea = address.subAdminArea

                // Set the location information to the binding
                binding.tvAlamat1.text = "$subLocality, $locality, $subAdminArea"
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    override fun onLoading(loading: Boolean, message: String?) {
        when (loading) {
            true -> sLoading.setTitleText(message).show()
            false -> sLoading.dismiss()
        }
    }
    override fun onloadingswet(loading: Boolean, message: String?) {
        when (loading) {
            true -> sLoading.setContentText(message).show()
            false -> sLoading.dismiss()
        }
    }

    override fun onResultKategori(responseKategoriList: ResponseKategoriList) {
        spinnerKategori(responseKategoriList)
    }

    override fun onResult(responsePengaduanList: ResponsePengaduanList) {
        if (responsePengaduanList.status) {
            showSuccesOk(responsePengaduanList.message)
        } else {
            showError(responsePengaduanList.message)
        }
    }

    fun spinnerKategori(responseKategoriList: ResponseKategoriList) {

        val arrayString = java.util.ArrayList<String>()
        arrayString.add("Pilih Kategori")
        for (kategori in responseKategoriList.data) {
            arrayString.add(kategori.nama!!)
        }

        val adapter = ArrayAdapter(this, R.layout.item_spinner, arrayString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.edtKategori.adapter = adapter
        val selection = adapter.getPosition(Constant.KATEGORINAME)
        binding.edtKategori.setSelection(selection)
        binding.edtKategori.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        Constant.KATEGORI_ID = 0
                        Constant.KATEGORINAME = "Pilih Kategori"
                    }
                    else -> {
                        val kategori = responseKategoriList.data[position - 1].nama
                        Constant.KATEGORI_ID = responseKategoriList.data[position - 1].id!!.toInt()
                        Constant.KATEGORINAME = kategori.toString()
                    }

                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    }

    fun spinnerpatokan() {

        val arrayString = ArrayList<String>()
        arrayString.add("Pilih RT")
        arrayString.add("RT 01")
        arrayString.add("RT 02")
        arrayString.add("RT 03")
        arrayString.add("RT 04")
        arrayString.add("RT 05")
        arrayString.add("RT 06")
        arrayString.add("RT 07")
        arrayString.add("RT 08")
        arrayString.add("RT 09")
        arrayString.add("RT 10")
        arrayString.add("RT 11")
        arrayString.add("RT 12")
        arrayString.add("RT 13")
        arrayString.add("RT 14")
        arrayString.add("RT 15")
        arrayString.add("RT 16")
        arrayString.add("RT 17")
        arrayString.add("RT 18")
        arrayString.add("RT 19")
        arrayString.add("RT 20")
        arrayString.add("RT 21")

        val adapter = ArrayAdapter(this, R.layout.item_spinner, arrayString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.edtpatokan.adapter = adapter
        val selection = adapter.getPosition(Constant.PATOKANNAME)
        binding.edtpatokan.setSelection(selection)
        binding.edtpatokan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        Constant.PATOKAN_ID = 0
                        Constant.PATOKANNAME = "Pilih RT"
                    }
                    1 -> {
                        Constant.PATOKAN_ID = 1
                        Constant.PATOKANNAME = "RT 01"
                    }
                    2 -> {
                        Constant.PATOKAN_ID = 2
                        Constant.PATOKANNAME = "RT 02"
                    }
                    3 -> {
                        Constant.PATOKAN_ID = 3
                        Constant.PATOKANNAME = "RT 03"
                    }
                    4 -> {
                        Constant.PATOKAN_ID = 4
                        Constant.PATOKANNAME = "RT 04"
                    }
                    5 -> {
                        Constant.PATOKAN_ID = 5
                        Constant.PATOKANNAME = "RT 05"
                    }
                    6 -> {
                        Constant.PATOKAN_ID = 6
                        Constant.PATOKANNAME = "RT 06"
                    }
                    7 -> {
                        Constant.PATOKAN_ID = 7
                        Constant.PATOKANNAME = "RT 07"
                    }
                    8 -> {
                        Constant.PATOKAN_ID = 8
                        Constant.PATOKANNAME = "RT 08"
                    }
                    9 -> {
                        Constant.PATOKAN_ID = 9
                        Constant.PATOKANNAME = "RT 09"
                    }
                    10 -> {
                        Constant.PATOKAN_ID = 10
                        Constant.PATOKANNAME = "RT 10"
                    }
                    11 -> {
                        Constant.PATOKAN_ID = 11
                        Constant.PATOKANNAME = "RT 11"
                    }
                    12 -> {
                        Constant.PATOKAN_ID = 12
                        Constant.PATOKANNAME = "RT 12"
                    }
                    13 -> {
                        Constant.PATOKAN_ID = 13
                        Constant.PATOKANNAME = "RT 13"
                    }
                    14 -> {
                        Constant.PATOKAN_ID = 14
                        Constant.PATOKANNAME = "RT 14"
                    }
                    15 -> {
                        Constant.PATOKAN_ID = 15
                        Constant.PATOKANNAME = "RT 15"
                    }
                    16 -> {
                        Constant.PATOKAN_ID = 16
                        Constant.PATOKANNAME = "RT 16"
                    }
                    17 -> {
                        Constant.PATOKAN_ID = 17
                        Constant.PATOKANNAME = "RT 17"
                    }
                    18 -> {
                        Constant.PATOKAN_ID = 18
                        Constant.PATOKANNAME = "RT 18"
                    }
                    19 -> {
                        Constant.PATOKAN_ID = 19
                        Constant.PATOKANNAME = "RT 19"
                    }
                    20 -> {
                        Constant.PATOKAN_ID = 20
                        Constant.PATOKANNAME = "RT 20"
                    }
                    21 -> {
                        Constant.PATOKAN_ID = 21
                        Constant.PATOKANNAME = "RT 21"
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    }


//    override fun showSuccesOk(message: String) {
//        sSuccess
//            .setContentText(message)
//            .setConfirmText("OK")
//            .setConfirmClickListener {
//                it.dismissWithAnimation()
//                finish()
////                startActivity(Intent(this, LoginActivity::class.java))
//            }.show()
//    }

    override fun showSuccesOk(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismissWithAnimation()
//                finish()
            }
            .show()

        // Delay for 2 seconds before dismissing the alert
        Handler(Looper.getMainLooper()).postDelayed({
            sSuccess.dismissWithAnimation()

            // Delay for an additional 2 seconds before starting the new activity
            Handler(Looper.getMainLooper()).postDelayed({
            onBackPressed()
//                startActivity(Intent(this, UserActivity::class.java))
            }, 500L) // 2000 milliseconds = 2 seconds
        }, 1000L) // 2000 milliseconds = 2 seconds
    }

    override fun showSucces(message: String) {
        sSuccess
            .setContentText(message)
            .setTitleText("Ok")
            .setConfirmClickListener {
                it.dismissWithAnimation()
            }.show()
    }

    override fun showError(message: String) {
        sError
            .setContentText(message)
            .setConfirmText("Gagal")
            .setConfirmClickListener {
                it.dismiss()
            }.show()
    }

    override fun showAlert(message: String) {
        sAlert
            .setContentText(message)
            .setTitleText("Ya")
            .setConfirmClickListener {
                it.dismissWithAnimation()
            }
            .setTitleText("Nanti")
            .setConfirmClickListener {
                it.dismiss()
            }.show()
        sAlert.setCancelable(true)
    }

}