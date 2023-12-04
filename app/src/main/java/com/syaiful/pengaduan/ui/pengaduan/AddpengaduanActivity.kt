package com.syaiful.pengaduan.ui.pengaduan

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Bundle
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
import com.syaiful.pengaduan.ui.maps.MapsActivity
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog
import com.syaiful.pengaduan.ui.utils.FileUtils
import com.syaiful.pengaduan.ui.utils.MapsHelper
import java.io.ByteArrayOutputStream

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

        binding.imvImages.setOnClickListener {
            showImageSourceDialog()
        }

        binding.BtnLokasi1.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

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
            } else if (binding.edtAlamat.text!!.isEmpty()) {
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
                    binding.edtAlamat.text.toString(),
                    FileUtils.getFile(this, uriImage),
                    Constant.LATITUDE,
                    Constant.LONGITUDE,
                )
        }
    }

    private fun showImageSourceDialog() {
        openCamera()
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
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
                    }
                }
            }
            // Handle other request codes if needed
            else -> {
                // Handle other cases if needed
            }
        }
    }    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
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

    fun main(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener(this){ location ->
            if(location != null) {
                lastLocation = location
                if (Constant.LATITUDE.isEmpty()) {
                    currentLatLng = LatLng(location.latitude, location.longitude)

                    Constant.LATITUDE = location.latitude.toString()
                    Constant.LONGITUDE = location.longitude.toString()


                } else {
                    currentLatLng =
                        LatLng(Constant.LATITUDE.toDouble(), Constant.LONGITUDE.toDouble())
                }

            }
        }
    }
    override fun showSuccesOk(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismissWithAnimation()
                finish()
//                startActivity(Intent(this, LoginActivity::class.java))
            }.show()
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