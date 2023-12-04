package com.syaiful.pengaduan.data.model.pengaduan

import com.google.gson.annotations.SerializedName

data class ResponsePengaduanList(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<DataPengaduan>
)