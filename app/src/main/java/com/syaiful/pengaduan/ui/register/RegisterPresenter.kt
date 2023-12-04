package com.syaiful.pengaduan.ui.register


import com.syaiful.pengaduan.data.model.user.ResponseUser
import com.syaiful.pengaduan.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterPresenter (val view: RegisterContract.View) : RegisterContract.Presenter {

    init {
        view.initActivity()
        view.initListener()
        view.onLoading(false)
    }

    override fun insertregister(
        nama: String,
        telp: String,
        password: String,
    ) {
        view.onLoading(true, "Loading..")
        ApiService.endpoint.register(nama, telp, password).enqueue(object: Callback<ResponseUser>{
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                view.onLoading(false)
                if (response.isSuccessful) {
                    val responseUser: ResponseUser? = response.body()
                    view.onResult(responseUser!!)
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                view.onLoading(false)
            }

        })
    }

}