package com.syaiful.pengaduan.ui.fragment.notifications.tabs.selesai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.komentar.DataKomentar
import com.syaiful.pengaduan.data.model.komentar.ResponseKomentar
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog

class KomentarFragment : Fragment(), KomentarContract.View {

    lateinit var presenter: KomentarPresenter
    lateinit var selesaiAdapter: KomentarAdapter
    lateinit var komentar: DataKomentar
    lateinit var prefsManager: PrefsManager

    lateinit var rcvSelesai: RecyclerView
    lateinit var Swipe: SwipeRefreshLayout
    lateinit var layoutkosong: LinearLayout
//    lateinit var lin_refresh: LinearLayout
    lateinit var gambarKosong: ImageView
    lateinit var layoutkosonglogin: LinearLayout
    lateinit var BtnKomens: Button

    lateinit var sLoading: SweetAlertDialog
    lateinit var sAlert: SweetAlertDialog
    lateinit var sError: SweetAlertDialog
    lateinit var sSuccess: SweetAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_komentar, container, false)

        presenter = KomentarPresenter(this)
        prefsManager = PrefsManager(requireActivity())

        initFragment(view)

        return view
    }

    override fun onStart() {
        super.onStart()
        presenter.getKomentar(Constant.PENGADUAN_ID.toString())
    }

    override fun initFragment(view: View) {
        sLoading = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        sSuccess = SweetAlertDialog(requireActivity(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil")
        sError = SweetAlertDialog(requireActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText("Gagal")
        sAlert = SweetAlertDialog(requireActivity(), SweetAlertDialog.WARNING_TYPE).setTitleText("Perhatian!")

        rcvSelesai = view.findViewById(R.id.rcvSelesai)
        Swipe = view.findViewById(R.id.swipeKom)
        layoutkosong = view.findViewById(R.id.layoutdatakosongselesai)
        gambarKosong = view.findViewById(R.id.gambardatakosong3)
        BtnKomens = view.findViewById(R.id.tambahkomentar)


        selesaiAdapter = KomentarAdapter(
            requireActivity(),
            arrayListOf()
        ) { dataKomentar: DataKomentar, position: Int, type: String ->

            komentar = dataKomentar

        }
        rcvSelesai.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = selesaiAdapter
        }


        Swipe.setOnRefreshListener {
            if (prefsManager.prefIsLogin) {
                presenter.getKomentar(Constant.PENGADUAN_ID.toString())
            }
        }

        BtnKomens.setOnClickListener {
            showKomentar()
        }

        gambarKosong.setOnClickListener {
            showError("Data Tidak Ada !!")
        }
    }

    override fun onloading(loading: Boolean) {
        when(loading){
            true -> Swipe.isRefreshing = true
            false -> Swipe.isRefreshing = false
        }
    }
    override fun onloadingswet(loading: Boolean, message: String?) {
        when(loading){
            true -> sLoading.setContentText(message).show()
            false -> sLoading.dismiss()
        }
    }

    override fun onResult(responseKomentar: ResponseKomentar) {
        if (responseKomentar.status) {
            val transaksi: List<DataKomentar> = responseKomentar.data
            selesaiAdapter.setData(transaksi)
            layoutkosong.visibility = View.GONE
            rcvSelesai.visibility = View.VISIBLE
        }else{
            layoutkosong.visibility = View.VISIBLE
            rcvSelesai.visibility = View.GONE
        }
    }

    override fun onResultKomen(responseKomentar: ResponseKomentar) {
        if (responseKomentar.status) {
            showSuccessOk(responseKomentar.message)
        } else {
            showError(responseKomentar.message)
        }    }

    override fun showKomentar() {
        val dialog = BottomSheetDialog(requireActivity())
        val view = layoutInflater.inflate(R.layout.dialog_komentar, null)
        val btnBatals = view.findViewById<Button>(R.id.btnBatal)
        val btnKomens = view.findViewById<Button>(R.id.btnKomentar)
        val EdtKomen = view.findViewById<EditText>(R.id.edtKomentar)

        btnBatals.setOnClickListener {
            dialog.dismiss()
        }

        btnKomens.setOnClickListener {
            val komentar = EdtKomen.text
            if (komentar.isNullOrEmpty()) {
                showError("Masukkan Komentar")
            } else {
                presenter.Komentar(prefsManager.prefsId, Constant.PENGADUAN_ID.toString(), komentar.toString())
                dialog.dismiss()
            }
        }

        dialog.setContentView(view)
        dialog.show()
    }

    override fun showSuccessOk(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismiss()
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