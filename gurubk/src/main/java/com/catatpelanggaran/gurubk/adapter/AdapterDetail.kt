package com.catatpelanggaran.gurubk.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.model.Pelanggaran
import kotlinx.android.synthetic.main.item_detail.view.*

class AdapterDetail(val detail: ArrayList<Pelanggaran>) :
    RecyclerView.Adapter<AdapterDetail.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterDetail.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = detail.size

    override fun onBindViewHolder(holder: AdapterDetail.ViewHolder, position: Int) {
        holder.bind(detail[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataDetail: Pelanggaran) {
            with(itemView) {
                val absen = position + 1
                Log.e("data", dataDetail.namaPelanggaran.toString())
                nama_detail.text = dataDetail.namaPelanggaran.toString()
                tanggal_pel.text = "Tanggal = ${dataDetail.tanggal}"
                no_detail.text = absen.toString()

            }
        }
    }
}