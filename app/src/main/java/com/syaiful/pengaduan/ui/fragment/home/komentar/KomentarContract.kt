package com.syaiful.pengaduan.ui.fragment.notifications.tabs.selesai

import com.syaiful.pengaduan.data.model.komentar.ResponseKomentar

interface KomentarContract {

    interface Presenter {
        fun getKomentar(id: String)

        fun Komentar(user_id: String, pengaduan_id: String, komentar: String)

    }

    interface View{
        fun initFragment(view: android.view.View)
        fun onloading(loading: Boolean)
        fun onloadingswet(loading: Boolean, message: String? = "Loading..")
        fun onResult(responseKomentar: ResponseKomentar)
        fun onResultKomen(responseKomentar: ResponseKomentar)

        fun showKomentar()

        fun showSuccessOk(message: String)
        fun showSuccess(message: String)
        fun showError(message: String)
        fun showAlert(message: String)
    }
}