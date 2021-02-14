package com.catatpelanggaran.gurubk.dashboard.catat

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.model.Catat
import com.catatpelanggaran.gurubk.model.Penghargaan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_catat_pelanggaran.*
import kotlinx.android.synthetic.main.activity_catat_pelanggaran.input_tanggal
import kotlinx.android.synthetic.main.activity_catat_penghargaan.*
import java.util.*
import kotlin.collections.ArrayList

class CatatPenghargaanActivity : AppCompatActivity() {

    companion object {
        const val DATA_PENGHARGAAN = "dataPenghargaan"
    }

    lateinit var adapterPenghargaan: ArrayAdapter<String>
    lateinit var dataListPenghargaan: ArrayList<String>
    lateinit var dataListPoin: ArrayList<String>
    lateinit var dataListId: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catat_penghargaan)
        setSupportActionBar(toolbar_penghargaan)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        tanggal_penghargaan.setOnClickListener {
            val dpd = DatePickerDialog(this, { _, mYear, mMonth, mDay ->
                val mmMonth = mMonth + 1
                tanggal_penghargaan.setText("" + mDay + "/" + mmMonth + "/" + mYear)
            }, year, month, day)
            dpd.show()
        }

        dataListPoin = ArrayList()
        dataListPenghargaan = ArrayList()
        dataListId = ArrayList()
        adapterPenghargaan = ArrayAdapter(
            this@CatatPenghargaanActivity,
            android.R.layout.simple_spinner_dropdown_item,
            dataListPenghargaan
        )
        jenispenghargaan.adapter = adapterPenghargaan

        var dataCatat = intent.getParcelableExtra<Catat>(DATA_PENGHARGAAN)
        val database = FirebaseDatabase.getInstance().reference
        database.child("Penghargaan").child(dataCatat!!.nis.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val poin = snapshot.child("dataPenghargaan").child("poin").value.toString()
                        dataCatat = Catat(dataCatat!!.nis, dataCatat!!.nama_siswa, poin.toInt())
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        if (dataCatat != null) {
            if (dataCatat!!.poin != null) {
                setStatus(false)
                setJenpel(true)
                getData(dataCatat)
            } else {
                setStatus(true)
                setJenpel(false)
                getData(dataCatat)
                retrieveData()
            }
        } else {
            retrieveData()
        }

        button_simpanpenghargaan.setOnClickListener {
            if (dataCatat?.poin != null) {
                editData(dataCatat!!)
            } else {
                insertData(dataCatat!!)
            }
        }

        back_penghargaan.setOnClickListener {
            onBackPressed()
        }
    }

    private fun insertData(dataCatat: Catat) {
        val database = FirebaseDatabase.getInstance().reference
        val tanggal = tanggal_penghargaan.text.toString()
        val nis = dataCatat.nis.toString()
        val namaSiswa = dataCatat.nama_siswa.toString()
        val idpenghargaan = jenispenghargaan.selectedItemId
        val jenispenghargaan = jenispenghargaan.selectedItem.toString()
        val keterangan = keterangan_penghargaan.text.toString()
        val poin = dataListPoin[idpenghargaan.toInt()].toInt()
        val id = dataListId[idpenghargaan.toInt()]

        val data = Catat(nis, namaSiswa, poin)
        val dataPenghargaan = Penghargaan(id, jenispenghargaan, poin, keterangan, tanggal)

        if (tanggal.isEmpty() || nis.isEmpty() || namaSiswa.isEmpty() || keterangan.isEmpty()) {
            if (tanggal.isEmpty()) {
                input_tanggal.error = "Data tidak boleh kosong!"
            }
            if (nis.isEmpty()) {
                detail_nis.error = "Data tidak boleh kosong!"
            }
            if (namaSiswa.isEmpty()) {
                detail_nama.error = "Data tidak boleh kosong!"
            }
            if (keterangan.isEmpty()) {
                keterangan_penghargaan.error = "Data tidak boleh kosong!"
            }
        } else {
            try {
                database.child("Penghargaan").child(nis).child("dataPenghargaan").setValue(data)
                    .addOnCompleteListener {
                        database.child("Penghargaan").child(nis).child(id).setValue(dataPenghargaan)
                            .addOnCompleteListener {
                                Toast.makeText(this, "berhasil", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                    }
            } catch (e: Exception) {
                Toast.makeText(
                    this@CatatPenghargaanActivity,
                    "Check your internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun editData(dataCatat: Catat) {
        val database = FirebaseDatabase.getInstance().reference
        val tanggal = tanggal_penghargaan.text.toString()
        val nis = dataCatat.nis.toString()
        val namaSiswa = dataCatat.nama_siswa.toString()
        val idpenghargaan = jenispenghargaan.selectedItemId
        val jenispenghargaan = jenispenghargaan.selectedItem.toString()
        val keterangan = keterangan_penghargaan.text.toString()
        val poin = dataListPoin[idpenghargaan.toInt()].toInt()
        val id = dataListId[idpenghargaan.toInt()]

        val data = Catat(nis, namaSiswa, dataCatat.poin!! + poin)
        val dataPenghargaan = Penghargaan(id, jenispenghargaan, poin, keterangan, tanggal)

        if (tanggal.isEmpty() || nis.isEmpty() || namaSiswa.isEmpty() || keterangan.isEmpty()) {
            if (tanggal.isEmpty()) {
                input_tanggal.error = "Data tidak boleh kosong!"
            }
            if (nis.isEmpty()) {
                detail_nis.error = "Data tidak boleh kosong!"
            }
            if (namaSiswa.isEmpty()) {
                detail_nama.error = "Data tidak boleh kosong!"
            }
            if (keterangan.isEmpty()) {
                keterangan_penghargaan.error = "Data tidak boleh kosong!"
            }
        } else {
            try {
                database.child("Penghargaan").child(nis).child("dataPenghargaan").setValue(data)
                    .addOnCompleteListener {
                        database.child("Penghargaan").child(nis).child(id).setValue(dataPenghargaan)
                            .addOnCompleteListener {
                                Toast.makeText(this, "berhasil", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                    }
            } catch (e: Exception) {
                Toast.makeText(
                    this@CatatPenghargaanActivity,
                    "Check your internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getData(dataCatat: Catat?) {
        dataCatat?.let {
//            input_tanggal.setText(dataCatat.tanggal)
            nis_penghargaan.setText(dataCatat.nis.toString())
            nama_penghargaan.setText(dataCatat.nama_siswa)
//            detail_pelanggaran.setText(dataCatat.namaPelanggaran)
//            input_keterangan.setText(dataCatat.keterangan)
        }
    }

    private fun setStatus(status: Boolean) {
        if (status) {
            detail_penghargaan.visibility = View.GONE
        }
    }

    private fun setJenpel(status: Boolean) {
        if (status) {
            message_penghargaan.visibility = View.VISIBLE
            jenispenghargaan.visibility = View.GONE
        }
    }

    private fun retrieveData() {
        val database = FirebaseDatabase.getInstance().reference
        database.child("jenis_penghargaan").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    dataListPenghargaan.add(i.child("namaPenghargaan").value.toString())
                    dataListPoin.add(i.child("poin").value.toString())
                    dataListId.add(i.child("id_penghargaan").value.toString())
                }
                adapterPenghargaan.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CatatPenghargaanActivity, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}