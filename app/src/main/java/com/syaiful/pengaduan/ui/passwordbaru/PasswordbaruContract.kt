package com.syaiful.pengaduan.ui.passwordbaru

import com.syaiful.pengaduan.data.model.user.ResponseUser


interface PasswordbaruContract {

    interface Presenter {
        fun passwordbaru(id: Long, password: String, password_confirmation: String)
    }

    interface View {
        fun initActivity()
        fun initListener()
        fun onLoading(loading: Boolean, message: String? = "Loading..")
        fun onResult(responseUser: ResponseUser)
        fun showSuccesOk(message: String)
        fun showSucces(message: String)
        fun showError(message: String)
        fun showAlert(message: String)
    }
}