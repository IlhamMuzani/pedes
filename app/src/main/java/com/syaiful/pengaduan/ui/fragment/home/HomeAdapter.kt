package com.syaiful.pengaduan.ui.fragment.home

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
import com.syaiful.pengaduan.ui.fragment.home.detailpengaduanlist.DetailpengaduanlistActivity
import com.syaiful.pengaduan.ui.utils.GlideHelper
import kotlin.collections.ArrayList



class HomeAdapter(
    val context: Context,
    var dataPengaduan: ArrayList<DataPengaduan>,
    val clickListener: (DataPengaduan, Int, String) -> Unit,
    val detailClickListener: (DataPengaduan, Int) -> Unit // Tambahkan clickListener baru
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var isActivityLoaded = false
    fun onRecyclerViewVisible() {
        isActivityLoaded = false
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.adapter_home, parent, false)
    )

    override fun getItemCount() = dataPengaduan.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataPengaduan[position])

        // Set clickListener untuk item pengaduan
        holder.view.setOnClickListener {
            clickListener(dataPengaduan[position], position, "some_string")
        }

        // Load image using GlideHelper
        GlideHelper.setImage(holder.itemView.context, Constant.IP_IMAGE + (dataPengaduan[position].gambar ?: ""), holder.ivImage)

        // Set clickListener untuk layoutDetail
        holder.view.findViewById<View>(R.id.layouthome).setOnClickListener {
            if (!isActivityLoaded) {
                // Setel isActivityLoaded menjadi true sebelum melakukan intent
                isActivityLoaded = true

                // Panggil detailClickListener
                detailClickListener(dataPengaduan[position], position)

                // Lakukan intent
                Constant.PENGADUAN_ID = dataPengaduan[position].id!!
                holder.itemView.context.startActivity(
                    Intent(
                        holder.itemView.context,
                        DetailpengaduanlistActivity::class.java
                    )
                )
            }
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view = view
        // Declare views to be bound to the DataPengaduan object
        internal val ivImage = view.findViewById<ImageView>(R.id.iv_image)
        private val txvSomeText = view.findViewById<TextView>(R.id.tv_deskripsis)
        private val txvKategoris = view.findViewById<TextView>(R.id.tv_kategori)

        fun bind(dataPengaduan: DataPengaduan) {
            // Set the maximum number of characters you want to display
            val maxChars = 50

            // Truncate the text and add ellipses if it's too long
            val truncatedText = if (dataPengaduan.deskripsi!!.length > maxChars) {
                "${dataPengaduan.deskripsi.substring(0, maxChars)} <b>Selengkapnya..</b>"
            } else {
                dataPengaduan.deskripsi
            }

            // Use HTML formatting to make "Selengkapnya" bold
            txvSomeText.text = Html.fromHtml(truncatedText, Html.FROM_HTML_MODE_COMPACT)
            txvKategoris.text = dataPengaduan.kategori.nama
        }
    }


    fun setData(newDataPengaduan: List<DataPengaduan>) {
        dataPengaduan.clear()
        dataPengaduan.addAll(newDataPengaduan)
        notifyDataSetChanged()
    }

    fun removePengaduan(position: Int) {
        dataPengaduan.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataPengaduan.size)
    }
}
