package com.catatpelanggaran.gurubk.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.model.Catat
import com.catatpelanggaran.gurubk.model.Pelanggaran
import kotlinx.android.synthetic.main.item_pelanggaran.view.*
import kotlinx.android.synthetic.main.item_siswa.view.*

class AdapterPelanggaran(val pelanggaran: ArrayList<Pelanggaran>) :
    RecyclerView.Adapter<AdapterPelanggaran.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterPelanggaran.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pelanggaran, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = pelanggaran.size

    override fun onBindViewHolder(holder: AdapterPelanggaran.ViewHolder, position: Int) {
        holder.bind(pelanggaran[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataPelanggaran: Pelanggaran) {
            with(itemView) {
                val nomor = position + 1
                nama_pel.text = dataPelanggaran.namaPelanggaran
                poin_pel.text = "Poin = ${dataPelanggaran.poinPelanggaran}"
                no_absenpel.text = nomor.toString()
            }
        }


    }

}