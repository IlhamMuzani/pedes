package com.syaiful.pengaduan.data.model.pengaduan

import com.google.gson.annotations.SerializedName

class ResponsePengaduanUpdate (
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String
)