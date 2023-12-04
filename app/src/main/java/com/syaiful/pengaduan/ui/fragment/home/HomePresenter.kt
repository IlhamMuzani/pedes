package com.syaiful.pengaduan.ui.fragment.home


import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import com.syaiful.pengaduan.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePresenter (var view: HomeContract.View) : HomeContract.Presenter {


    override fun getPengaduan() {
        view.onLoadingswet(true, "Loading..")
        view.onLoading(true)
        ApiService.endpoint.getPengaduan().enqueue(object : Callback<ResponsePengaduanList> {
            override fun onResponse(
                call: Call<ResponsePengaduanList>,
                response: Response<ResponsePengaduanList>
            ) {
                view.onLoadingswet(false)
                view.onLoading(false)
                if (response.isSuccessful) {
                    val responsePengaduanList: ResponsePengaduanList? = response.body()
                    view.onResult( responsePengaduanList!! )
                }
            }

            override fun onFailure(call: Call<ResponsePengaduanList>, t: Throwable) {
                view.onLoadingswet(false)
                view.onLoading(false)
            }

        })
    }

    override fun Searchpengaduan(keyword: String) {
        view.onLoading(true)
        view.onLoadingswet(true, "Loading..")
        ApiService.endpoint.Searchpengaduan(keyword).enqueue( object : Callback<ResponsePengaduanList>{
            override fun onResponse(
                call: Call<ResponsePengaduanList>,
                response: Response<ResponsePengaduanList>
            ) {
                view.onLoading(false)
                view.onLoadingswet(false, "Loading..")
                if (response.isSuccessful){
                    val responsePengaduanList: ResponsePengaduanList? = response.body()
                    view.onResult(responsePengaduanList!!)
                }
            }

            override fun onFailure(call: Call<ResponsePengaduanList>, t: Throwable) {
                view.onLoading(false)
                view.onLoadingswet(false, "Loading..")
            }

        })
    }
}