package com.syaiful.pengaduan.ui.detailpengaduan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.pengaduan.DataPengaduan
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduandetail
import com.syaiful.pengaduan.databinding.ActivityDetailpengaduanBinding
import com.syaiful.pengaduan.ui.fragment.UserActivity
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog
import com.syaiful.pengaduan.ui.utils.GlideHelper

class DetailpengaduanActivity : AppCompatActivity(), DetailpengaduanContract.View {
    private lateinit var binding: ActivityDetailpengaduanBinding

    lateinit var presenter: DetailpengaduanPresenter
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
        binding = ActivityDetailpengaduanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        presenter = DetailpengaduanPresenter(this)
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
//    override fun onShowDialogdetailgambar() {
//        val dialog = BottomSheetDialog(this)
//        val view = layoutInflater.inflate(R.layout.dialog_gambar, null)
//        val imvgambar = view.findViewById<ImageView>(R.id.imvgambar)
//
//        GlideHelper.setImage(this, Constant.IP_IMAGE + transaksi.bukti, imvgambar)
//
//        dialog.setContentView(view)
//        dialog.show()
//    }


    override fun onResult(responsePengaduandetail: ResponsePengaduandetail) {
        pengaduan = responsePengaduandetail.data!!

        GlideHelper.setImage( applicationContext,Constant.IP_IMAGE + pengaduan.gambar!!, binding.viewPager)
        binding.tvName.text = pengaduan.kategori.nama
        binding.tvDescription.text = pengaduan.deskripsi

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