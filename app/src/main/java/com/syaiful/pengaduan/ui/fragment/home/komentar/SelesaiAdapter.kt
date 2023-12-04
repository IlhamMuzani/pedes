package com.syaiful.pengaduan.ui.fragment.notifications.tabs.selesai

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.model.komentar.DataKomentar
import com.syaiful.pengaduan.data.model.pengaduan.DataPengaduan
import kotlin.collections.ArrayList


class SelesaiAdapter (val context: Context, var dataKomentar: ArrayList<DataKomentar>,
                       val clickListener: (DataKomentar, Int, String) -> Unit ):
    RecyclerView.Adapter<SelesaiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_komentar, parent, false)
    )

    override fun getItemCount() = dataKomentar.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataKomentar[position])
//        GlideHelper.setImage(context, Constant.IP_IMAGE + dataPengaduan[position].gambar!!, holder.view.ivImage)

        // Anda dapat menambahkan clickListener di sini
        holder.view.setOnClickListener {
            clickListener(dataKomentar[position], position, "some_string")
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view = view
        // Deklarasikan tampilan yang akan Anda bind ke objek DataPengaduan
        private val ivImage = view.findViewById<ImageView>(R.id.imvKomentar)
        private val txvSomeText = view.findViewById<TextView>(R.id.txvKomentar)

        fun bind(dataKomentar: DataKomentar) {
            // Bind data dari objek DataPengaduan ke tampilan yang sesuai
//            GlideHelper.setImage(context, Constant.IP_IMAGE + dataPengaduan.gambar!!, ivImage)
            txvSomeText.text = dataKomentar.komentar // Gantilah dengan nama properti yang sesuai pada DataPengaduan
        }
    }

    fun setData(newDataKomentar: List<DataKomentar>) {
        dataKomentar.clear()
        dataKomentar.addAll(newDataKomentar)
        notifyDataSetChanged()
    }
}
