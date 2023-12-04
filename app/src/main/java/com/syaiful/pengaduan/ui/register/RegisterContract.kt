package com.syaiful.pengaduan.ui.register

import com.syaiful.pengaduan.data.model.user.ResponseUser


interface RegisterContract {

    interface Presenter {
      fun insertregister(nama: String, telp: String, password: String)
    }

    interface View {
        fun initActivity()
        fun initListener()
        fun onLoading(loading: Boolean, message: String?= "Loading..")
        fun onResult(responseUser: ResponseUser)
        fun showSuccesOk(message: String)
        fun showSucces(message: String)
        fun showError(message: String)
        fun showAlert(message: String)
    }
}