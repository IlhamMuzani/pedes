package com.syaiful.pengaduan.ui.pengaduan

import com.syaiful.pengaduan.data.model.kategori.ResponseKategoriList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import java.io.File

interface AddpengaduanContract {

    interface Presenter {
        fun insertPengaduan(user_id: String, kategori_id: String, deskripsi: String, lokasi: String,  patokan: String, gambar: File, latitude: String, longitude: String,
        )

        fun getKategori()

    }

    interface View {
        fun initActivity()
        fun initListener()
        fun onLoading(loading: Boolean, message: String? = "Loading...")
        fun onloadingswet(loading: Boolean, message: String? = "Loading..")
        fun onResult(responsePengaduanList: ResponsePengaduanList)
        fun onResultKategori(responseKategoriList: ResponseKategoriList)
        fun showSuccesOk(message: String)
        fun showSucces(message: String)
        fun showError(message: String)
        fun showAlert(message: String)
    }
}