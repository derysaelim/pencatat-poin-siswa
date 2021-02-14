package com.catatpelanggaran.gurubk.dashboard.catat

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.model.Catat
import com.catatpelanggaran.gurubk.model.Pelanggaran
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_catat_pelanggaran.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item_siswa.*
import java.util.*
import kotlin.collections.ArrayList

class CatatPelanggaranActivity : AppCompatActivity() {

    companion object {
        const val DATA_PELANGGAR = "dataPelanggar"
    }

    lateinit var adapterPelanggaran: ArrayAdapter<String>
    lateinit var dataListPelanggaran: ArrayList<String>
    lateinit var dataListHukuman: ArrayList<String>
    lateinit var dataListPoin: ArrayList<String>
    lateinit var dataListId: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catat_pelanggaran)
        setSupportActionBar(toolbar_catat)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        input_tanggal.setOnClickListener {
            val dpd = DatePickerDialog(this, { _, mYear, mMonth, mDay ->
                val mmMonth = mMonth + 1
                input_tanggal.setText("" + mDay + "/" + mmMonth + "/" + mYear)
            }, year, month, day)
            dpd.show()
        }

        dataListHukuman = ArrayList()
        dataListPoin = ArrayList()
        dataListPelanggaran = ArrayList()
        dataListId = ArrayList()
        adapterPelanggaran = ArrayAdapter(
            this@CatatPelanggaranActivity,
            android.R.layout.simple_spinner_dropdown_item,
            dataListPelanggaran
        )
        jenispel.adapter = adapterPelanggaran

        jenispel.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val hukuman = dataListHukuman[jenispel.selectedItemId.toInt()]
                text_hukuman.setText(hukuman)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        var dataCatat = intent.getParcelableExtra<Catat>(DATA_PELANGGAR)
        val database = FirebaseDatabase.getInstance().reference
        database.child("Pelanggar").child(dataCatat!!.nis.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val poin = snapshot.child("dataPelanggar").child("poin").value.toString()
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
        }
        else {
            retrieveData()
        }

        button_simpanpelanggaran.setOnClickListener {
            if (dataCatat?.poin != null) {
                editData(dataCatat!!)
            } else {
                insertData(dataCatat!!)
            }
        }

        back_pelanggaran.setOnClickListener {
            onBackPressed()
        }
    }

    private fun insertData(dataCatat: Catat) {
        val database = FirebaseDatabase.getInstance().reference
        val tanggal = input_tanggal.text.toString()
        val nis = dataCatat.nis.toString()
        val namaSiswa = dataCatat.nama_siswa.toString()
        val idpelanggaran = jenispel.selectedItemId
        val jenispel = jenispel.selectedItem.toString()
        val keterangan = input_keterangan.text.toString()
        val poin = dataListPoin[idpelanggaran.toInt()].toInt()
        val hukuman = dataListHukuman[idpelanggaran.toInt()]
        val id = dataListId[idpelanggaran.toInt()]

        val data = Catat(nis, namaSiswa, poin)
        val dataPelanggar = Pelanggaran(id, jenispel, poin, hukuman, keterangan, tanggal)

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
                input_keterangan.error = "Data tidak boleh kosong!"
            }

        }
        else {
            try {
                database.child("Pelanggar").child(nis).child("dataPelanggar").setValue(data)
                    .addOnCompleteListener {
                        database.child("Pelanggar").child(nis).child(id).setValue(dataPelanggar)
                            .addOnCompleteListener {
                                Toast.makeText(this, "berhasil", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                    }
            } catch (e: Exception) {
                Toast.makeText(
                    this@CatatPelanggaranActivity,
                    "Check your internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun editData(dataCatat: Catat) {
        val database = FirebaseDatabase.getInstance().reference
        val tanggal = input_tanggal.text.toString()
        val nis = dataCatat.nis.toString()
        val namaSiswa = dataCatat.nama_siswa.toString()
        val idpelanggaran = jenispel.selectedItemId
        val jenispel = jenispel.selectedItem.toString()
        val keterangan = input_keterangan.text.toString()
        val poin = dataListPoin[idpelanggaran.toInt()].toInt()
        val hukuman = dataListHukuman[idpelanggaran.toInt()]
        val id = dataListId[idpelanggaran.toInt()]

        val data = Catat(nis, namaSiswa, dataCatat.poin!! + poin)
        val dataPelanggar = Pelanggaran(id, jenispel, poin, hukuman, keterangan, tanggal)

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
                input_keterangan.error = "Data tidak boleh kosong!"
            }
        } else {
            try {
                database.child("Pelanggar").child(nis).child("dataPelanggar").setValue(data)
                    .addOnCompleteListener {
                        database.child("Pelanggar").child(nis).child(id).setValue(dataPelanggar)
                            .addOnCompleteListener {
                                Toast.makeText(this, "berhasil", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                    }
            } catch (e: Exception) {
                Toast.makeText(
                    this@CatatPelanggaranActivity,
                    "Check your internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getData(dataCatat: Catat?) {
        dataCatat?.let {
//            input_tanggal.setText(dataCatat.tanggal)
            detail_nis.setText(dataCatat.nis.toString())
            detail_nama.setText(dataCatat.nama_siswa)
//            detail_pelanggaran.setText(dataCatat.namaPelanggaran)
//            input_keterangan.setText(dataCatat.keterangan)
        }
    }

    private fun setStatus(status: Boolean) {
        if (status) {
            detail_pelanggaran.visibility = View.GONE
        }
    }

    private fun setJenpel(status: Boolean) {
        if (status) {
            message_button.visibility = View.VISIBLE
            jenispel.visibility = View.GONE
        }
    }

    private fun retrieveData() {
        val database = FirebaseDatabase.getInstance().reference
        database.child("jenis_pelanggaran").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    dataListPelanggaran.add(i.child("namaPelanggaran").value.toString())
                    dataListPoin.add(i.child("poinPelanggaran").value.toString())
                    dataListHukuman.add(i.child("hukuman").value.toString())
                    dataListId.add(i.child("idPelanggaran").value.toString())
                }
                adapterPelanggaran.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CatatPelanggaranActivity, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}