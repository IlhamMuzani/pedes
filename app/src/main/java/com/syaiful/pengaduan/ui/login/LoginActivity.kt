package com.syaiful.pengaduan.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.user.DataUser
import com.syaiful.pengaduan.data.model.user.ResponseUser
import com.syaiful.pengaduan.databinding.ActivityLoginBinding
import com.syaiful.pengaduan.ui.fragment.UserActivity
import com.syaiful.pengaduan.ui.register.RegisterActivity
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog

class LoginActivity : AppCompatActivity(), LoginContract.View {
    private lateinit var binding: ActivityLoginBinding

    lateinit var presenter: LoginPresenter
    lateinit var prefsManager: PrefsManager

    private lateinit var sLoading: SweetAlertDialog
    private lateinit var sSuccess: SweetAlertDialog
    private lateinit var sError: SweetAlertDialog
    private lateinit var sAlert: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        presenter = LoginPresenter(this)
        prefsManager = PrefsManager(this)
    }

    override fun initActivity() {

        binding.tvTitle.text = "Login";

//        binding.ivBack.setOnClickListener {
//            onBackPressed()
//        }

        sLoading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sSuccess = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil")
        sError = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Gagal !")
        sAlert = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Peringatan !")

    }

    override fun initListener() {

//        binding.btnLogin.setOnClickListener {
//            startActivity(Intent(this, RegisterActivity::class.java))
//        }

        dummy()

//        ivKembali.setOnClickListener {
//            onBackPressed()
//        }
//
        binding.btnLogin.setOnClickListener {
            if (binding.editTelp.text!!.isEmpty()){
                showError("Masukkan Nomor Telepon !")
            }else if (binding.editPassword.text!!.isEmpty()){
                showError("Masukkan Password !")
            }else{
                presenter.doLogin(binding.editTelp.text.toString(), binding.editPassword.text.toString())
            }
        }

        binding.textViewRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvLogin.setOnClickListener {
            dummy()
        }
//
//        txtlupapassword.setOnClickListener {
//            startActivity(Intent(this, CheckUserActivity::class.java))
//        }
    }

    override fun onLoading(loading: Boolean, message: String?) {
        when (loading) {
            true -> sLoading.setConfirmText(message).show()
            false -> sLoading.dismiss()
        }
    }

    override fun onResult(responseUser: ResponseUser) {
        val status: Boolean = responseUser.status
        val message: String = responseUser.message
        if (status){
            val user: DataUser = responseUser.data!!

            presenter.setPrefs(prefsManager, user)
            showSuccesOk(message)
        }else{
            if (status == false){
                showError(message)
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
                // Do not start the activity here
            }
            .show()

        // Delay for 2 seconds before dismissing the alert
        Handler(Looper.getMainLooper()).postDelayed({
            sSuccess.dismissWithAnimation()

            // Delay for an additional 2 seconds before starting the new activity
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, UserActivity::class.java))
            }, 500L) // 2000 milliseconds = 2 seconds
        }, 1000L) // 2000 milliseconds = 2 seconds
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

    fun dummy() {
        binding.editTelp.setText("81234567890")
        binding.editPassword.setText("syaiful")
    }
}