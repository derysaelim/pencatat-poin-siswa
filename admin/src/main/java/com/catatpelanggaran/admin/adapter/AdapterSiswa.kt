package com.catatpelanggaran.admin.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Siswa
import kotlinx.android.synthetic.main.item_siswa.view.*

class AdapterSiswa(val siswa: ArrayList<Siswa>) : RecyclerView.Adapter<AdapterSiswa.ViewHolder>() {

    var onItemClick: ((Siswa) -> Unit)? = null
    var onItemDeleteClick: ((Siswa) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSiswa.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_siswa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterSiswa.ViewHolder, position: Int) {
        holder.bind(siswa[position])
    }

    override fun getItemCount(): Int = siswa.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        PopupMenu.OnMenuItemClickListener {
        fun bind(dataSiswa: Siswa) {
            with(itemView) {
                val absen = position + 1

                nama_siswa.text = dataSiswa.nama_siswa
                nis_siswa.text = dataSiswa.nis
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
                onItemClick?.invoke(siswa[adapterPosition])
            }
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.edit_data -> {
                    onItemClick?.invoke(siswa[adapterPosition])
                    return true
                }
                R.id.delete_data -> {
                    onItemDeleteClick?.invoke(siswa[adapterPosition])
                    return true
                }
            }
            return true
        }
    }

}