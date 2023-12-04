package com.syaiful.pengaduan.ui.login


import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.user.DataUser
import com.syaiful.pengaduan.data.model.user.ResponseUser
import com.syaiful.pengaduan.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(val view: LoginContract.View) : LoginContract.Presenter {

    init {
        view.initActivity()
        view.initListener()
        view.onLoading(false)
    }

    override fun doLogin(telp: String, password: String) {
        view.onLoading(true, "Loading..")
        ApiService.endpoint.login(telp, password).enqueue(object : Callback<ResponseUser> {
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

    override fun setPrefs(prefsManager: PrefsManager, dataUser: DataUser) {
        prefsManager.prefIsLogin = true
        prefsManager.prefsId = dataUser.id.toString()
        prefsManager.prefs_is_nama = dataUser.nama!!
        prefsManager.prefs_is_telp = dataUser.telp!!
    }
}