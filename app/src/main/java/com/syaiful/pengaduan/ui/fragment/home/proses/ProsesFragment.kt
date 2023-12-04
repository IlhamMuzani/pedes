package com.syaiful.pengaduan.ui.fragment.notifications.tabs.proses

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.komentar.DataKomentar
import com.syaiful.pengaduan.data.model.pengaduan.DataPengaduan
import com.syaiful.pengaduan.data.model.pengaduan.DetailDataPengaduan
import com.syaiful.pengaduan.data.model.pengaduan.ResponseDetailPengaduanList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanUpdate
import com.syaiful.pengaduan.ui.detailpengaduan.DetailpengaduanActivity
import com.syaiful.pengaduan.ui.fragment.home.proses.ProsesAdapter
import com.syaiful.pengaduan.ui.fragment.notifications.tabs.selesai.SelesaiAdapter
import com.syaiful.pengaduan.ui.login.LoginActivity
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog

class ProsesFragment : Fragment(), ProsesContract.View {

    lateinit var presenter: ProsesPresenter
    lateinit var prosesAdapater: ProsesAdapter
    lateinit var detailDataPengaduan: DetailDataPengaduan
    lateinit var prefsManager: PrefsManager

    lateinit var rcvSudahbayar: RecyclerView
    lateinit var swipeSudahbayar: SwipeRefreshLayout
    lateinit var layoutkosong: LinearLayout
//    lateinit var lin_refresh: LinearLayout
    lateinit var gambarKosong: ImageView
    lateinit var logindulu: ImageView
    lateinit var layoutdatakosonglogin: LinearLayout

    lateinit var sLoading: SweetAlertDialog
    lateinit var sAlert: SweetAlertDialog
    lateinit var sError: SweetAlertDialog
    lateinit var sSuccess: SweetAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_proses, container, false)

        presenter = ProsesPresenter(this)
        prefsManager = PrefsManager(requireActivity())

        initFragment(view)

        return view
    }


    override fun onStart() {
        super.onStart()
        presenter.getProses(Constant.PENGADUAN_ID.toString())
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

        rcvSudahbayar = view.findViewById(R.id.rcvSudahbayar)
        swipeSudahbayar = view.findViewById(R.id.swipeSudahbayar)
        layoutkosong = view.findViewById(R.id.layoutdatakosong)
//        lin_refresh = view.findViewById(R.id.lin_refreshbayar)
        gambarKosong = view.findViewById(R.id.gambardatakosong2)
        logindulu = view.findViewById(R.id.gambarkosonglogin2)
        layoutdatakosonglogin = view.findViewById(R.id.layoutkosongLogin2)


        prosesAdapater = ProsesAdapter(
            requireActivity(),
            arrayListOf()
        ) { detailData: DetailDataPengaduan, position: Int, type: String ->

            detailDataPengaduan = detailData

        }

        rcvSudahbayar.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = prosesAdapater
        }

//        lin_refresh.setOnClickListener {
//            if (prefsManager.prefIsLogin) {
//                showSuccess("")
//                presenter.getStatusproses(prefsManager.prefsId.toLong())
//            }
//        }
        gambarKosong.setOnClickListener {
            showError("Data Tidak Ada !!")
        }

        logindulu.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
    }

    override fun onloading(loading: Boolean) {
        when (loading) {
            true -> swipeSudahbayar.isRefreshing = true
            false -> swipeSudahbayar.isRefreshing = false
        }
    }

    override fun onloadingswet(loading: Boolean, message: String?) {
        when (loading) {
            true -> sLoading.setContentText(message).show()
            false -> sLoading.dismiss()
        }
    }

    override fun onResult(responseDetailPengaduanList: ResponseDetailPengaduanList) {
        if (responseDetailPengaduanList.status) {
            val transaksi: List<DetailDataPengaduan> = responseDetailPengaduanList.data
            prosesAdapater.setData(transaksi)
            layoutkosong.visibility = View.GONE
            rcvSudahbayar.visibility = View.VISIBLE
        }else{
            layoutkosong.visibility = View.VISIBLE
            rcvSudahbayar.visibility = View.GONE
        }
    }
    override fun showSuccessOk(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismissWithAnimation()
//                finish()
//                startActivity(Intent(requireActivity(), UserActivity::class.java))
            }
            .show()
    }


    override fun showSuccess(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismiss()
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
//                startPhoneNumberVerification(phone)
            }
            .setCancelText("Nanti")
            .setCancelClickListener {
                it.dismiss()
            }
            .show()
        sAlert.setCancelable(true)
    }

}