package com.catatpelanggaran.admin.dashboard.siswa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Siswa
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_kelas.*
import kotlinx.android.synthetic.main.activity_add_siswa.*
import kotlinx.android.synthetic.main.activity_add_siswa.back_button
import kotlinx.android.synthetic.main.item_siswa.*

class AddSiswaActivity : AppCompatActivity() {

    companion object {
        const val DATA_SISWA = "dataSiswa"
    }

    lateinit var adapterKelas: ArrayAdapter<String>
    lateinit var dataListKelas: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_siswa)

        dataListKelas = ArrayList()
        adapterKelas = ArrayAdapter(
            this@AddSiswaActivity,
            android.R.layout.simple_spinner_dropdown_item,
            dataListKelas
        )
        kelas.adapter = adapterKelas

        val dataSiswa = intent.getParcelableExtra<Siswa>(DATA_SISWA)
        if (dataSiswa != null) {
            setText(dataSiswa)
            setStatus(true)
            retrieveData(true, dataSiswa)
        } else {
            setStatus(false)
            retrieveData(false, null)
        }

        button_simpansiswa.setOnClickListener {
            if (dataSiswa != null) {
                editData(dataSiswa)
            } else {
                insertData()
            }
        }

        back_button.setOnClickListener { finish() }

        deletesiswa_button.setOnClickListener {
            deleteData(dataSiswa)
        }

        editsiswa_button.setOnClickListener {
            edit_kelas.visibility = View.GONE
            kelas.visibility = View.VISIBLE
            button_simpansiswa.visibility = View.VISIBLE
            editsiswa_button.visibility = View.GONE
            retrieveData(false, null)

            val test = dataSiswa!!.id_kelas.toString()
            val a = dataListKelas.indexOf(test)

            kelas.setSelection(a)
        }

    }

    private fun deleteData(dataSiswa: Siswa?) {
        val database = FirebaseDatabase.getInstance().reference
        val builderdelete = AlertDialog.Builder(this@AddSiswaActivity)
        builderdelete.setTitle("Warning!")
        builderdelete.setMessage("Are you sure want to delete ${dataSiswa!!.nama_siswa} ?")
        builderdelete.setPositiveButton("Delete") { _, _ ->
            database.child("Siswa").child(dataSiswa.nis!!).removeValue()
                .addOnCompleteListener {
                    database.child("daftar_kelas").child(dataSiswa.id_kelas!!)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val jumlahmurid = snapshot.childrenCount.toInt()

                                if (jumlahmurid > 1) {
                                    database.child("daftar_kelas").child(dataSiswa.id_kelas)
                                        .child(dataSiswa.nis).removeValue()
                                } else {
                                    database.child("daftar_kelas")
                                        .child(dataSiswa.id_kelas)
                                        .removeValue()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
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
        val database = FirebaseDatabase.getInstance().reference

        val nis = input_nis.text.toString()
        val namaSiswa = input_nama.text.toString()
        val kelas = kelas.selectedItem.toString().replace("\\s".toRegex(), "")
        val alamat = input_alamat.text.toString()
        var jenkel = ""

        if (radio_laki.isChecked) {
            jenkel = "L"
        } else if (radio_perempuan.isChecked) {
            jenkel = "P"
        }

        if (nis.isEmpty() || namaSiswa.isEmpty() || alamat.isEmpty()) {

        } else {
            database.child("Siswa").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(nis).exists()) {
                        Toast.makeText(this@AddSiswaActivity, "Sudah ada", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val data = Siswa(nis, namaSiswa, kelas, jenkel, alamat)
                        val dataKelas = Siswa(nis, namaSiswa)
                        database.child("Siswa").child(nis).setValue(data).addOnCompleteListener {
                            database.child("daftar_kelas")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.child(kelas).exists()) {
                                            database.child("daftar_kelas").child(kelas).child(nis)
                                                .setValue(dataKelas)
                                            Toast.makeText(
                                                this@AddSiswaActivity,
                                                "Berhasil",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            database.child("daftar_kelas").child(kelas).child(nis)
                                                .setValue(dataKelas)
                                                .addOnCompleteListener {
                                                    Toast.makeText(
                                                        this@AddSiswaActivity,
                                                        "Berhasil",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(
                                            this@AddSiswaActivity,
                                            "Error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                            finish()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@AddSiswaActivity,
                        "Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

    private fun editData(dataSiswa: Siswa) {
        val database = FirebaseDatabase.getInstance().reference

        val nis = dataSiswa.nis.toString()
        val namaSiswa = input_nama.text.toString()
        val kelas = kelas.selectedItem.toString().replace("\\s".toRegex(), "")
        val alamat = input_alamat.text.toString()
        var jenkel = ""

        if (radio_laki.isChecked) {
            jenkel = "L"
        } else if (radio_perempuan.isChecked) {
            jenkel = "P"
        }
        val data = Siswa(nis, namaSiswa, kelas, jenkel, alamat)
        val dataKelas = Siswa(nis, namaSiswa)

        if (nis.isEmpty() || namaSiswa.isEmpty() || alamat.isEmpty()) {

        } else {

            if (kelas != dataSiswa.id_kelas) {
                database.child("daftar_kelas").child(dataSiswa.id_kelas!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val jumlahmurid = snapshot.childrenCount.toInt()

                            if (jumlahmurid > 1) {
                                database.child("daftar_kelas").child(dataSiswa.id_kelas)
                                    .child(dataSiswa.nis!!).removeValue().addOnCompleteListener {
                                        database.child("daftar_kelas").child(kelas).child(nis)
                                            .setValue(dataKelas)
                                    }
                            } else {
                                database.child("daftar_kelas")
                                    .child(dataSiswa.id_kelas)
                                    .removeValue()
                                    .addOnCompleteListener {
                                        database.child("daftar_kelas").child(kelas).child(nis)
                                            .setValue(dataKelas)
                                    }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
            } else {
                database.child("Siswa").child(nis).setValue(data).addOnCompleteListener {
                    Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun setStatus(status: Boolean) {
        if (status) {
            editsiswa_button.visibility = View.VISIBLE
            deletesiswa_button.visibility = View.VISIBLE
            edit_kelas.visibility = View.VISIBLE
            kelas.visibility = View.GONE
            input_nis.isEnabled = false
            button_simpansiswa.text = "Edit"
            title_siswa.text = "Edit Siswa"
        }
    }

    private fun setText(dataSiswa: Siswa?) {
        dataSiswa?.let {

            button_simpansiswa.visibility = View.GONE
            kelas.visibility = View.GONE

            input_nis.setText(dataSiswa.nis)
            input_nama.setText(dataSiswa.nama_siswa)
            input_alamat.setText(dataSiswa.alamat)
            val jenkel = dataSiswa.jenkel.toString()

            if (jenkel == "L") {
                radio_laki.isChecked = true
            } else {
                radio_perempuan.isChecked = true
            }
        }
    }

    private fun retrieveData(status: Boolean, dataSiswa: Siswa?) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("kelas").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (status) {
                    edit_kelas.setText(
                        snapshot.child(dataSiswa?.id_kelas!!)
                            .child("tingkat").value.toString() + " " + snapshot.child(dataSiswa?.id_kelas!!)
                            .child("jurusan").value.toString() + " " + snapshot.child(dataSiswa?.id_kelas!!)
                            .child("kelas").value.toString()
                    )
                } else {
                    for (i in snapshot.children) {
                        dataListKelas.add(
                            i.child("tingkat").value.toString() + " " + i.child("jurusan").value.toString() + " " + i.child(
                                "kelas"
                            ).value.toString()
                        )
                    }
                    adapterKelas.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddSiswaActivity, "Error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}