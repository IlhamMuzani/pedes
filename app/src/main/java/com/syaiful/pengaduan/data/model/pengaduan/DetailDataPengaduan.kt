package com.syaiful.pengaduan.data.model.pengaduan

import com.google.gson.annotations.SerializedName
import com.syaiful.pengaduan.data.model.kategori.Datakategori

class DetailDataPengaduan (
    @SerializedName( "id") val id: Long?,
    @SerializedName( "pengaduan_id") val pengaduan_id: String?,
    @SerializedName("deskripsi") val deskripsi: String?,
    @SerializedName("gambar") val gambar: String?,
    @SerializedName("pengaduan") val pengaduan: DataPengaduan,

    )