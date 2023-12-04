package com.syaiful.pengaduan.ui.fragment.notifications.tabs.proses

import com.syaiful.pengaduan.data.model.pengaduan.DataPengaduan
import com.syaiful.pengaduan.data.model.pengaduan.DetailDataPengaduan
import com.syaiful.pengaduan.data.model.pengaduan.ResponseDetailPengaduanList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanUpdate

interface ProsesContract {

    interface Presenter {
        fun getProses(id : String)
    }

    interface View{
        fun initFragment(view: android.view.View)
        fun onloading(loading: Boolean)
        fun onloadingswet(loading: Boolean, message: String? = "Loading..")
        fun onResult(responseDetailPengaduanList: ResponseDetailPengaduanList)
        fun showSuccessOk(message: String)
        fun showSuccess(message: String)
        fun showError(message: String)
        fun showAlert(message: String)
    }
}