package com.syaiful.pengaduan.ui.updateprofil

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.PhoneAuthProvider
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.user.ResponseUserdetail
import com.syaiful.pengaduan.databinding.ActivityProfileUpdateBinding
import com.syaiful.pengaduan.databinding.ActivityRegisterBinding
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog
import java.util.concurrent.TimeUnit

class ProfileUpdateActivity : AppCompatActivity(), ProfileUpdateContract.View {
    private lateinit var binding: ActivityProfileUpdateBinding

    lateinit var prefManager: PrefsManager
    lateinit var presenter: ProfileUpdatePresenter

    private lateinit var sLoading: SweetAlertDialog
    private lateinit var sSuccess: SweetAlertDialog
    private lateinit var sError: SweetAlertDialog
    private lateinit var sAlert: SweetAlertDialog

    private var checkLocation: Int = 0

    private lateinit var oldphone: String

    // Firebase Phone
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var code: String? = null
    var phone: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUpdateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        prefManager = PrefsManager(this)
        presenter = ProfileUpdatePresenter(this)

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
        binding.tvTitle.text = "Update Profil";

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        sLoading = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sSuccess = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil")
        sError = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Gagal!")
        sAlert = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Perhatian!")

    }

    override fun initListener() {
//        iv_back.setOnClickListener {
//            onBackPressed()
//        }

//        binding.btn_save.setOnClickListener {
//            val radioId = rg_gender.checkedRadioButtonId
//            val radioButton = findViewById<RadioButton>(radioId)
//            val gender = radioButton.text
////            Toast.makeText(applicationContext, "$gender", Toast.LENGTH_SHORT).show()
//            val name = et_name.text
//            phone = et_phone.text.trim().toString()
//            val address = tv_address.text
//            when {
//                name.isEmpty() -> {
//                    validationError(et_name, "Nama lengkap tidak boleh kosong!")
//                }
//                !isAlphabetical(name.toString()) -> {
//                    showError("Tidak dapat memasukan simbol atau angka pada kolom nama!")
//                }
//                phone.isEmpty() -> {
//                    validationError(et_phone, "Nomor telepon tidak boleh kosong!")
//                }
//                phone.length < 9 -> {
//                    showError("Masukan nomor telepon dengan benar")
//                }
//                tv_address.text == "" -> {
//                    showError("Alamat tidak sesuai!")
//                }
//                gender.isEmpty() -> {
//                    showError("Pilih jenis kelamin!")
//                }
//                else ->
//                    presenter.userUpdateProfile(
//                        prefManager.prefId,
//                        name.toString(),
//                        oldphone,
//                        phone,
//                        gender.toString(),
//                        address.toString()
//                    )
//            }
//        }
    }

    override fun onLoading(loading: Boolean, message: String?) {
        when (loading) {
            true -> sLoading.setTitleText(message).show()
            false -> sLoading.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResultDetail(responseUserdetail: ResponseUserdetail) {
        val status: Boolean = responseUserdetail.status
        val message: String = responseUserdetail.message!!

//        if (status) {
//            val user = responseUserdetail.user!!
//            et_name.setText(user.name)
//            et_phone.setText(user.phone)
//            oldphone = user.phone!!
//            when (user.gender) {
//                "Laki-laki" -> {
//                    rb_male.isChecked = true
//                }
//                "Perempuan" -> {
//                    rb_female.isChecked = true
//                }
//                else -> {
//                    rb_male.isChecked = true
//                }
//            }
//            if (user.address != null) {
//                layout_address.visibility = View.VISIBLE
//                if (checkLocation == 0) {
//                    Constant.LATITUDE = user.latitude!!
//                    Constant.LONGITUDE = user.longitude!!
//                    tv_address.text = user.address
//                    btn_location.text = "Ubah Lokasi"
//                    checkLocation = 1
//                }
//            } else {
//                tv_address.visibility = View.GONE
//                btn_location.text = "Tambah Lokasi"
//            }
//        } else {
//            showError(message)
//        }
    }

    override fun onResultUpdate(responseUserDetail: ResponseUserdetail) {
        val status: Boolean = responseUserDetail.status
        val message: String = responseUserDetail.message!!

//        if (status) {
//            if (message == "Lakukan verifikasi OTP untuk memperbarui nomor telepon") {
//                val user = responseUserDetail.user!!
//                Constant.UPDATE = true
//                Constant.USER_ID = user.id!!
////                Toast.makeText(applicationContext, "+62$phone", Toast.LENGTH_SHORT).show()
//                showAlertVerify("+62$phone", message)
//            } else {
//                showSuccess(message)
//            }
//        } else {
//            showError(message)
//        }
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