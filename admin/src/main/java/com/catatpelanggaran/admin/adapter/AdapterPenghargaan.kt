package com.catatpelanggaran.admin.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Penghargaan
import kotlinx.android.synthetic.main.item_siswa.view.*

class AdapterPenghargaan(val penghargaan: ArrayList<Penghargaan>) :
    RecyclerView.Adapter<AdapterPenghargaan.ViewHolder>() {

    var onItemClick: ((Penghargaan) -> Unit)? = null
    var onItemDeleteClick: ((Penghargaan) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterPenghargaan.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_siswa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterPenghargaan.ViewHolder, position: Int) {
        holder.bind(penghargaan[position])
    }

    override fun getItemCount(): Int = penghargaan.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        PopupMenu.OnMenuItemClickListener {
        fun bind(penghargaan: Penghargaan) {
            with(itemView) {
                val nomor = position + 1

                nama_siswa.text = penghargaan.namaPenghargaan
                nis_siswa.text = "Poin = ${penghargaan.poin}"
                no_absen.text = nomor.toString()

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
                onItemClick?.invoke(penghargaan[position])
            }
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.edit_data -> {
                    onItemClick?.invoke(penghargaan[adapterPosition])
                    return true
                }
                R.id.delete_data -> {
                    onItemDeleteClick?.invoke(penghargaan[adapterPosition])
                    return true
                }
            }
            return true
        }
    }

}