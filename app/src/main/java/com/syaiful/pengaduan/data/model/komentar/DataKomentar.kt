package com.syaiful.pengaduan.data.model.komentar

import com.google.gson.annotations.SerializedName
import com.syaiful.pengaduan.data.model.kategori.Datakategori
import com.syaiful.pengaduan.data.model.user.DataUser

class DataKomentar (
    @SerializedName( "id") val id: Long?,
    @SerializedName( "user_id") val user_id: String?,
    @SerializedName("pengaduan_id") val pengaduan_id: String?,
    @SerializedName("komentar") val komentar: String?,
    @SerializedName("user") val user: DataUser,
    )