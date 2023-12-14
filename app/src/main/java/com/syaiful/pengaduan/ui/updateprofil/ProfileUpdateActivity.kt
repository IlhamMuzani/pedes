package com.syaiful.pengaduan.ui.updateprofil

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.PhoneAuthProvider
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.user.DataUser
import com.syaiful.pengaduan.data.model.user.ResponseUserdetail
import com.syaiful.pengaduan.databinding.ActivityProfileUpdateBinding
import com.syaiful.pengaduan.databinding.ActivityRegisterBinding
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog
import java.util.concurrent.TimeUnit

class ProfileUpdateActivity : AppCompatActivity(), ProfileUpdateContract.View {
    private lateinit var binding: ActivityProfileUpdateBinding

    lateinit var prefManager: PrefsManager
    lateinit var presenter: ProfileUpdatePresenter
    lateinit var user: DataUser

    private lateinit var sLoading: SweetAlertDialog
    private lateinit var sSuccess: SweetAlertDialog
    private lateinit var sError: SweetAlertDialog
    private lateinit var sAlert: SweetAlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUpdateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        prefManager = PrefsManager(this)
        presenter = ProfileUpdatePresenter(this)
        presenter.userDetail(prefManager.prefsId)

    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    @SuppressLint("SetTextI18n")
    override fun initActivity() {
        binding.tvTitle.text = "Perbarui Profil";

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        sLoading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sSuccess = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil")
        sError = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Gagal!")
        sAlert = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Perhatian!")

    }

    override fun initListener() {
        binding.tvTitle.text = "Perbarui Profil";

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            if (binding.etName.text!!.isEmpty()) {
                showError("Masukkan Nama !")
            } else if (binding.etPhone.text!!.isEmpty()) {
                showError("Masukkan Telp !")
            } else {
                presenter.userUpdateProfile(
                    Constant.USER_ID,
                    binding.etName.text.toString(),
                    binding.etPhone.text.toString()
                )
            }
        }
    }


        override fun onLoading(loading: Boolean, message: String?) {
        when (loading) {
            true -> sLoading.setTitleText(message).show()
            false -> sLoading.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResultDetail(responseUserdetail: ResponseUserdetail) {
        user = responseUserdetail.data!!

//        if (user.foto.isNullOrEmpty()){
//        }else{
//            GlideHelper.setImage(this, Constant.IP_IMAGE + user.foto, fotoprofile)
//        }

        binding.etName.setText( user.nama )
        binding.etPhone.setText( user.telp )


    }

    override fun onResultUpdate(responseUserDetail: ResponseUserdetail) {
        val status: Boolean = responseUserDetail.status
        val message: String = responseUserDetail.message!!
        if (status){
//            val user: DataUser = responseUserDetail.data!!

            showSuccess(message)
        }else{
                showError(message)
        }
    }

    override fun showSuccess(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismissWithAnimation()
                finish()
            }
            .show()
    }

    override fun showError(message: String) {
        sError
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismiss()
            }
            .show()
    }

    override fun showSuccessVerify(message: String) {
//        sSuccess
//            .setContentText(message)
//            .setConfirmText("OK")
//            .setConfirmClickListener {
//                it.dismissWithAnimation()
//                finish()
//                val intent = Intent(applicationContext, OtpActivity::class.java)
//                intent.putExtra("phone", phone)
//                intent.putExtra("verificationId", storedVerificationId)
//                startActivity(intent)
//            }
//            .show()
    }

    override fun showAlertVerify(phone: String, message: String) {
        sAlert
            .setContentText(message)
            .setConfirmText("Ya")
            .setConfirmClickListener {
                it.dismissWithAnimation()
//                startPhoneNumberVerification(phone)
            }
            .setCancelText("Nanti")
            .setCancelClickListener {
                it.dismiss()
            }
            .show()
        sAlert.setCancelable(true)
    }

    override fun validationError(editText: EditText, message: String) {
        editText.error = message
        editText.requestFocus()
    }

    private fun isAlphabetical(input: String): Boolean {
        return input.all { it.isLetter() }
    }
}