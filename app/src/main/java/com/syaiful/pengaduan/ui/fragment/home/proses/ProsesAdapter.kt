package com.syaiful.pengaduan.ui.fragment.home.proses

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.syaiful.pengaduan.R
import com.syaiful.pengaduan.data.model.Constant
import com.syaiful.pengaduan.data.model.pengaduan.DataPengaduan
import com.syaiful.pengaduan.data.model.pengaduan.DetailDataPengaduan
import com.syaiful.pengaduan.ui.fragment.home.detailpengaduanlist.DetailpengaduanlistActivity
import com.syaiful.pengaduan.ui.utils.GlideHelper
import kotlin.collections.ArrayList


class ProsesAdapter(
    val context: Context,
    var detailDataPengaduan: ArrayList<DetailDataPengaduan>,
    val clickListener: (DetailDataPengaduan, Int, String) -> Unit,
) : RecyclerView.Adapter<ProsesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_proses, parent, false)
    )

    override fun getItemCount() = detailDataPengaduan.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(detailDataPengaduan[position])

        // Set clickListener untuk item pengaduan
        holder.view.setOnClickListener {
            clickListener(detailDataPengaduan[position], position, "some_string")
        }

        // Load image using GlideHelper
//        GlideHelper.setImage(holder.itemView.context, Constant.IP_IMAGE + (dataPengaduan[position].gambar ?: ""), holder.ivImage)

        // Set clickListener untuk layoutDetail
        holder.view.findViewById<View>(R.id.layoutproses).setOnClickListener {
            Constant.PENGADUAN_ID = detailDataPengaduan[position].id!!
            holder.itemView.context.startActivity(Intent(holder.itemView.context, DetailpengaduanlistActivity::class.java))
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view = view
        // Declare views to be bound to the DataPengaduan object
        internal val ivImage = view.findViewById<ImageView>(R.id.imvImageproses)
        private val txvSomeText = view.findViewById<TextView>(R.id.txvDeskripsiproses)
//        private val txvKategoris = view.findViewById<TextView>(R.id.txvKategoriproses)

        fun bind(detailDataPengaduan: DetailDataPengaduan) {
//            GlideHelper.setImage(itemView.context, Constant.IP_IMAGE + (dataPengaduan.gambar ?: ""), ivImage)
            txvSomeText.text = detailDataPengaduan.deskripsi
        }
    }

    fun setData(newDetailDataPengaduan: List<DetailDataPengaduan>) {
        detailDataPengaduan.clear()
        detailDataPengaduan.addAll(newDetailDataPengaduan)
        notifyDataSetChanged()
    }
}
