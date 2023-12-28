package com.syaiful.pengaduan.ui.fragment.notifications

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.pengaduan.DataPengaduan
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanUpdate
import com.syaiful.pengaduan.ui.detailpengaduan.DetailpengaduanActivity
import com.syaiful.pengaduan.ui.pengaduan.AddpengaduanActivity
import com.syaiful.pengaduan.ui.login.LoginActivity
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog

class NotificationsFragment : Fragment(), NotificationsContract.View {

    lateinit var presenter: NotificationsPresenter
    lateinit var menungguAdapter: PengaduanAdapter
    lateinit var pengaduan: DataPengaduan
    lateinit var prefsManager: PrefsManager

    lateinit var Fab: Button

    lateinit var layoutdatakosong: LinearLayout
    lateinit var rcvMenunggu: RecyclerView
    lateinit var swipeMenunggu: SwipeRefreshLayout
    //    lateinit var  liner_refresh : LinearLayout
    lateinit var layoutbelumlogin : LinearLayout
    lateinit var gambarKosong : ImageView
    lateinit var logindulu : ImageView
    lateinit var title : TextView
    private var isActivityLoaded = false

    lateinit var sLoading: SweetAlertDialog
    lateinit var sAlert: SweetAlertDialog
    lateinit var sError: SweetAlertDialog
    lateinit var sSuccess: SweetAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        presenter = NotificationsPresenter(this)
        prefsManager = PrefsManager(requireActivity())

        initFragment(view)

        return view
    }

    override fun onStart() {
        super.onStart()
        if (prefsManager.prefIsLogin) {
            presenter.getStatusmenunggu(prefsManager.prefsId.toLong())
//            liner_refresh.visibility = View.VISIBLE
            layoutbelumlogin.visibility = View.GONE
            Fab.visibility = View.VISIBLE
        }else{
            layoutbelumlogin.visibility = View.VISIBLE
            Fab.visibility = View.GONE
//            liner_refresh.visibility = View.GONE
        }
    }

    override fun initFragment(view: View) {
        sLoading = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        sSuccess = SweetAlertDialog(requireActivity(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil")
        sError = SweetAlertDialog(requireActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText("Gagal")
        sAlert = SweetAlertDialog(requireActivity(), SweetAlertDialog.WARNING_TYPE).setTitleText("Perhatian!")

        Fab = view.findViewById(R.id.tambahpengaduan)
        rcvMenunggu = view.findViewById(R.id.rcvMenunggu)
        swipeMenunggu = view.findViewById(R.id.swipeMenunggu)
        layoutdatakosong = view.findViewById(R.id.layoutdatatidakada)
//        liner_refresh = view.findViewById(R.id.liner_refresh)
        gambarKosong = view.findViewById(R.id.gambarkosong)
        logindulu = view.findViewById(R.id.gambarkosonglogin)
        layoutbelumlogin = view.findViewById(R.id.layoutkosongLogin)
        title = view.findViewById(R.id.tv_title1)

        title.text = "Pengaduan Anda";


        menungguAdapter = PengaduanAdapter(
            requireActivity(),
            arrayListOf(),
            { dataPengaduan: DataPengaduan, position: Int, type: String ->
                pengaduan = dataPengaduan
                when (type) {
                    "Delete" -> showDialogDelete(dataPengaduan, position)
                }
            },
            { dataPengaduan: DataPengaduan, position: Int ->
//                Constant.PENGADUAN_ID = dataPengaduan.id!!
//                requireActivity().startActivity(Intent(requireActivity(), DetailpengaduanActivity::class.java))
            }
        )
        rcvMenunggu.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = menungguAdapter
        }

        swipeMenunggu.setOnRefreshListener {
            if (prefsManager.prefIsLogin) {
                presenter.getStatusmenunggu(prefsManager.prefsId.toLong())
            }
        }

//        liner_refresh.setOnClickListener {
//            if (prefsManager.prefIsLogin) {
//                showSuccess("")
//                presenter.getStatusmenunggu(prefsManager.prefsId.toLong())
//            }
//        }

        gambarKosong.setOnClickListener {
            showError("Data Tidak Ada !!")
        }

        logindulu.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }

        Fab.setOnClickListener { view ->
            if (!isActivityLoaded) {
                startActivity(Intent(requireActivity(), AddpengaduanActivity::class.java))
                isActivityLoaded = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Setel isActivityLoaded menjadi false agar aktivitas dapat dimuat lagi
        isActivityLoaded = false
        menungguAdapter.onRecyclerViewVisible()
    }

    override fun onloading(loading: Boolean) {
        when (loading) {
            true -> swipeMenunggu.isRefreshing = true
            false -> swipeMenunggu.isRefreshing = false
        }
    }

    override fun onloadingswet(loading: Boolean, message: String?) {
        when (loading) {
            true -> sLoading.setContentText(message).show()
            false -> sLoading.dismiss()
        }
    }

    override fun onResult(responsePengaduanList: ResponsePengaduanList) {
        if (responsePengaduanList.status) {
            val transaksi: List<DataPengaduan> = responsePengaduanList.data
            menungguAdapter.setData(transaksi)
            layoutdatakosong.visibility = View.GONE
            rcvMenunggu.visibility = View.VISIBLE
        }else{
            layoutdatakosong.visibility = View.VISIBLE
            rcvMenunggu.visibility = View.GONE
        }
    }

    override fun onResultDelete(responsePengaduanUpdate: ResponsePengaduanUpdate) {
        showSuccessOk(responsePengaduanUpdate.message)
    }

    override fun showDialogDelete(dataPengaduan: DataPengaduan, position: Int) {
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle("Konfirmasi")
        dialog.setMessage("Hapus ${dataPengaduan.deskripsi}?")

        dialog.setPositiveButton("Hapus") { dialog, which ->
            presenter.deletepengaduan(Constant.PENGADUAN_ID)
            menungguAdapter.removePengaduan(position)
            dialog.dismiss()
        }

        dialog.setNegativeButton("Batal") { dialog, which ->
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun showSuccessOk(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("OK")
            .setConfirmClickListener {
                it.dismissWithAnimation()
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