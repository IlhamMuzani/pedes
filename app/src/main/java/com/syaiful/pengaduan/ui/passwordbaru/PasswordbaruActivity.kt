package com.syaiful.pengaduan.ui.passwordbaru

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.user.ResponseUser
import com.syaiful.pengaduan.databinding.ActivityAddpengaduanBinding
import com.syaiful.pengaduan.databinding.ActivityPasswordbaruBinding
import com.syaiful.pengaduan.databinding.ActivityPhoneverifiBinding
import com.syaiful.pengaduan.ui.fragment.UserActivity
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog


class PasswordbaruActivity : AppCompatActivity(), PasswordbaruContract.View {
    private lateinit var binding: ActivityPasswordbaruBinding

    lateinit var presenter: PasswordbaruPresenter
    lateinit var prefsManager: PrefsManager

    private lateinit var sLoading: SweetAlertDialog
    private lateinit var sSuccess: SweetAlertDialog
    private lateinit var sError: SweetAlertDialog
    private lateinit var sAlert: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordbaruBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        presenter = PasswordbaruPresenter(this)
        prefsManager = PrefsManager(this)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun initActivity() {

        binding.tvTitle.text = "Perbarui Password";

        sLoading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sSuccess = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil")
        sError = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Gagal !")
        sAlert = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Peringatan !")


//        tv_nama.text ="Ubah Password"
    }

    override fun initListener() {

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnPassworbaru.setOnClickListener {
            if (binding.editUbahtextPassword.text!!.isEmpty()){
                showError("Masukkan Password")
            } else if (binding.editUbahkonfirmasiPassword.text!!.isEmpty()){
              showError("Masukkan Konfirmasi Password")
            }
                presenter.passwordbaru(Constant.USER_ID, binding.editUbahtextPassword.text.toString(), binding.editUbahkonfirmasiPassword.text.toString())
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

    override fun onResult(responseUser: ResponseUser) {
        if (responseUser.status) {
            showSuccesOk(responseUser.message)
        }else{
            showError(responseUser.message)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
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
}