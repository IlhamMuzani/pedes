package com.syaiful.pengaduan.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isEmpty
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.user.DataUser
import com.syaiful.pengaduan.data.model.user.ResponseUser
import com.syaiful.pengaduan.databinding.ActivityLoginBinding
import com.syaiful.pengaduan.databinding.ActivityRegisterBinding
import com.syaiful.pengaduan.ui.login.LoginActivity
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog
import com.syaiful.pengaduan.ui.verifikasi.PhoneVerifiActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), RegisterContract.View {
    private lateinit var binding: ActivityRegisterBinding

    lateinit var presenter: RegisterPresenter
    lateinit var telp: String

    lateinit var sLoading: SweetAlertDialog
    lateinit var sError: SweetAlertDialog
    lateinit var sSuccess: SweetAlertDialog
    lateinit var sAlert: SweetAlertDialog

    lateinit var data: DataUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        presenter = RegisterPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        if (Constant.AREA != "") {
//            tv_alamat1.text = Constant.AREA
        }
    }

    override fun initActivity() {

        binding.tvTitle.text = "Register";

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        sLoading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sSuccess = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil")
        sError = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Gagal")
        sAlert = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Peringatan !")

//        tv_nama.text = "Register"

    }

    override fun initListener() {

//        ivKembali.setOnClickListener {
//            onBackPressed()
//        }
//
//        Btn_Lokasi1.setOnClickListener {
//            startActivity(Intent(this, MapsActivity::class.java))
//        }

       binding.btnRegister.setOnClickListener {

         if (binding.edtNama.text!!.isEmpty()) {
                showError("Kolom nama tidak boleh kosong !")
            } else if (binding.edtTelp.text!!.isEmpty()) {
                showError("Kolom telepon tidak boleh kosong !")
            } else if (binding.edtPassword.text!!.isEmpty()) {
                showError("Kolom password tidak boleh kosong !")
//            } else if (edit_Passwordconfirm.text!!.isEmpty()) {
//                showError("Kolom password konfirmasi tidak boleh kosong !")
            } else
                presenter.insertregister(
                    binding.edtNama.text.toString(),
                    binding.edtTelp.text.toString(),
                    binding.edtPassword.text.toString(),
//                    edit_Passwordconfirm.text.toString()
                )
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
            data = responseUser.data!!
            showSuccesOk(responseUser.message)
        } else {
            showError(responseUser.message)
        }
    }

    override fun showSuccesOk(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismissWithAnimation()
                finish()
                Constant.USER_ID = data.id!!
                startActivity(Intent(this, PhoneVerifiActivity::class.java))
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun isAlphabetical(input: String): Boolean {
        return input.all { it.isLetter() }
    }
}