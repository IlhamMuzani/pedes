package com.syaiful.pengaduan.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.pengaduan.DataPengaduan
import com.syaiful.pengaduan.data.model.pengaduan.ResponsePengaduanList
import com.syaiful.pengaduan.ui.fragment.UserActivity
import com.syaiful.pengaduan.ui.fragment.home.proses.ProsesAdapter
import com.syaiful.pengaduan.ui.pengaduan.AddpengaduanActivity
import com.syaiful.pengaduan.ui.sweetalert.SweetAlertDialog

class HomeFragment : Fragment(), HomeContract.View {

    lateinit var prefsManager: PrefsManager
    lateinit var presenter: HomePresenter
    lateinit var pengaduan: DataPengaduan
    lateinit var pengaduanAdapter: HomeAdapter
    lateinit var Fab: Button

    lateinit var RcvPengaduan : RecyclerView
    lateinit var Swipe : SwipeRefreshLayout
    lateinit var EditSearch : EditText

    private lateinit var sLoading: SweetAlertDialog
    private lateinit var sSuccess: SweetAlertDialog
    private lateinit var sError: SweetAlertDialog
    private lateinit var sAlert: SweetAlertDialog
    private var isActivityLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        presenter = HomePresenter(this)
        prefsManager = PrefsManager(requireActivity())

        initFragment(view)

        return view
    }

    override fun onStart() {
        super.onStart()
        presenter.getPengaduan()

    }

    override fun initFragment(view: View) {

        sLoading = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        sSuccess = SweetAlertDialog(requireActivity(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("Berhasil")
        sError = SweetAlertDialog(requireActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText("Gagal !")
        sAlert = SweetAlertDialog(requireActivity(), SweetAlertDialog.WARNING_TYPE).setTitleText("Peringatan !")
        Fab = view.findViewById(R.id.tambahpengaduan1)


        RcvPengaduan = view.findViewById(R.id.rcvPengaduan)
        Swipe = view.findViewById(R.id.swipe)
        EditSearch = view.findViewById(R.id.edtSearch)

        pengaduanAdapter = HomeAdapter(
            requireActivity(),
            arrayListOf(),
            { dataPengaduan: DataPengaduan, position: Int, type: String ->
                pengaduan = dataPengaduan
                when (type) {
                }
            },
            { dataPengaduan: DataPengaduan, position: Int ->
//                Constant.PENGADUAN_ID = dataPengaduan.id!!
//                requireActivity().startActivity(Intent(requireActivity(), DetailpengaduanlistActivity::class.java))
            }
        )

        RcvPengaduan.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = pengaduanAdapter
        }

        Swipe.setOnRefreshListener {
            presenter.getPengaduan()
        }

        EditSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                Constant.KEYWORD = EditSearch.text.toString()
                presenter.Searchpengaduan(Constant.KEYWORD)
                true
            } else {
                false
            }
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
        pengaduanAdapter.onRecyclerViewVisible()
    }

    override fun onLoading(loading: Boolean) {
        when(loading){
            true -> Swipe.isRefreshing = true
            false -> Swipe.isRefreshing = false
        }
    }


    override fun onLoadingswet(loading: Boolean, message: String?) {
        when (loading){
            true -> sLoading.setTitleText(message).show()
            false -> sLoading.dismiss()
        }
    }

    override fun onResult(responsePengaduanList: ResponsePengaduanList) {
        val dataPengaduan: List<DataPengaduan> = responsePengaduanList.data
        if (responsePengaduanList.status) {
            pengaduanAdapter.setData(dataPengaduan)
        } else {
            showError(responsePengaduanList.message)
        }
    }

    override fun showSuccesOk(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("Ok")
            .setConfirmClickListener {
                it.dismissWithAnimation()
                startActivity(Intent(requireActivity(), UserActivity::class.java))
            }.show()
    }

    override fun showSucces(message: String) {
        sSuccess
            .setContentText(message)
            .setConfirmText("Ok")
            .setConfirmClickListener {
                it.dismissWithAnimation()
            }.show()
    }

    override fun showError(message: String) {
        sError
            .setContentText(message)
            .setConfirmText("Gagal")
            .setConfirmClickListener {
                it.dismiss()
            }.show()
    }

    override fun showAlert(message: String) {
        sAlert
            .setContentText(message)
            .setConfirmText("Ya")
            .setConfirmClickListener {
                it.dismissWithAnimation()
            }
            .setConfirmText("Nanti")
            .setConfirmClickListener {
                it.dismiss()
            }.show()
        sAlert.setCancelable(true)
    }

}