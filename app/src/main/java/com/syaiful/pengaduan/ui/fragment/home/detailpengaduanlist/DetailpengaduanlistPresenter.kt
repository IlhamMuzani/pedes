package com.syaiful.pengaduan.ui.fragment.home.detailpengaduanlist

import com.syaiful.pengaduan.data.model.komentar.ResponseKomentar
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanUpdate
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduandetail
import com.syaiful.pengaduan.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailpengaduanlistPresenter (val view: DetailpengaduanlistContract.View) : DetailpengaduanlistContract.Presenter {

    init {
        view.initActivity()
        view.initListener()
        view.onLoading(false)
    }

    override fun getPengaduan(id: Long) {
        view.onLoading(true, "Loading..")
        ApiService.endpoint.detailpengaduan(id).enqueue(object : Callback<ResponsePengaduandetail>{
            override fun onResponse(
                call: Call<ResponsePengaduandetail>,
                response: Response<ResponsePengaduandetail>
            ) {
                view.onLoading(false)
                if (response.isSuccessful){
                    val responsePengaduandetail:ResponsePengaduandetail? = response.body()
                    view.onResult(responsePengaduandetail!!)
                }
            }

            override fun onFailure(call: Call<ResponsePengaduandetail>, t: Throwable) {
                view.onLoading(false)
            }

        })
    }

}