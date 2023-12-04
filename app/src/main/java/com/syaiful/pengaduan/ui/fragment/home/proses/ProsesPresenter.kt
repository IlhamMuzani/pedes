package com.syaiful.pengaduan.ui.fragment.notifications.tabs.proses

import com.syaiful.pengaduan.data.model.pengaduan.ResponseDetailPengaduanList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanUpdate
import com.syaiful.pengaduan.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProsesPresenter (var view: ProsesContract.View) : ProsesContract.Presenter {


    override fun getProses(id: String) {
        view.onloading(true)
        view.onloadingswet(true, "Loading..")
        ApiService.endpoint.getProses(id).enqueue(object : Callback<ResponseDetailPengaduanList>{
            override fun onResponse(
                call: Call<ResponseDetailPengaduanList>,
                response: Response<ResponseDetailPengaduanList>
            ) {
                view.onloading(false)
                view.onloadingswet(false, "Loading..")
                if (response.isSuccessful){
                    val responseDetailPengaduanList: ResponseDetailPengaduanList? = response.body()
                    view.onResult(responseDetailPengaduanList!!)
                }
            }

            override fun onFailure(call: Call<ResponseDetailPengaduanList>, t: Throwable) {
                view.onloading(false)
                view.onloadingswet(false, "Loading..")
            }

        })
    }
}