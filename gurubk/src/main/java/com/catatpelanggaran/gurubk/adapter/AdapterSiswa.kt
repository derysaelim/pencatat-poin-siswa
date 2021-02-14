package com.catatpelanggaran.gurubk.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.model.Catat
import com.catatpelanggaran.gurubk.model.Siswa
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_siswa.view.*

class AdapterSiswa(val siswa: ArrayList<Catat>) : RecyclerView.Adapter<AdapterSiswa.ViewHolder>() {

    var onItemClick: ((Catat) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSiswa.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_siswa, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = siswa.size

    override fun onBindViewHolder(holder: AdapterSiswa.ViewHolder, position: Int) {
        holder.bind(siswa[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataCatat: Catat){
            with(itemView) {
                val absen = position + 1
                nama_siswa.text = dataCatat.nama_siswa
                nis_siswa.text = dataCatat.nis
                no_absen.text = absen.toString()
            }
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(siswa[adapterPosition])
            }
        }
    }



}