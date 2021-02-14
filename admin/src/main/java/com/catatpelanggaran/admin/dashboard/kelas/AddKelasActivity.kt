package com.catatpelanggaran.admin.dashboard.kelas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Kelas
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_guru.back_button
import kotlinx.android.synthetic.main.activity_add_kelas.*
import kotlinx.android.synthetic.main.activity_add_kelas.input_jurusan
import kotlinx.android.synthetic.main.activity_add_kelas.input_tingkat
import kotlinx.android.synthetic.main.activity_add_kelas.kelas_B
import kotlinx.android.synthetic.main.activity_add_kelas.kelas_a
import kotlinx.android.synthetic.main.activity_add_kelas.kelas_c
import kotlinx.android.synthetic.main.activity_add_kelas.kelas_d
import kotlinx.android.synthetic.main.activity_detail_siswa.*

class AddKelasActivity : AppCompatActivity() {

    companion object {
        const val DATA_KELAS = "DATAKELAS"
    }

    lateinit var adapter: ArrayAdapter<String>
    lateinit var spinnerDataList: ArrayList<String>
    lateinit var dataList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_kelas)
        dataList = ArrayList()
        spinnerDataList = ArrayList()
        adapter = ArrayAdapter(
            this@AddKelasActivity,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerDataList
        )
        walikelas.adapter = adapter
        retrieveData()

        val dataKelas = intent.getParcelableExtra<Kelas>(DATA_KELAS)
        if (dataKelas != null) {
            setText(dataKelas)
            setStatus(true)
        } else {
            setStatus(false)
        }

        button_simpankelas.setOnClickListener {
            if (dataKelas != null) {
                editData(dataKelas)
            } else {
                insertData()
            }
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        deletekelas_button.setOnClickListener {
            deleteData(dataKelas)
        }

        edit_button.setOnClickListener {
            edit_walikelas.visibility = View.GONE
            walikelas.visibility = View.VISIBLE
            button_simpankelas.visibility = View.VISIBLE
            edit_button.visibility = View.GONE

            val test = dataKelas!!.nip.toString()
            val a = dataList.indexOf(test)

            walikelas.setSelection(a)
        }
    }

    private fun deleteData(dataKelas: Kelas?) {
        val database = FirebaseDatabase.getInstance().reference
        val builderdelete = AlertDialog.Builder(this@AddKelasActivity)
        database.child("daftar_kelas")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(dataKelas!!.idKelas!!).exists()) {
                        builderdelete.setTitle("Warning!")
                        builderdelete.setMessage("Maaf kelas ini ada muridnya")
                        builderdelete.setPositiveButton("OK") { i, _ -> }
                    } else {
                        builderdelete.setTitle("Warning!")
                        builderdelete.setMessage("Are you sure want to delete ${dataKelas.tingkat} ${dataKelas.jurusan} ${dataKelas.kelas} ?")
                        builderdelete.setPositiveButton("Delete") { i, _ ->
                            database.child("kelas")
                                .child(dataKelas.idKelas!!).removeValue()
                                .addOnCompleteListener {
                                    database.child("walikelas")
                                        .child(dataKelas.nip!!)
                                        .removeValue()
                                        .addOnCompleteListener {
                                            Toast.makeText(
                                                this@AddKelasActivity,
                                                "Berhasil dihapus",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            finish()
                                        }
                                }
                        }
                        builderdelete.setNegativeButton("Cancel") { i, _ ->
                            Toast.makeText(
                                applicationContext,
                                "Data tidak jadi dihapus",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    val dialogdelete = builderdelete.create()
                    dialogdelete.show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@AddKelasActivity,
                        "Somethings wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun insertData() {
        val tingkat = input_tingkat.selectedItem.toString()
        val jurusan = input_jurusan.text.toString().trim()
        val walikelas = walikelas.selectedItemId
        var kelas = ""
        if (kelas_a.isChecked) {
            kelas = "A"
        }
        if (kelas_B.isChecked) {
            kelas = "B"
        }
        if (kelas_c.isChecked) {
            kelas = "C"
        }
        if (kelas_d.isChecked) {
            kelas = "D"
        }
        val idKelas = tingkat + jurusan + kelas

        if (jurusan.isEmpty() || kelas.isEmpty()) {
            if (jurusan.isEmpty()) {
                input_jurusan.error = "Isi"
            }
            if (kelas.isEmpty()) {
                no_kelas.visibility = View.VISIBLE
            }
        }

        val nip = dataList[walikelas.toInt()]

        val database = FirebaseDatabase.getInstance().reference
        database.child("kelas").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(idKelas).exists()) {
                    Toast.makeText(this@AddKelasActivity, "Sudah ada", Toast.LENGTH_SHORT).show()
                } else {
                    database.child("walikelas")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.child(nip).exists()) {
                                    Toast.makeText(
                                        this@AddKelasActivity,
                                        "Sudah ada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val data = Kelas(idKelas, tingkat, jurusan, kelas, nip)
                                    database.child("walikelas").child(nip).setValue(true)
                                        .addOnCompleteListener {
                                            database.child("kelas").child(idKelas).setValue(data)
                                                .addOnCompleteListener {
                                                    Toast.makeText(
                                                        this@AddKelasActivity,
                                                        "Berhasil",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    finish()
                                                }
                                        }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    this@AddKelasActivity,
                                    "something wrong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddKelasActivity, "something wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editData(dataKelas: Kelas) {
        val tingkat = input_tingkat.selectedItem.toString()
        val jurusan = input_jurusan.text.toString().trim()
        val walikelas = walikelas.selectedItemId
        var kelas = ""
        if (kelas_a.isChecked) {
            kelas = "A"
        }
        if (kelas_B.isChecked) {
            kelas = "B"
        }
        if (kelas_c.isChecked) {
            kelas = "C"
        }
        if (kelas_d.isChecked) {
            kelas = "D"
        }
        val idKelas = tingkat + jurusan + kelas

        if (jurusan.isEmpty() || kelas.isEmpty()) {
            if (jurusan.isEmpty()) {
                input_jurusan.error = "Isi"
            }
            if (kelas.isEmpty()) {
                no_kelas.visibility = View.VISIBLE
            }
        }

        val nip = dataList[walikelas.toInt()]

        val database = FirebaseDatabase.getInstance().reference

        val data = Kelas(idKelas, tingkat, jurusan, kelas, nip)

        if (dataKelas.idKelas == idKelas && dataKelas.nip == nip) {
            Toast.makeText(this@AddKelasActivity, "tidak ada yang dirubah", Toast.LENGTH_SHORT)
                .show()
        } else if (dataKelas.idKelas == idKelas) {
            database.child("walikelas").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(nip).exists()) {
                        Toast.makeText(this@AddKelasActivity, "Tidak bisa", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        database.child("walikelas").child(dataKelas.nip!!).removeValue()
                            .addOnCompleteListener {
                                database.child("kelas").child(dataKelas.idKelas).setValue(data)
                                    .addOnCompleteListener {
                                        database.child("walikelas").child(nip).setValue(true)
                                            .addOnCompleteListener {
                                                Toast.makeText(
                                                    this@AddKelasActivity,
                                                    "Berhasil",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                finish()
                                            }
                                    }
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AddKelasActivity, "something wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        } else if (dataKelas.nip == nip) {
            database.child("daftar_kelas").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(dataKelas.idKelas!!).exists()) {
                        Toast.makeText(
                            this@AddKelasActivity,
                            "Sudah ada muridnya",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        database.child("kelas").child(dataKelas.idKelas).removeValue()
                            .addOnCompleteListener {
                                database.child("kelas").child(idKelas).setValue(data)
                                    .addOnCompleteListener {
                                        Toast.makeText(
                                            this@AddKelasActivity,
                                            "Berhasil",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    }
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        } else {
            insertData()
        }

    }

    private fun setStatus(status: Boolean) {
        if (status) {
            deletekelas_button.visibility = View.VISIBLE
            edit_button.visibility = View.VISIBLE
            button_simpankelas.text = "Edit"
            title_kelas.text = "Edit Kelas"
        }
    }

    private fun setText(dataKelas: Kelas?) {
        dataKelas?.let {
            edit_walikelas.visibility = View.VISIBLE
            walikelas.visibility = View.GONE
            button_simpankelas.visibility = View.GONE

            when (dataKelas.tingkat) {
                "X" -> {
                    input_tingkat.setSelection(0)
                }
                "XI" -> {
                    input_tingkat.setSelection(1)
                }
                "XII" -> {
                    input_tingkat.setSelection(2)
                }
                else -> {
                    input_tingkat.setSelection(3)
                }
            }
            input_jurusan.setText(dataKelas.jurusan)
            when (dataKelas.kelas) {
                "A" -> {
                    kelas_a.isChecked = true
                }
                "B" -> {
                    kelas_B.isChecked = true
                }
                "C" -> {
                    kelas_c.isChecked = true
                }
                else -> {
                    kelas_d.isChecked = true
                }
            }

            val database = FirebaseDatabase.getInstance().reference
            database.child("Guru").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(dataKelas.nip!!).exists()) {
                        edit_walikelas.setText(
                            snapshot.child(dataKelas.nip).child("nama").value.toString()
                        )
                    } else {
                        Toast.makeText(this@AddKelasActivity, "Tidak ada", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AddKelasActivity, "Something wrong", Toast.LENGTH_SHORT)
                        .show()
                }

            })

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun retrieveData() {
        val database = FirebaseDatabase.getInstance().reference
        database.child("Guru").orderByChild("nama").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    spinnerDataList.add(i.child("nama").value.toString())
                    dataList.add(i.child("nip").value.toString())
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddKelasActivity, "something wrong", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

}