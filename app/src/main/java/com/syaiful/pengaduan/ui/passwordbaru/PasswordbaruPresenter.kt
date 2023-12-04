package com.syaiful.pengaduan.ui.passwordbaru


import com.syaiful.pengaduan.data.model.user.ResponseUser
import com.syaiful.pengaduan.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordbaruPresenter (val view: PasswordbaruContract.View) : PasswordbaruContract.Presenter {

    init {
        view.initActivity()
        view.initListener()
        view.onLoading(false)
    }

    override fun passwordbaru(id: Long, password: String, password_confirmation: String) {
        view.onLoading(true, "Loading..")
        ApiService.endpoint.Passwordbaru(id, password, password_confirmation).enqueue(object : Callback<ResponseUser>{
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                view.onLoading(false)
                if (response.isSuccessful){
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