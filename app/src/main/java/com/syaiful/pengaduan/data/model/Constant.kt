package com.syaiful.pengaduan.data.model

class Constant {
    companion object {

//        var IP: String = "http://192.168.43.224/pengaduan_desa/"
        var IP: String = "https://syaiful-kirom.com/"
        var IP_IMAGE: String = IP + "public/storage/uploads/"

        var LATITUDE: String = ""
        var LONGITUDE: String = ""

        const val LOCATION_PERMISSION_REQUEST_CODE = 1;
        var FORGET: Boolean = false
        var UPDATE: Boolean = false
        var AREA: String = ""
        var USER_ID: Long = 0
        var PENGADUAN_ID: Long = 0
        var KEYWORD: String = ""

        var KATEGORI_ID: Int = 0
        var KATEGORINAME: String = ""

            var PATOKAN_ID: Int = 0
            var PATOKANNAME: String = ""

        var IS_CHANGED: Boolean = false
    }
}