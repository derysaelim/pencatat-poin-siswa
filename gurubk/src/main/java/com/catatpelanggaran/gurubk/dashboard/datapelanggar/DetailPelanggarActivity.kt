package com.catatpelanggaran.gurubk.dashboard.datapelanggar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.adapter.AdapterDetail
import com.catatpelanggaran.gurubk.adapter.AdapterDetailPenghargaan
import com.catatpelanggaran.gurubk.model.Pelanggaran
import com.catatpelanggaran.gurubk.model.Penghargaan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_detail_pelanggar.*

class DetailPelanggarActivity : AppCompatActivity() {

    companion object {
        const val DATA_NIS = "DATA_NIS"
        const val DATA_SISWA = "DATA_SISWA"
    }

    var listPelanggaran: ArrayList<Pelanggaran>? = null
    var listPenghargaan: ArrayList<Penghargaan>? = null
    lateinit var nis: String
    lateinit var data: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pelanggar)
        setSupportActionBar(toolbar_detail)

        nis = intent.getStringExtra(DATA_NIS).toString()
        data = intent.getStringExtra(DATA_SISWA).toString()

        back_detail.setOnClickListener { finish() }

        if (data == "langgar") {
            text_detail.text = "Data Pelanggaran"
            getDataPelanggaran(nis)
        } else {
            text_detail.text = "Data Penghargaan"
            getDataPenghargaan(nis)
        }

    }

    private fun getDataPenghargaan(nis: String) {
        progress_bar.visibility = View.VISIBLE
        list_detail.layoutManager = LinearLayoutManager(this)
        list_detail.hasFixedSize()
        listPenghargaan = arrayListOf()
        val database = FirebaseDatabase.getInstance().reference
        database.child("DataPenghargaan").child(nis)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        listPenghargaan!!.clear()
                        for (i in snapshot.children) {
                            val penghargaan = i.getValue(Penghargaan::class.java)
                            listPenghargaan!!.add(penghargaan!!)
                        }
                        val adapter = AdapterDetailPenghargaan(listPenghargaan!!)
                        list_detail.adapter = adapter
                        progress_bar.visibility = View.GONE
                        detail_empty.visibility = View.GONE
                        list_detail.visibility = View.VISIBLE
                    } else {
                        progress_bar.visibility = View.GONE
                        detail_empty.visibility = View.VISIBLE
                        list_detail.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    private fun getDataPelanggaran(nis: String) {
        progress_bar.visibility = View.VISIBLE
        list_detail.layoutManager = LinearLayoutManager(this)
        list_detail.hasFixedSize()
        listPelanggaran = arrayListOf()
        val database = FirebaseDatabase.getInstance().reference
        database.child("DataPelanggar").child(nis)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        listPelanggaran!!.clear()
                        for (i in snapshot.children) {
                            val pelanggaran = i.getValue(Pelanggaran::class.java)
                            listPelanggaran!!.add(pelanggaran!!)
                            Log.e("DATA", pelanggaran.toString())
                        }
                        val adapter = AdapterDetail(listPelanggaran!!)
                        list_detail.adapter = adapter
                        progress_bar.visibility = View.GONE
                        detail_empty.visibility = View.GONE
                        list_detail.visibility = View.VISIBLE
                    } else {
                        progress_bar.visibility = View.GONE
                        detail_empty.visibility = View.VISIBLE
                        list_detail.visibility = View.GONE
                    }

                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}