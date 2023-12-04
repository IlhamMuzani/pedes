package com.syaiful.pengaduan.ui.komentar

import com.syaiful.pengaduan.data.model.kategori.ResponseKategoriList
import com.syaiful.pengaduan.data.model.komentar.ResponseKomentar
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

class AddkomentarPresenter(val view: AddkomentarContract.View) : AddkomentarContract.Presenter {

    init {
        view.initActivity()
        view.initListener()
        view.onLoading(false)
        view.onloadingswet(false)
    }

    override fun insertKomentar(
        user_id: String,
        pengaduan_id: String,
        komentar: String,
    ) {

        view.onLoading(true,"Loading...")
        ApiService.endpoint.InsertKomentar(
            user_id,
            pengaduan_id,
            komentar,
        )
            .enqueue(object : Callback<ResponseKomentar> {
                override fun onResponse(
                    call: Call<ResponseKomentar>,
                    response: Response<ResponseKomentar>
                ) {
                    view.onLoading(false)
                    if (response.isSuccessful) {
                        val responseKomentar: ResponseKomentar? = response.body()
                        view.onResult(responseKomentar!!)
                    }
                }

                override fun onFailure(call: Call<ResponseKomentar>, t: Throwable) {
                    view.onLoading(false)
                }

            })
    }

}