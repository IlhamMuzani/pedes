package com.syaiful.pengaduan.ui.verifikasi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.user.ResponseUser
import com.syaiful.pengaduan.data.model.user.ResponseUserdetail
import com.syaiful.pengaduan.data.model.user.ResponseVerifikasi
import com.syaiful.pengaduan.databinding.ActivityPhoneverifiBinding
import com.syaiful.pengaduan.databinding.ActivityRegisterBinding
import com.syaiful.pengaduan.ui.login.LoginActivity
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog
import java.util.concurrent.TimeUnit

class PhoneVerifiActivity : AppCompatActivity(), PhoneverifiContract.View {
    private lateinit var binding: ActivityPhoneverifiBinding


    lateinit var sLoading: SweetAlertDialog
    lateinit var sError: SweetAlertDialog
    lateinit var sSuccess: SweetAlertDialog
    lateinit var sAlert: SweetAlertDialog

    lateinit var presenter : PhoneverifiPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneverifiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        presenter = PhoneverifiPresenter(this)

    }

    override fun onStart() {
        super.onStart()
        binding.edtotp1.setText("")
        binding.edtotp2.setText("")
        binding.edtotp3.setText("")
        binding.edtotp4.setText("")
        binding.edtotp5.setText("")
        binding.edtotp6.setText("")
    }

    override fun onResume() {
        super.onResume()
        binding.edtotp1.setText("")
        binding.edtotp2.setText("")
        binding.edtotp3.setText("")
        binding.edtotp4.setText("")
        binding.edtotp5.setText("")
        binding.edtotp6.setText("")

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    override fun initActivity() {
        sLoading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sSuccess = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil")
        sError = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Gagal")
        sAlert = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Peringatan !")

        setupInput()

    }

    override fun initListener() {

        binding.btnVerifi.setOnClickListener {

            val otp1 = binding.edtotp1.text.toString().trim()
            val otp2 = binding.edtotp2.text.toString().trim()
            val otp3 = binding.edtotp3.text.toString().trim()
            val otp4 = binding.edtotp4.text.toString().trim()
            val otp5 = binding.edtotp5.text.toString().trim()
            val otp6 = binding.edtotp6.text.toString().trim()
            if (otp1.isEmpty() || otp2.isEmpty()
                || otp3.isEmpty() || otp4.isEmpty()
                || otp5.isEmpty() || otp6.isEmpty()
            ) {
                Toast.makeText(this, "Masukan kode OTP dengan benar!", Toast.LENGTH_SHORT).show()
            } else {
                val code = otp1 + otp2 + otp3 + otp4 + otp5 + otp6

                    presenter.verifikasi(Constant.USER_ID, code)
            }

        }
    }

    override fun onLoading(loading: Boolean, message: String?) {
        when(loading){
            true -> sLoading.setTitleText(message).show()
            false -> sLoading.dismiss()
        }
    }

    override fun onResult(responseVerifikasi: ResponseVerifikasi) {
        if (responseVerifikasi.status) {
            showSuccesOk(responseVerifikasi.message)
        } else {
            showError(responseVerifikasi.message)
        }
    }


    override fun setupInput() {
        binding.edtotp1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.edtotp2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.edtotp2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.edtotp3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.edtotp3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.edtotp4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.edtotp4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.edtotp5.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.edtotp5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    binding.edtotp6.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    override fun showSuccesOk(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismissWithAnimation()
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
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