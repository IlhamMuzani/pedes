package com.syaiful.pengaduan.ui.updateprofil

import android.widget.EditText
import com.syaiful.pengaduan.data.model.user.ResponseUserdetail
import java.io.File

interface ProfileUpdateContract {
    interface Presenter {
        fun userDetail(id: String)
        fun userUpdateProfile(
            id: Long,
            name: String,
            old_phone: String,
            new_phone: String,
            gender: String,
            address: String
        )
    }

    interface View {
        fun initActivity()
        fun initListener()
        fun onLoading(loading: Boolean, message: String? = "Loading...")
        fun onResultDetail(responseUserdetail: ResponseUserdetail)
        fun onResultUpdate(responseUserdetail: ResponseUserdetail)
        fun showSuccess(message: String)
        fun showError(message: String)
        fun showSuccessVerify(message: String)
        fun showAlertVerify(phone: String, message: String)
        fun validationError(editText: EditText, message: String)
    }

}