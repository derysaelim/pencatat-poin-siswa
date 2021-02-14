package com.catatpelanggaran.admin.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Pelanggaran
import kotlinx.android.synthetic.main.item_siswa.view.*

class AdapterPelanggaran(val pelanggaran: ArrayList<Pelanggaran>) :
    RecyclerView.Adapter<AdapterPelanggaran.ViewHolder>() {

    var onItemClick: ((Pelanggaran) -> Unit)? = null
    var onItemDeleteClick: ((Pelanggaran) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterPelanggaran.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_siswa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterPelanggaran.ViewHolder, position: Int) {
        holder.bind(pelanggaran[position])
    }

    override fun getItemCount(): Int = pelanggaran.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        PopupMenu.OnMenuItemClickListener {
        fun bind(dataPelanggaran: Pelanggaran) {
            with(itemView) {
                val nomor = position + 1

                nama_siswa.text = dataPelanggaran.namaPelanggaran
                nis_siswa.text = "Poin = ${dataPelanggaran.poinPelanggaran}"
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
                onItemClick?.invoke(pelanggaran[position])
            }
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.edit_data -> {
                    onItemClick?.invoke(pelanggaran[adapterPosition])
                    return true
                }
                R.id.delete_data -> {
                    onItemDeleteClick?.invoke(pelanggaran[adapterPosition])
                    return true
                }
            }
            return true
        }
    }
}