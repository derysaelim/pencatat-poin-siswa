package com.catatpelanggaran.gurubk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.model.Penghargaan
import kotlinx.android.synthetic.main.item_detail.view.*

class AdapterDetailPenghargaan(val detail: ArrayList<Penghargaan>) :
    RecyclerView.Adapter<AdapterDetailPenghargaan.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterDetailPenghargaan.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = detail.size

    override fun onBindViewHolder(holder: AdapterDetailPenghargaan.ViewHolder, position: Int) {
        holder.bind(detail[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataDetail: Penghargaan) {
            with(itemView) {
                val absen = position + 1
                nama_detail.text = dataDetail.namaPenghargaan.toString()
                tanggal_pel.text = "Tanggal = ${dataDetail.tanggal}"
                no_detail.text = absen.toString()
            }
        }
    }
}