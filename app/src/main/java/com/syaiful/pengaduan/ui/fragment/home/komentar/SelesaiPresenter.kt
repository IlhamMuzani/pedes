package com.syaiful.pengaduan.ui.fragment.notifications.tabs.selesai

import com.syaiful.pengaduan.data.model.komentar.ResponseKomentar
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanUpdate
import com.syaiful.pengaduan.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelesaiPresenter (var view: SelesaiContract.View) : SelesaiContract.Presenter {


    override fun getKomentar(id: String) {
//        view.onloading(true)
        view.onloadingswet(true, "Loading..")
        ApiService.endpoint.getKomentar(id).enqueue(object : Callback<ResponseKomentar>{
            override fun onResponse(
                call: Call<ResponseKomentar>,
                response: Response<ResponseKomentar>
            ) {
//                view.onloading(false)
                view.onloadingswet(false, "Loading..")
                if (response.isSuccessful){
                    val responseKomentar: ResponseKomentar? = response.body()
                    view.onResult(responseKomentar!!)
                }
            }

            override fun onFailure(call: Call<ResponseKomentar>, t: Throwable) {
//                view.onloading(false)
                view.onloadingswet(false, "Loading..")
            }

        })
    }

    override fun Komentar(user_id: String, pengaduan_id: String, komentar: String) {
        view.onloadingswet(true, "Loading..")
        ApiService.endpoint.InsertKomentar(user_id, pengaduan_id, komentar).enqueue(object : Callback<ResponseKomentar>{
            override fun onResponse(
                call: Call<ResponseKomentar>,
                response: Response<ResponseKomentar>
            ) {
                view.onloadingswet(false)
                if (response.isSuccessful){
                    val responseKomentar:ResponseKomentar? = response.body()
                    view.onResultKomen(responseKomentar!!)
                }
            }

            override fun onFailure(call: Call<ResponseKomentar>, t: Throwable) {
                view.onloadingswet(false)
            }

        })
    }


}