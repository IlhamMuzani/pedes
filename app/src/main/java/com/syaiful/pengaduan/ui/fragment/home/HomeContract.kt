package com.syaiful.pengaduan.ui.fragment.home

import com.syaiful.pengaduan.data.model.komentar.ResponseKomentar
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList


interface HomeContract {

    interface Presenter{
        fun getPengaduan()
        fun Searchpengaduan(keyword: String)
    }

    interface View{

        fun initFragment(view: android.view.View)
        fun onLoading(loading: Boolean)
        fun onLoadingswet(loading: Boolean, message: String? = "Menampilkan..")
        fun showSuccesOk(message: String)
        fun showSucces(message: String)
        fun showError(message: String)
        fun showAlert(message: String)
        fun onResult(responsePengaduanList: ResponsePengaduanList)

    }
}