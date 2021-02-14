package com.catatpelanggaran.admin.dashboard.pelanggaran

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Guru
import com.catatpelanggaran.admin.model.Pelanggaran
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_pelanggaran.*
import java.lang.Exception

class AddPelanggaranActivity : AppCompatActivity() {

    companion object {
        const val DATA_PELANGGARAN = "DATAPELANGGARAN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pelanggaran)

        val dataPelanggaran = intent.getParcelableExtra<Pelanggaran>(DATA_PELANGGARAN)

        if (dataPelanggaran != null) {
            setText(dataPelanggaran)
            setStatus(true)
        } else {
            setStatus(false)
        }

        button_simpan.setOnClickListener {
            if (dataPelanggaran != null) {
                editData(dataPelanggaran)
            } else {
                insertData()
            }
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        delete_button.setOnClickListener {
            deleteData(dataPelanggaran)
        }

    }

    private fun setText(dataPelanggaran: Pelanggaran?) {
        dataPelanggaran?.let {
            input_pelanggaran.setText(dataPelanggaran.namaPelanggaran)
            input_poin.setText(dataPelanggaran.poinPelanggaran.toString())
            input_hukuman.setText(dataPelanggaran.hukuman)
        }
    }

    private fun setStatus(status: Boolean) {
        if (status) {
            delete_button.visibility = View.VISIBLE
            button_simpan.text = "Edit"
            title_pelanggaran.text = "Edit Pelanggaran"
        } else {
            button_simpan.text = "Simpan"
            title_pelanggaran.text = "Tambah Pelanggaran"
        }
    }

    private fun deleteData(dataPelanggaran: Pelanggaran?) {
        val database = FirebaseDatabase.getInstance().reference
        val builderdelete = AlertDialog.Builder(this@AddPelanggaranActivity)
        builderdelete.setTitle("Warning!")
        builderdelete.setMessage("Are you sure want to delete ${dataPelanggaran?.namaPelanggaran} ?")
        builderdelete.setPositiveButton("Delete") { i, _ ->
            database.child("jenis_pelanggaran")
                .child(dataPelanggaran?.idPelanggaran!!)
                .removeValue()
                .addOnCompleteListener {
                    Toast.makeText(
                        this@AddPelanggaranActivity,
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
        val jenis = input_pelanggaran.text.toString()
        val poin = input_poin.text.toString()
        val hukuman = input_hukuman.text.toString()

        if (jenis.isEmpty() || poin.isEmpty() || hukuman.isEmpty()) {
            if (jenis.isEmpty()) {
                input_pelanggaran.error = "Tolong isi"
            }
            if (poin.isEmpty()) {
                input_poin.error = "Tolong isi"
            }
            if (hukuman.isEmpty()) {
                input_hukuman.error = "Tolong isi"
            }
        } else {
            val database = FirebaseDatabase.getInstance().reference
            val pelanggaranId = database.push().key
            val data = Pelanggaran(pelanggaranId, jenis, poin.toInt(), hukuman)
            try {
                database.child("jenis_pelanggaran").child(pelanggaranId.toString())
                    .setValue(data).addOnCompleteListener {
                        Toast.makeText(
                            this@AddPelanggaranActivity,
                            "berhasil",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
            } catch (e: Exception) {
                Toast.makeText(
                    this@AddPelanggaranActivity,
                    "check your internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun editData(dataPelanggaran: Pelanggaran) {
        val jenis = input_pelanggaran.text.toString()
        val poin = input_poin.text.toString()
        val hukuman = input_hukuman.text.toString()

        if (jenis.isEmpty() || poin.isEmpty() || hukuman.isEmpty()) {
            if (jenis.isEmpty()) {
                input_pelanggaran.error = "Tolong isi"
            }
            if (poin.isEmpty()) {
                input_poin.error = "Tolong isi"
            }
            if (hukuman.isEmpty()) {
                input_poin.error = "Tolong isi"
            }
        } else {
            val database = FirebaseDatabase.getInstance().reference
            val data = Pelanggaran(dataPelanggaran.idPelanggaran, jenis, poin.toInt(), hukuman)

            try {
                database.child("jenis_pelanggaran").child(dataPelanggaran.idPelanggaran!!)
                    .setValue(data).addOnCompleteListener {
                    Toast.makeText(this, "Berhasil update", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}