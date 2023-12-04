package com.syaiful.pengaduan.ui.pengaduan

import com.syaiful.pengaduan.data.model.kategori.ResponseKategoriList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import com.syaiful.pengaduan.data.model.user.ResponseUserdetail
import com.syaiful.pengaduan.network.ApiService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddpengaduanPresenter(val view: AddpengaduanContract.View) : AddpengaduanContract.Presenter {

    init {
        view.initActivity()
        view.initListener()
        view.onLoading(false)
        view.onloadingswet(false)
    }

    override fun insertPengaduan(
        user_id: String,
        kategori_id: String,
        deskripsi: String,
        lokasi: String,
        patokan: String,
        gambar: File,
        latitude: String,
        longitude: String,
    ) {

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), gambar)
        val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData(
            "gambar",
            gambar.name, requestBody
        )

        view.onLoading(true,"Loading...")
        ApiService.endpoint.InserPengaduan(
            user_id,
            kategori_id,
            deskripsi,
            lokasi,
            patokan,
            multipartBody!!,
            latitude,
            longitude,
        )
            .enqueue(object : Callback<ResponsePengaduanList> {
                override fun onResponse(
                    call: Call<ResponsePengaduanList>,
                    response: Response<ResponsePengaduanList>
                ) {
                    view.onLoading(false)
                    if (response.isSuccessful) {
                        val responsePengaduanList: ResponsePengaduanList? = response.body()
                        view.onResult(responsePengaduanList!!)
                    }
                }

                override fun onFailure(call: Call<ResponsePengaduanList>, t: Throwable) {
                    view.onLoading(false)
                }

            })
    }

    override fun getKategori() {
        view.onLoading(true)
        view.onloadingswet(true, "Loading..")
        ApiService.endpoint.getKategori().enqueue(object : Callback<ResponseKategoriList>{
            override fun onResponse(
                call: Call<ResponseKategoriList>,
                response: Response<ResponseKategoriList>
            ) {
                view.onLoading(false)
                view.onloadingswet(false, "Loading..")
                if (response.isSuccessful){
                    val responseKategoriList: ResponseKategoriList? = response.body()
                    view.onResultKategori(responseKategoriList!!)
                }
            }

            override fun onFailure(call: Call<ResponseKategoriList>, t: Throwable) {
                view.onLoading(false)
                view.onloadingswet(false, "Loading..")
            }

        })
    }


}