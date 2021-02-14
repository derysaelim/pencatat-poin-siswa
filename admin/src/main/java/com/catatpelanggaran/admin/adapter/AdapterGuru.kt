package com.catatpelanggaran.admin.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Guru
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_siswa.view.*

class AdapterGuru(val guru: ArrayList<Guru>) :
    RecyclerView.Adapter<AdapterGuru.ViewHolder>() {

    var onItemClick: ((Guru) -> Unit)? = null
    var onItemDeleteClick: ((Guru) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterGuru.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_siswa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterGuru.ViewHolder, position: Int) {
        holder.bind(guru[position])
    }

    override fun getItemCount(): Int = guru.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        PopupMenu.OnMenuItemClickListener {
        fun bind(guruData: Guru) {
            with(itemView) {

                val absen = position + 1

                nama_siswa.text = guruData.nama
                nis_siswa.text = guruData.nip
                no_absen.text = absen.toString()

                more_button.setOnClickListener {
                    val popupMenu = PopupMenu(context, more_button)
                    popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

                    popupMenu.setOnMenuItemClickListener(this@ViewHolder)
                    popupMenu.show()
                }

            }
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(guru[adapterPosition])
            }
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.edit_data -> {
                    onItemClick?.invoke(guru[adapterPosition])
                    return true
                }
                R.id.delete_data -> {
                    onItemDeleteClick?.invoke(guru[adapterPosition])
                    return true
                }
            }
            return true
        }
    }
}