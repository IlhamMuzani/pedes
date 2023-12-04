package com.syaiful.pengaduan.ui.maps

class MapsPresenter (val view: MapsContract.View) {

    init {
        view.initActivity()
        view.initListener()
        view.onLoading(false)
    }

}