package com.syaiful.pengaduan.data.model.pengaduan

import com.google.gson.annotations.SerializedName
import com.syaiful.pengaduan.data.model.kategori.Datakategori

class DataPengaduan (
    @SerializedName( "id") val id: Long?,
    @SerializedName( "user_id") val user_id: String?,
    @SerializedName("kategori_id") val kategori_id: String?,
    @SerializedName("deskripsi") val deskripsi: String?,
    @SerializedName("lokasi") val alamat: String?,
    @SerializedName("patokan") val patokan: String?,
    @SerializedName("latitude") val latitude: String?,
    @SerializedName("longitude") val longitude: String?,
    @SerializedName("gambar") val gambar: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("tanggal_proses") val tanggal_proses: String?,
    @SerializedName("tanggal_selesai") val tanggal_selesai: String?,
    @SerializedName("kategori") val kategori: Datakategori,
    )