package com.syaiful.pengaduan.ui.fragment.notifications

import com.syaiful.pengaduan.data.model.pengaduan.DataPengaduan
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanUpdate

interface NotificationsContract {

    interface Presenter {
        fun getStatusmenunggu(kd_user : Long)
        fun deletepengaduan(id: Long)
    }

    interface View{
        fun initFragment(view: android.view.View)
        fun onloading(loading: Boolean)
        fun onloadingswet(loading: Boolean, message: String? = "Loading..")
        fun onResult(responsePengaduanList: ResponsePengaduanList)
        fun onResultDelete(responsePengaduanUpdate: ResponsePengaduanUpdate)
        fun showDialogDelete(dataPengaduan: DataPengaduan, position: Int)
        fun showSuccessOk(message: String)
        fun showSuccess(message: String)
        fun showError(message: String)
        fun showAlert(message: String)
    }
}