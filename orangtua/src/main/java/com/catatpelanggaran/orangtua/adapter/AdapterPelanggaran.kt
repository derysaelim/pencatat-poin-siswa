package com.catatpelanggaran.orangtua.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.orangtua.R
import com.catatpelanggaran.orangtua.model.Pelanggaran
import kotlinx.android.synthetic.main.item_pelanggaran.view.*

class AdapterPelanggaran(val pelanggaran: ArrayList<Pelanggaran>) :
    RecyclerView.Adapter<AdapterPelanggaran.ViewHolder>() {

//    var onItemClick: ((Pelanggaran) -> Unit)? = null
//    var onItemDeleteClick: ((Pelanggaran) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterPelanggaran.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pelanggaran, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterPelanggaran.ViewHolder, position: Int) {
        holder.bind(pelanggaran[position])
    }

    override fun getItemCount(): Int = pelanggaran.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataPelanggaran: Pelanggaran) {
            with(itemView) {
                val nomor = position + 1

                nama_pel.text = dataPelanggaran.namaPelanggaran

                if (dataPelanggaran.tanggal == null) {
                    tanggal_pel.text = "Poin : ${dataPelanggaran.poinPelanggaran}"
                } else {
                    tanggal_pel.text = "Tanggal : ${dataPelanggaran.tanggal}"
                }
                no_absenpel.text = nomor.toString()
            }
        }
    }
}