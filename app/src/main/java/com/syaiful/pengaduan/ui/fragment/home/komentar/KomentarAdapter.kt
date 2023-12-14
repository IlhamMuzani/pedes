package com.syaiful.pengaduan.ui.fragment.notifications.tabs.selesai

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.komentar.DataKomentar
import com.syaiful.pengaduan.ui.utils.GlideHelper
import kotlin.collections.ArrayList


class KomentarAdapter (val context: Context, var dataKomentar: ArrayList<DataKomentar>,
                       val clickListener: (DataKomentar, Int, String) -> Unit ):
    RecyclerView.Adapter<KomentarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_komentar, parent, false)
    )

    override fun getItemCount() = dataKomentar.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataKomentar[position])
//        GlideHelper.setImage(holder.itemView.context, Constant.IP_IMAGE + (dataKomentar[position].gambar ?: ""), holder.ivImage)

        // Anda dapat menambahkan clickListener di sini
        holder.view.setOnClickListener {
            clickListener(dataKomentar[position], position, "some_string")
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view = view
        // Deklarasikan tampilan yang akan Anda bind ke objek DataPengaduan
        private val ivImage = view.findViewById<ImageView>(R.id.imvKomentar)
        private val txvUsername = view.findViewById<TextView>(R.id.username)
        private val txvSomeText = view.findViewById<TextView>(R.id.txvKomentar)

        fun bind(dataKomentar: DataKomentar) {
//            txvUsername.text = dataKomentar.user.nama
            txvSomeText.text = dataKomentar.komentar
        }
    }

    fun setData(newDataKomentar: List<DataKomentar>) {
        dataKomentar.clear()
        dataKomentar.addAll(newDataKomentar)
        notifyDataSetChanged()
    }
}
