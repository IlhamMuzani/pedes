package com.syaiful.pengaduan.ui.fragment.home.detailpengaduanlist

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.pengaduan.DataPengaduan
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduandetail
import com.syaiful.pengaduan.databinding.ActivityDetailpengaduanlistBinding
import com.syaiful.pengaduan.ui.fragment.UserActivity
import com.syaiful.pengaduan.ui.fragment.notifications.tabs.proses.ProsesFragment
import com.syaiful.pengaduan.ui.fragment.notifications.tabs.selesai.KomentarFragment
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog
import com.syaiful.pengaduan.ui.utils.GlideHelper

class DetailpengaduanlistActivity : AppCompatActivity(), DetailpengaduanlistContract.View,
    OnMapReadyCallback {
    private lateinit var binding: ActivityDetailpengaduanlistBinding

    lateinit var presenter: DetailpengaduanlistPresenter
    lateinit var pengaduan: DataPengaduan
    lateinit var prefsManager: PrefsManager

    private var uriImage: Uri? = null
    private var pickImage = 1
    lateinit var imvBukti: ImageView

    private val STORAGE_CADE = 1001

    private lateinit var sLoading: SweetAlertDialog
    private lateinit var sSuccess: SweetAlertDialog
    private lateinit var sError: SweetAlertDialog
    private lateinit var sAlert: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailpengaduanlistBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        presenter = DetailpengaduanlistPresenter(this)
        prefsManager = PrefsManager(this)

    }

    override fun onStart() {
        super.onStart()
        presenter.getPengaduan(Constant.PENGADUAN_ID)
    }

    override fun initActivity() {

        binding.tvTitle.text = "Detail Pengaduan";

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        sLoading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sSuccess = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil")
        sError = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Gagal !")
        sAlert = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Peringatan !")

    }

    override fun initListener() {

        val adapter = ViewPagerAdapter(this.supportFragmentManager)
        adapter.addFragment(ProsesFragment(), "Proses")
        adapter.addFragment(KomentarFragment(), "Komentar")
//        binding.btnViepager.adapter = adapter
//        binding.btnTabs.setupWithViewPager(binding.btnViepager)

        binding.btnLihatSelengkapnya.setOnClickListener {
            startActivity(Intent(this, DetailProsesActivity::class.java))
        }

    }

    override fun onLoading(loading: Boolean, message: String?) {
        when (loading) {
            true -> {
                sLoading.setContentText(message).show()
            }
            false -> {
                sLoading.dismiss()
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng (pengaduan.latitude!!.toDouble(), pengaduan.longitude!!.toDouble())
        googleMap.addMarker ( MarkerOptions(). position(latLng).title( pengaduan.patokan ))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }

    override fun onResult(responsePengaduandetail: ResponsePengaduandetail) {
        pengaduan = responsePengaduandetail.data!!

        GlideHelper.setImage( applicationContext,Constant.IP_IMAGE + pengaduan.gambar!!, binding.viewPager)
        binding.tvName.text = pengaduan.kategori.nama
        binding.tvDescription.text = pengaduan.deskripsi
        binding.tvLokasi.text = pengaduan.patokan
        binding.tvStatus.text = pengaduan.status
        binding.tanggalProses.text = pengaduan.tanggal_proses
        binding.tanggalselesai.text = pengaduan.tanggal_selesai

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapss) as SupportMapFragment
        mapFragment.getMapAsync( this )

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImage && resultCode == Activity.RESULT_OK) {
            uriImage = data!!.data
            imvBukti.setImageURI(uriImage)
        }
    }

    override fun showSuccesOk(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismissWithAnimation()
                finish()
                startActivity(Intent(this, UserActivity::class.java))
            }
            .show()
    }

    override fun showSucces(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("Ok")
            .setConfirmClickListener {
                it.dismissWithAnimation()
            }.show()
    }

    override fun showError(message: String) {
        sError
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismiss()
            }.show()
    }

    override fun showAlert(message: String) {
        sAlert
            .setContentText(message)
            .setConfirmText("Ya")
            .setConfirmClickListener {
                it.dismissWithAnimation()
            }
            .setConfirmText("Nanti")
            .setConfirmClickListener {
                it.dismiss()
            }.show()
        sAlert.setCancelable(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }


}