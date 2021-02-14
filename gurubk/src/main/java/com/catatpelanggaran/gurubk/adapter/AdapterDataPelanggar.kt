package com.catatpelanggaran.gurubk.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.model.Catat
import kotlinx.android.synthetic.main.item_datapel.view.*

class AdapterDataPelanggar(val siswa: ArrayList<Catat>) : RecyclerView.Adapter<AdapterDataPelanggar.ViewHolder>() {

    var onItemClick: ((Catat) -> Unit)? = null
    var onItemDeleteClick: ((Catat) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterDataPelanggar.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_datapel, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = siswa.size


    override fun onBindViewHolder(holder: AdapterDataPelanggar.ViewHolder, position: Int) {
        holder.bind(siswa[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        PopupMenu.OnMenuItemClickListener {
        fun bind(dataCatat: Catat) {
            with(itemView) {
                val absen = position + 1

                nama_siswa.text = dataCatat.nama_siswa
                poin_pel.text = "Poin = ${dataCatat.poin}"
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