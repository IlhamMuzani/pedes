package com.syaiful.pengaduan.network

import com.syaiful.pengaduan.data.model.kategori.ResponseKategoriList
import com.syaiful.pengaduan.data.model.komentar.ResponseKomentar
import com.syaiful.pengaduan.data.model.pengaduan.ResponseDetailPengaduanList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanUpdate
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduandetail
import com.syaiful.pengaduan.data.model.user.ResponseUser
import com.syaiful.pengaduan.data.model.user.ResponseUserdetail
import com.syaiful.pengaduan.data.model.user.ResponseVerifikasi
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiEndpoint {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("nama") nama: String,
        @Field("telp") telp: String,
        @Field("password") password: String,
//        @Field("fcm") fcm: String
    ): Call<ResponseUser>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("telp") telp: String,
        @Field("password") password: String
    ) : Call<ResponseUser>

    @GET("user/show/{id}")
    fun ProfilDetail(
        @Path("id") id: String
    ) : Call<ResponseUserdetail>

    @POST("userUpdateProfile/{id}")
    fun userUpdateProfile(
        @Path("id") id: Long,
        @Query("name") name: String,
        @Query("old_phone") old_phone: String,
        @Query("new_phone") new_phone: String,
        @Query("gender") gender: String,
        @Query("address") address: String,
        @Query("_method") _method: String
    ): Call<ResponseUserdetail>

    @POST("user/u_password/{id}")
    fun Passwordbaru(
        @Path("id") id: Long,
        @Query("password") password: String,
        @Query("password_confirmation") password_confirmation: String,
    ): Call<ResponseUser>


    @GET("pengaduan/list-all")
    fun getPengaduan(
    ): Call<ResponsePengaduanList>


    @GET("pengaduan/list/{id}")
    fun getMypengaduan(
        @Path("id") id: Long
    ): Call<ResponsePengaduanList>

    @GET("pengaduan/list-komentar/{id}")
    fun getKomentar(
        @Path("id") id: String
    ): Call<ResponseKomentar>

    @GET("pengaduan/list-proses/{id}")
    fun getProses(
        @Path("id") id: String
    ): Call<ResponseDetailPengaduanList>
    @POST("verifikasi/{id}")
    fun verifikasi(
        @Path("id") id: Long,
        @Query("kode") kode: String,
    ): Call<ResponseVerifikasi>
    @GET("pengaduan/get-kategori")
    fun getKategori(
    ): Call<ResponseKategoriList>

    @POST("pengaduan-search")
    fun Searchpengaduan(
        @Query("keyword") keyword: String,
    ): Call<ResponsePengaduanList>

    @POST("pengaduan-delete/{id}")
    fun deletepengaduan(
        @Query("user_id") user_id: Long,
    ): Call<ResponsePengaduanUpdate>

    @Multipart
    @POST("pengaduan/store")
    fun InserPengaduan(
        @Query("user_id") user_id: String,
        @Query("kategori_id") kategori_id: String,
        @Query("deskripsi") deskripsi: String,
        @Query("lokasi") lokasi: String,
        @Query("patokan") patokan: String,
        @Part gambar: MultipartBody.Part,
        @Query("latitude") latidude: String,
        @Query("longitude") longitude: String,
    ): Call<ResponsePengaduanList>

    @POST("komentar/store")
    fun InsertKomentar(
        @Query("user_id") user_id: String,
        @Query("pengaduan_id") pengaduan_id: String,
        @Query("komentar") komentar: String,
    ): Call<ResponseKomentar>
    @GET("pengaduan/show/{id}")
    fun detailpengaduan(
        @Path("id") id: Long
    ): Call<ResponsePengaduandetail>

}