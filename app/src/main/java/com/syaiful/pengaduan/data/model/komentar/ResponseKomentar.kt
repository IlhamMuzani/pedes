package com.syaiful.pengaduan.data.model.komentar

import com.google.gson.annotations.SerializedName

data class ResponseKomentar(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<DataKomentar>
)