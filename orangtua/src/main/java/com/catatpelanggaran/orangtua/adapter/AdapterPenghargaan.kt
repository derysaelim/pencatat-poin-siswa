package com.catatpelanggaran.orangtua.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.orangtua.R
import com.catatpelanggaran.orangtua.model.Penghargaan
import kotlinx.android.synthetic.main.item_pelanggaran.view.*

class AdapterPenghargaan(val Penghargaan: ArrayList<Penghargaan>) :
    RecyclerView.Adapter<AdapterPenghargaan.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterPenghargaan.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pelanggaran, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterPenghargaan.ViewHolder, position: Int) {
        holder.bind(Penghargaan[position])
    }

    override fun getItemCount(): Int = Penghargaan.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataPenghargaan: Penghargaan) {
            with(itemView) {
                val nomor = position + 1

                nama_pel.text = dataPenghargaan.namaPenghargaan
                if (dataPenghargaan.tanggal == null) {
                    tanggal_pel.text = "Poin : ${dataPenghargaan.poin}"
                } else {
                    tanggal_pel.text = "Tanggal : ${dataPenghargaan.tanggal}"
                }
                no_absenpel.text = nomor.toString()
            }
        }
    }

}