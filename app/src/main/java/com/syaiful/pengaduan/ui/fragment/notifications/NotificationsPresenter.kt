package com.syaiful.pengaduan.ui.fragment.notifications

import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanUpdate
import com.syaiful.pengaduan.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsPresenter (var view: NotificationsContract.View) : NotificationsContract.Presenter {

    override fun getStatusmenunggu(kd_user: Long) {
        view.onloading(true)
        view.onloadingswet(true, "Loading..")
        ApiService.endpoint.getMypengaduan(kd_user).enqueue(object :
            Callback<ResponsePengaduanList> {
            override fun onResponse(
                call: Call<ResponsePengaduanList>,
                response: Response<ResponsePengaduanList>
            ) {
                view.onloading(false)
                view.onloadingswet(false, "Loading..")
                if (response.isSuccessful){
                    val responsePengaduanList: ResponsePengaduanList? = response.body()
                    view.onResult(responsePengaduanList!!)
                }
            }

            override fun onFailure(call: Call<ResponsePengaduanList>, t: Throwable) {
                view.onloading(false)
                view.onloadingswet(false, "Loading..")
            }

        })
    }

    override fun deletepengaduan(id: Long) {
        view.onloading(true)
        view.onloadingswet(true, "Loading..")
        ApiService.endpoint.deletepengaduan(id).enqueue(object : Callback<ResponsePengaduanUpdate> {
            override fun onResponse(
                call: Call<ResponsePengaduanUpdate>,
                response: Response<ResponsePengaduanUpdate>
            ) {
                view.onloading(false)
                view.onloadingswet(false, "Loading..")
                if (response.isSuccessful) {
                    val responsePengaduanUpdate: ResponsePengaduanUpdate? = response.body()
                    view.onResultDelete( responsePengaduanUpdate!! )
                }
            }

            override fun onFailure(call: Call<ResponsePengaduanUpdate>, t: Throwable) {
                view.onloading(false)
                view.onloadingswet(false, "Loading..")
            }

        })
    }

}