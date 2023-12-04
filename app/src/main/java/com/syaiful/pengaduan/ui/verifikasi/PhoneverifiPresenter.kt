package com.syaiful.pengaduan.ui.verifikasi


import com.syaiful.pengaduan.data.model.user.ResponseUser
import com.syaiful.pengaduan.data.model.user.ResponseUserdetail
import com.syaiful.pengaduan.data.model.user.ResponseVerifikasi
import com.syaiful.pengaduan.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhoneverifiPresenter(val view: PhoneverifiContract.View) : PhoneverifiContract.Presenter {

    init {
        view.initActivity()
        view.initListener()
        view.onLoading(false)
    }
    override fun verifikasi(id: Long, kode: String) {
        view.onLoading(true, "Loading...")
        ApiService.endpoint.verifikasi(id, kode)
            .enqueue(object : Callback<ResponseVerifikasi> {
                override fun onResponse(
                    call: Call<ResponseVerifikasi>,
                    response: Response<ResponseVerifikasi>
                ) {
                    view.onLoading(false)
                    if (response.isSuccessful) {
                        val responseVerifikasi: ResponseVerifikasi? = response.body()
                        view.onResult( responseVerifikasi!! )
                    }
                }

                override fun onFailure(call: Call<ResponseVerifikasi>, t: Throwable) {
                    view.onLoading(false)
                }

            })
    }
}