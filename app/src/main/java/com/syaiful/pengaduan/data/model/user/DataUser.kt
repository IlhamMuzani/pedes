package com.syaiful.pengaduan.data.model.user

import com.google.gson.annotations.SerializedName

class DataUser(
    @SerializedName("id") val id: Long?,
    @SerializedName("nama") val nama: String?,
    @SerializedName("telp") val telp: String?,
//    @SerializedName("alamat") val alamat: String?,
//    @SerializedName("foto") val foto: String?,
    @SerializedName("role") val role: String?,
//    @SerializedName("kode") val kode: String?
)