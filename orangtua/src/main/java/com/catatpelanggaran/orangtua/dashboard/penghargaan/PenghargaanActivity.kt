package com.catatpelanggaran.orangtua.dashboard.penghargaan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.orangtua.R
import com.catatpelanggaran.orangtua.adapter.AdapterPelanggaran
import com.catatpelanggaran.orangtua.adapter.AdapterPenghargaan
import com.catatpelanggaran.orangtua.dashboard.pelanggaran.PelanggaranActivity
import com.catatpelanggaran.orangtua.model.Pelanggaran
import com.catatpelanggaran.orangtua.model.Penghargaan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_pelanggaran.*
import kotlinx.android.synthetic.main.activity_penghargaan.*
import kotlinx.android.synthetic.main.activity_penghargaan.back_button
import kotlinx.android.synthetic.main.activity_penghargaan.progress_bar

class PenghargaanActivity : AppCompatActivity() {

    companion object {
        const val NIS_SISWA = "NIS_SISWA"
        const val DATA_ACTIVITY = "DATA_ACTIVITY"
    }

    var listPenghargaan: ArrayList<Penghargaan>? = null

    lateinit var nis: String
    lateinit var data: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penghargaan)

        back_button.setOnClickListener { onBackPressed() }

        nis = intent.getStringExtra(NIS_SISWA).toString()
        data = intent.getStringExtra(PelanggaranActivity.DATA_ACTIVITY).toString()

        if (data == "siswa") {
            getDataSiswa(nis)
        } else {
            getData()
        }
    }

    override fun onResume() {
        super.onResume()
        if (data == "siswa") {
            getDataSiswa(nis)
        } else {
            getData()
        }
    }

    private fun getData() {
        progress_bar.visibility = View.VISIBLE
        list_penghargaan.layoutManager = LinearLayoutManager(this)
        list_penghargaan.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        listPenghargaan = arrayListOf()
        database.child("jenis_penghargaan").orderByChild("namaPenghargaan")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        listPenghargaan!!.clear()
                        for (i in snapshot.children) {
                            val penghargaan = i.getValue(Penghargaan::class.java)
                            listPenghargaan!!.add(penghargaan!!)
                        }

                        val adapter = AdapterPenghargaan(listPenghargaan!!)
                        list_penghargaan.adapter = adapter
                        progress_bar.visibility = View.GONE
                        penghargaan_empty.visibility = View.GONE
                        list_penghargaan.visibility = View.VISIBLE

                    } else {
                        list_penghargaan.visibility = View.GONE
                        penghargaan_empty.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@PenghargaanActivity,
                        "Check your internet",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            })
    }

    private fun getDataSiswa(nis: String) {
        progress_bar.visibility = View.VISIBLE
        list_penghargaan.layoutManager = LinearLayoutManager(this)
        list_penghargaan.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        listPenghargaan = arrayListOf()
        database.child("Penghargaan").child(nis).orderByChild("namaPenghargaan")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        listPenghargaan!!.clear()
                        for (i in snapshot.children) {
                            val penghargaan = i.getValue(Penghargaan::class.java)
                            if (penghargaan!!.tanggal != null) {
                                listPenghargaan!!.add(penghargaan)
                            }
                        }

                        val adapter = AdapterPenghargaan(listPenghargaan!!)
                        list_penghargaan.adapter = adapter
                        progress_bar.visibility = View.GONE
                        penghargaan_empty.visibility = View.GONE
                        list_penghargaan.visibility = View.VISIBLE

                    } else {
                        list_penghargaan.visibility = View.GONE
                        penghargaan_empty.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@PenghargaanActivity,
                        "Check your internet",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}