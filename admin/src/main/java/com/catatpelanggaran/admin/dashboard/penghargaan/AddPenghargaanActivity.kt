package com.catatpelanggaran.admin.dashboard.penghargaan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Penghargaan
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_pelanggaran.*
import kotlinx.android.synthetic.main.activity_add_pelanggaran.back_button
import kotlinx.android.synthetic.main.activity_add_pelanggaran.button_simpan
import kotlinx.android.synthetic.main.activity_add_pelanggaran.delete_button
import kotlinx.android.synthetic.main.activity_add_penghargaan.*
import java.lang.Exception

class AddPenghargaanActivity : AppCompatActivity() {

    companion object {
        const val DATA_PENGHARGAAN = "DataPenghargaan"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_penghargaan)

        val dataPenghargaan = intent.getParcelableExtra<Penghargaan>(DATA_PENGHARGAAN)

        if (dataPenghargaan != null) {
            setText(dataPenghargaan)
            setStatus(true)
        } else {
            setStatus(false)
        }

        button_simpan.setOnClickListener {
            if (dataPenghargaan != null) {
                editData(dataPenghargaan)
            } else {
                insertData()
            }
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        delete_button.setOnClickListener {
            deleteData(dataPenghargaan)
        }

    }

    private fun deleteData(dataPenghargaan: Penghargaan?) {
        val database = FirebaseDatabase.getInstance().reference
        val builderdelete = AlertDialog.Builder(this@AddPenghargaanActivity)
        builderdelete.setTitle("Warning!")
        builderdelete.setMessage("Are you sure want to delete ${dataPenghargaan?.namaPenghargaan} ?")
        builderdelete.setPositiveButton("Delete") { i, _ ->
            database.child("jenis_penghargaan")
                .child(dataPenghargaan?.id_penghargaan!!)
                .removeValue()
                .addOnCompleteListener {
                    Toast.makeText(
                        this@AddPenghargaanActivity,
                        "Berhasil dihapus",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
        }
        builderdelete.setNegativeButton("Cancel") { i, _ ->
            Toast.makeText(
                applicationContext,
                "Data tidak jadi dihapus",
                Toast.LENGTH_LONG
            ).show()
        }
        val dialogdelete = builderdelete.create()
        dialogdelete.show()
    }

    private fun insertData() {
        val jenis = input_penghargaan.text.toString()
        val poin = input_poinpenghargaan.text.toString()
        val database = FirebaseDatabase.getInstance().reference
        val idPenghargaan = database.push().key.toString()
        val data = Penghargaan(idPenghargaan, jenis, poin.toInt())

        if (jenis.isEmpty() || poin.isEmpty()) {
            if (jenis.isEmpty()) {
                input_penghargaan.error = "Tolong isi"
            }
            if (poin.isEmpty()) {
                input_poinpenghargaan.error = "Tolong isi"
            }
        } else {
            try {
                database.child("jenis_penghargaan").child(idPenghargaan).setValue(data)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            } catch (E: Exception) {
                Toast.makeText(this, "Check Your Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun editData(dataPenghargaan: Penghargaan) {
        val jenis = input_penghargaan.text.toString()
        val poin = input_poinpenghargaan.text.toString()
        val database = FirebaseDatabase.getInstance().reference
        val data = Penghargaan(dataPenghargaan.id_penghargaan, jenis, poin.toInt())

        if (jenis.isEmpty() || poin.isEmpty()) {
            if (jenis.isEmpty()) {
                input_penghargaan.error = "Tolong isi"
            }
            if (poin.isEmpty()) {
                input_poinpenghargaan.error = "Tolong isi"
            }
        } else {
            try {
                database.child("jenis_penghargaan").child(dataPenghargaan.id_penghargaan!!)
                    .setValue(data).addOnCompleteListener {
                    Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (E: Exception) {
                Toast.makeText(this, "Check Your Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setStatus(status: Boolean) {
        if (status) {
            delete_button.visibility = View.VISIBLE
            button_simpan.text = "Edit"
            title_penghargaan.text = "Edit Penghargaan"
        } else {
            button_simpan.text = "Simpan"
            title_penghargaan.text = "Tambah Penghargaan"
        }
    }

    private fun setText(dataPenghargaan: Penghargaan?) {
        dataPenghargaan?.let {
            input_penghargaan.setText(dataPenghargaan.namaPenghargaan)
            input_poinpenghargaan.setText(dataPenghargaan.poin.toString())
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}