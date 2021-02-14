package com.catatpelanggaran.gurubk.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.model.Guru
import com.catatpelanggaran.gurubk.model.Penghargaan
import kotlinx.android.synthetic.main.item_pelanggaran.view.*
import kotlinx.android.synthetic.main.item_siswa.view.*

class AdapterPenghargaan(val penghargaan: ArrayList<Penghargaan>) :
    RecyclerView.Adapter<AdapterPenghargaan.ViewHolder>() {

    var onItemClick: ((Guru) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterPenghargaan.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pelanggaran, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterPenghargaan.ViewHolder, position: Int) {
        holder.bind(penghargaan[position])
    }

    override fun getItemCount(): Int = penghargaan.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(penghargaan: Penghargaan) {
            with(itemView) {

                val absen = position + 1

                nama_pel.text = penghargaan.namaPenghargaan
                poin_pel.text = "Poin = ${penghargaan.poin}"
                no_absenpel.text = absen.toString()

            }
        }


    }
}