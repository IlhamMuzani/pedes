package com.syaiful.pengaduan.ui.fragment.akun

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.user.ResponseUserdetail
import com.syaiful.pengaduan.ui.fragment.UserActivity
import com.syaiful.pengaduan.ui.fragment.fragment.akun.AkunContract
import com.syaiful.pengaduan.ui.fragment.fragment.akun.AkunPresenter
import com.syaiful.pengaduan.ui.login.LoginActivity
import com.syaiful.pengaduan.ui.passwordbaru.PasswordbaruActivity
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog
import com.syaiful.pengaduan.ui.updateprofil.ProfileUpdateActivity
import com.syaiful.pengaduan.ui.utils.GlideHelper

class AkunFragment : Fragment(), AkunContract.View {

    lateinit var prefsManager: PrefsManager
    lateinit var presenter: AkunPresenter

    lateinit var BtnUbahProfil: LinearLayout
    lateinit var Btnpasswordbaru: LinearLayout
    lateinit var txvNama: TextView
    lateinit var imvGambar: ImageView
    lateinit var BtnLogout: TextView

    lateinit var title : TextView

    lateinit var sLoading: SweetAlertDialog
    lateinit var sAlert: SweetAlertDialog
    lateinit var sError: SweetAlertDialog
    lateinit var sSuccess: SweetAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_akun, container, false)

        initFragment(view)

        prefsManager = PrefsManager(requireActivity())
        presenter = AkunPresenter(this)
        presenter.doLogin(prefsManager)


        return view
    }

    override fun initFragment(view: View) {
        sLoading = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        sSuccess = SweetAlertDialog(
            requireActivity(),
            SweetAlertDialog.SUCCESS_TYPE
        ).setTitleText("Berhasil")
        sError =
            SweetAlertDialog(requireActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText("Gagal")
        sAlert = SweetAlertDialog(
            requireActivity(),
            SweetAlertDialog.WARNING_TYPE
        ).setTitleText("Perhatian!")

//        MapsHelper.permissionMap(requireContext(), requireActivity())
        BtnUbahProfil = view.findViewById(R.id.layout_profile)
        Btnpasswordbaru = view.findViewById(R.id.layout_password)
        txvNama = view.findViewById(R.id.tv_name)
//        imvGambar = view.findViewById(R.id.imvgambar)
        BtnLogout = view.findViewById(R.id.btn_logout)
        title = view.findViewById(R.id.tv_title2)

        title.text = "Profil"


//        BtnUbahProfil.setOnClickListener {
//            Constant.USER_ID = prefsManager.prefsId.toLong()
//            startActivity(Intent(requireActivity(), UpdateprofilActivity::class.java))
//        }

        Btnpasswordbaru.setOnClickListener {
            Constant.USER_ID = prefsManager.prefsId.toLong()
            startActivity(Intent(requireActivity(), PasswordbaruActivity::class.java))
        }

        BtnUbahProfil.setOnClickListener {
            Constant.USER_ID = prefsManager.prefsId.toLong()
            startActivity(Intent(requireActivity(), ProfileUpdateActivity::class.java))
        }

        BtnLogout.setOnClickListener {
            presenter.doLogout(prefsManager)
        }

    }

    override fun onStart() {
        super.onStart()
        presenter.profildetail(prefsManager.prefsId)

    }

    override fun onResultLogin(prefsManageruser: PrefsManager) {

    }

    override fun onResultLogout() {
        showSuccessOk("Berhasil Logout")
    }

    override fun onResult(responseUserdetail: ResponseUserdetail) {
        val akun = responseUserdetail.data
        txvNama.setText(akun!!.nama)
//        Txvphone.setText(akun!!.telp)
//        GlideHelper.setImage(requireActivity(), Constant.IP_IMAGE + akun!!.foto, imvGambar)
    }

    override fun showSuccessOk(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismissWithAnimation()
            }
            .show()

        // Delay for 2 seconds before dismissing the alert
        Handler(Looper.getMainLooper()).postDelayed({
            sSuccess.dismissWithAnimation()

            // Delay for an additional 2 seconds before starting the new activity
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            }, 500L) // 2000 milliseconds = 2 seconds
        }, 1000L) // 2000 milliseconds = 2 seconds
    }


    override fun showSuccess(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismissWithAnimation()
            }
            .show()
    }

    override fun showError(message: String) {
        sError
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismiss()
            }
            .show()
    }

    override fun showAlert(message: String) {
        sAlert
            .setContentText(message)
            .setConfirmText("Ya")
            .setConfirmClickListener {
                it.dismissWithAnimation()
            }
            .setCancelText("Nanti")
            .setCancelClickListener {
                it.dismiss()
            }
            .show()
        sAlert.setCancelable(true)
    }
}