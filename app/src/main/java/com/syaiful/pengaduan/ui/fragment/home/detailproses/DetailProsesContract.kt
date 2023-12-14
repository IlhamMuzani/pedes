package com.syaiful.pengaduan.ui.fragment.home.detailproses

import com.syaiful.pengaduan.data.model.komentar.ResponseKomentar
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanUpdate
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduandetail

interface DetailProsesContract {

    interface Presenter {
        fun getPengaduan(id: Long)
    }

    interface View {
        fun initActivity()
        fun initListener()
        fun onLoading(loading: Boolean, message: String? ="Loading..")
//        fun onShowDialogdetailgambar()
//        fun onResultUpdate(responsePengaduanUpdate: ResponsePengaduanUpdate)
        fun onResult(responsePengaduandetail: ResponsePengaduandetail)
        fun showSuccesOk(message: String)
        fun showSucces(message: String)
        fun showError(message: String)
        fun showAlert(message: String)
    }
}