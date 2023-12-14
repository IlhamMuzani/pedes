package com.syaiful.pengaduan.ui.fragment.home.proses

import android.content.Context
import android.content.Intent
import android.text.Html
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
import com.syaiful.pengaduan.ui.fragment.home.HomeAdapter
import com.syaiful.pengaduan.ui.fragment.home.detailpengaduanlist.DetailpengaduanlistActivity
import com.syaiful.pengaduan.ui.utils.GlideHelper
import kotlin.collections.ArrayList


class ProsesAdapter(
    val context: Context,
    var detailDataPengaduan: ArrayList<DetailDataPengaduan>,
    val clickListener: (DetailDataPengaduan, Int, String) -> Unit,
    val detailClickListener: (DetailDataPengaduan, Int) -> Unit // Tambahkan clickListener baru

) : RecyclerView.Adapter<ProsesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_proses, parent, false)
    )

    override fun getItemCount() = detailDataPengaduan.size


    override fun onBindViewHolder(holder: ProsesAdapter.ViewHolder, position: Int) {
        holder.bind(detailDataPengaduan[position])

        // Set clickListener untuk item pengaduan
        holder.view.setOnClickListener {
            clickListener(detailDataPengaduan[position], position, "some_string")
        }

        // Load image using GlideHelper
        GlideHelper.setImage(holder.itemView.context, Constant.IP_IMAGE + (detailDataPengaduan[position].gambar ?: ""), holder.ivImage)

        // Set clickListener untuk layoutDetail
        holder.view.findViewById<View>(R.id.layouthomes).setOnClickListener {
            detailClickListener(detailDataPengaduan[position], position)
            Constant.PENGADUAN_ID = detailDataPengaduan[position].id!!
            holder.itemView.context.startActivity(Intent(holder.itemView.context, DetailpengaduanlistActivity::class.java))
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view = view
        // Declare views to be bound to the DataPengaduan object
        internal val ivImage = view.findViewById<ImageView>(R.id.iv_images)
        private val txvSomeText = view.findViewById<TextView>(R.id.tv_deskripsiss)
//        private val txvKategoris = view.findViewById<TextView>(R.id.tv_kategoris)

        fun bind(detailDataPengaduan: DetailDataPengaduan) {
            // Set the maximum number of characters you want to display
            val maxChars = 50

            // Truncate the text and add ellipses if it's too long
            val truncatedText = if (detailDataPengaduan.deskripsi!!.length > maxChars) {
                "${detailDataPengaduan.deskripsi.substring(0, maxChars)} <b>Selengkapnya..</b>"
            } else {
                detailDataPengaduan.deskripsi
            }

            // Use HTML formatting to make "Selengkapnya" bold
            txvSomeText.text = Html.fromHtml(truncatedText, Html.FROM_HTML_MODE_COMPACT)
//            txvKategoris.text = detailDataPengaduan.pengaduan.kategori.nama
        }
    }

    fun setData(newDetailDataPengaduan: List<DetailDataPengaduan>) {
        detailDataPengaduan.clear()
        detailDataPengaduan.addAll(newDetailDataPengaduan)
        notifyDataSetChanged()
    }
}
