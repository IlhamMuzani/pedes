package com.syaiful.pengaduan.data.model.kategori

import com.google.gson.annotations.SerializedName

data class ResponseKategoriList(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Datakategori>
)