package com.syaiful.pengaduan.ui.verifikasi

import com.google.firebase.auth.PhoneAuthCredential
import com.syaiful.pengaduan.data.model.user.ResponseUser
import com.syaiful.pengaduan.data.model.user.ResponseUserdetail
import com.syaiful.pengaduan.data.model.user.ResponseVerifikasi

interface PhoneverifiContract {

    interface Presenter {
        fun verifikasi(id:Long, kode: String)
    }

    interface View {
        fun initActivity()
        fun initListener()
        fun onLoading(loading: Boolean, message: String? = "Loading...")
        fun setupInput()
        fun onResult(responseVerifikasi: ResponseVerifikasi)
        fun showSuccesOk(message: String)
        fun showSucces(message: String)
        fun showError(message: String)
        fun showAlert(message: String)
    }
}