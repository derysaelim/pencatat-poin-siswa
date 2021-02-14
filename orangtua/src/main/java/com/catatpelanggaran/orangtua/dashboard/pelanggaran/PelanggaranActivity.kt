package com.catatpelanggaran.orangtua.dashboard.pelanggaran

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.orangtua.R
import com.catatpelanggaran.orangtua.adapter.AdapterPelanggaran
import com.catatpelanggaran.orangtua.model.Pelanggaran
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_pelanggaran.*

class PelanggaranActivity : AppCompatActivity() {

    var listPelanggaran: ArrayList<Pelanggaran>? = null

    companion object {
        const val NIS_SISWA = "NIS_SISWA"
        const val DATA_ACTIVITY = "DATA_ACTIVITY"
    }

    lateinit var nis: String
    lateinit var data: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelanggaran)
        setSupportActionBar(toolbar_pelanggaran)

        nis = intent.getStringExtra(NIS_SISWA).toString()
        data = intent.getStringExtra(DATA_ACTIVITY).toString()

        back_button.setOnClickListener { onBackPressed() }

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
        list_pelanggaran.layoutManager = LinearLayoutManager(this)
        list_pelanggaran.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        listPelanggaran = arrayListOf()
        database.child("jenis_pelanggaran").orderByChild("namaPelanggaran")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        listPelanggaran!!.clear()
                        for (i in snapshot.children) {
                            val pelanggaran = i.getValue(Pelanggaran::class.java)
                            listPelanggaran!!.add(pelanggaran!!)
                        }

                        val adapter = AdapterPelanggaran(listPelanggaran!!)
                        list_pelanggaran.adapter = adapter
                        progress_bar.visibility = View.GONE
                        pelanggaran_empty.visibility = View.GONE
                        list_pelanggaran.visibility = View.VISIBLE

                    } else {
                        list_pelanggaran.visibility = View.GONE
                        pelanggaran_empty.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@PelanggaranActivity,
                        "Check your internet",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            })
    }

    private fun getDataSiswa(nis: String) {
        progress_bar.visibility = View.VISIBLE
        list_pelanggaran.layoutManager = LinearLayoutManager(this)
        list_pelanggaran.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        listPelanggaran = arrayListOf()
        database.child("Pelanggar").child(nis).orderByChild("namaPelanggaran")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        listPelanggaran!!.clear()
                        for (i in snapshot.children) {
                            val pelanggaran = i.getValue(Pelanggaran::class.java)
                            if (pelanggaran!!.tanggal != null) {
                                listPelanggaran!!.add(pelanggaran)
                            }
                        }

                        val adapter = AdapterPelanggaran(listPelanggaran!!)
                        list_pelanggaran.adapter = adapter
                        progress_bar.visibility = View.GONE
                        pelanggaran_empty.visibility = View.GONE
                        list_pelanggaran.visibility = View.VISIBLE

                    } else {
                        list_pelanggaran.visibility = View.GONE
                        pelanggaran_empty.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@PelanggaranActivity,
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