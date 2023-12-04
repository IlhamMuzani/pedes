package com.syaiful.pengaduan.ui.komentar

import com.syaiful.pengaduan.data.model.kategori.ResponseKategoriList
import com.syaiful.pengaduan.data.model.komentar.ResponseKomentar
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import java.io.File

interface AddkomentarContract {

    interface Presenter {
        fun insertKomentar(user_id: String, pengaduan_id: String, komentar: String)

    }

    interface View {
        fun initActivity()
        fun initListener()
        fun onLoading(loading: Boolean, message: String? = "Loading...")
        fun onloadingswet(loading: Boolean, message: String? = "Loading..")
        fun onResult(responseKomentar: ResponseKomentar)
        fun showSuccesOk(message: String)
        fun showSucces(message: String)
        fun showError(message: String)
        fun showAlert(message: String)
    }
}