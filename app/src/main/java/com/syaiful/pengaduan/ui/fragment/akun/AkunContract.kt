package com.syaiful.pengaduan.ui.fragment.fragment.akun

import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.user.ResponseUserdetail


interface AkunContract {

    interface Presenter {
        fun doLogin(prefsManager: PrefsManager)
        fun doLogout(prefsManager: PrefsManager)

        fun profildetail(id:String)
    }

    interface View {
        fun initFragment(view: android.view.View)
        fun onResultLogin(prefsManager: PrefsManager)
        fun onResultLogout()
        fun onResult(responseUserdetail: ResponseUserdetail)
        fun showSuccessOk(message: String)
        fun showSuccess(message: String)
        fun showError(message: String)
        fun showAlert(message: String)
    }

}