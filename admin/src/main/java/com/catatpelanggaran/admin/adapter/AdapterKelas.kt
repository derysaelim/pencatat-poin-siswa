package com.catatpelanggaran.admin.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Guru
import com.catatpelanggaran.admin.model.Kelas
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_siswa.view.*

class AdapterKelas(val kelas: ArrayList<Kelas>) : RecyclerView.Adapter<AdapterKelas.ViewHolder>() {

    var onItemClick: ((Kelas) -> Unit)? = null
    var onItemDeleteClick: ((Kelas) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterKelas.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_siswa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterKelas.ViewHolder, position: Int) {
        holder.bind(kelas[position])
    }

    override fun getItemCount(): Int = kelas.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        PopupMenu.OnMenuItemClickListener {
        fun bind(kelasData: Kelas) {
            with(itemView) {
                val absen = position + 1
                val kelas = "${kelasData.tingkat} ${kelasData.jurusan} ${kelasData.kelas}"

                no_absen.text = absen.toString()
                nama_siswa.text = kelas
                val database = FirebaseDatabase.getInstance().reference
                database.child("Guru").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        nis_siswa.text =
                            snapshot.child(kelasData.nip!!).child("nama").value.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        nis_siswa.text = "---"
                    }

                })

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
                onItemClick?.invoke(kelas[adapterPosition])
            }
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.edit_data -> {
                    onItemClick?.invoke(kelas[adapterPosition])
                    return true
                }
                R.id.delete_data -> {
                    onItemDeleteClick?.invoke(kelas[adapterPosition])
                    return true
                }
            }
            return true
        }
    }
}