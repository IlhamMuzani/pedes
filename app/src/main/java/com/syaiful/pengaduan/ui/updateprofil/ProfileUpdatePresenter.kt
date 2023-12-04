package com.syaiful.pengaduan.ui.updateprofil

import com.syaiful.pengaduan.data.model.user.ResponseUserdetail
import com.syaiful.pengaduan.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileUpdatePresenter(val view: ProfileUpdateContract.View): ProfileUpdateContract.Presenter {

    init {
        view.initActivity()
        view.initListener()
    }

    override fun userDetail(id: String) {
        view.onLoading(true, "Menampilkan data...")
        ApiService.endpoint.ProfilDetail(id).enqueue(object : Callback<ResponseUserdetail> {
            override fun onResponse(
                call: Call<ResponseUserdetail>,
                response: Response<ResponseUserdetail>
            ) {
                view.onLoading(false)
                if (response.isSuccessful) {
                    val responseUserdetail: ResponseUserdetail? = response.body()
                    view.onResultDetail(responseUserdetail!!)
                }
            }

            override fun onFailure(call: Call<ResponseUserdetail>, t: Throwable) {
                view.onLoading(false)
            }
        })
    }

    override fun userUpdateProfile(
        id: Long,
        name: String,
        old_phone: String,
        new_phone: String,
        gender: String,
        address: String
    ) {
        view.onLoading(true)
        ApiService.endpoint.userUpdateProfile(
            id,
            name,
            old_phone,
            new_phone,
            gender,
            address,
            "PUT"
        ).enqueue(object : Callback<ResponseUserdetail> {
            override fun onResponse(
                call: Call<ResponseUserdetail>,
                response: Response<ResponseUserdetail>
            ) {
                view.onLoading(false)
                if (response.isSuccessful) {
                    val responseUserUpdate: ResponseUserdetail? = response.body()
                    view.onResultUpdate(responseUserUpdate!!)
                }
            }

            override fun onFailure(call: Call<ResponseUserdetail>, t: Throwable) {
                view.onLoading(false)
            }

        })
    }
}