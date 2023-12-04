package com.syaiful.pengaduan.data.model.user

import com.google.gson.annotations.SerializedName

data class ResponseUserdetail (
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: DataUser?
)