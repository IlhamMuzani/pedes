package com.syaiful.pengaduan.ui.maps

interface MapsContract {

    interface View {
        fun initActivity()
        fun initListener()
        fun onLoading(loading: Boolean)
        fun showMessage(message: String)
    }
}