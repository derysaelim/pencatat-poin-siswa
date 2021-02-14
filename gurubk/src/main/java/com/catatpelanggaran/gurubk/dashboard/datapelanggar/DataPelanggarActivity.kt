package com.catatpelanggaran.gurubk.dashboard.datapelanggar

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.adapter.AdapterDataPelanggar
import com.catatpelanggaran.gurubk.adapter.AdapterSiswa
import com.catatpelanggaran.gurubk.dashboard.catat.CatatPelanggaranActivity
import com.catatpelanggaran.gurubk.model.Catat
import com.catatpelanggaran.gurubk.model.Pelanggaran
import com.catatpelanggaran.gurubk.model.Siswa
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_catat_pelanggaran.*
import kotlinx.android.synthetic.main.activity_data_pelanggar.*
import kotlinx.android.synthetic.main.activity_siswa.*
import kotlinx.android.synthetic.main.activity_siswa.progress_bar
import kotlinx.android.synthetic.main.activity_siswa.siswa_empty

class DataPelanggarActivity : AppCompatActivity() {

    companion object {
        const val DATA_PELANGGAR = "dataCatat"
    }

    var listPelanggaran: ArrayList<Pelanggaran>? = null
    var listSiswa: ArrayList<Catat>? = null

    lateinit var nis: String
    lateinit var data: String
    lateinit var dataCatat: Catat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_pelanggar)
        setSupportActionBar(toolbar_datapel)

        dataCatat = intent.getParcelableExtra(DATA_PELANGGAR)!!

        back_data.setOnClickListener {
            onBackPressed()
        }

        getData(null, dataCatat)
    }

    override fun onResume() {
        super.onResume()
        getData(null, dataCatat)
    }

    private fun getData(query: String?, dataCatat: Catat) {
        progress_bar.visibility = View.VISIBLE
        list_datapel.layoutManager = LinearLayoutManager(this)
        list_datapel.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null){
            listSiswa = arrayListOf()
            database.child("Pelanggar").child(dataCatat.nis!!).orderByChild("nama_siswa").startAt(query).endAt(query + "\uf8ff")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@DataPelanggarActivity, "Somethings wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listSiswa!!.clear()
                            for (i in snapshot.children) {
                                val siswa = i.getValue(Catat::class.java)
                                listSiswa!!.add(siswa!!)
                            }
                            val adapter = AdapterDataPelanggar(listSiswa!!)
                            list_datapel.adapter = adapter
                            progress_bar.visibility = View.GONE
                            siswa_empty.visibility = View.GONE
                            list_datapel.visibility = View.VISIBLE

                            adapter.onItemClick = { selectedSiswa ->
                                val intent = Intent(this@DataPelanggarActivity, CatatPelanggaranActivity::class.java)
                                intent.putExtra(
                                    CatatPelanggaranActivity.DATA_PELANGGAR, selectedSiswa
                                )
                                startActivity(intent)
                            }

                            adapter.onItemDeleteClick = { selectedSiswa ->
                                val builderdelete = AlertDialog.Builder(this@DataPelanggarActivity)
                                builderdelete.setTitle("Warning!")
                                builderdelete.setMessage("Are you sure want to delete ${selectedSiswa!!.nama_siswa} ?")
                                builderdelete.setPositiveButton("Delete") { _, _ ->
                                    database.child("Pelanggar").child(selectedSiswa.nis!!).removeValue()
                                        .addOnCompleteListener {
                                            Toast.makeText(this@DataPelanggarActivity, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                builderdelete.setNegativeButton("Cancel") { _, _ ->
                                    Toast.makeText(applicationContext, "Data tidak jadi dihapus", Toast.LENGTH_SHORT).show()
                                }
                                val dialogdelete = builderdelete.create()
                                dialogdelete.show()
                            }

                        }
                        else {
                            siswa_empty.visibility = View.VISIBLE
                            list_datapel.visibility = View.GONE
                        }
                    }
                })
        }
        else {
            listSiswa = arrayListOf()
            database.child("Pelanggar").child(dataCatat.nis!!).orderByChild("nama_siswa")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@DataPelanggarActivity, "Somethings wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listSiswa!!.clear()
                            for (i in snapshot.children) {
                                val siswa = i.getValue(Catat::class.java)
                                listSiswa!!.add(siswa!!)
                            }
                            val adapter = AdapterDataPelanggar(listSiswa!!)
                            list_datapel.adapter = adapter
                            progress_bar.visibility = View.GONE
                            siswa_empty.visibility = View.GONE
                            list_datapel.visibility = View.VISIBLE

                            adapter.onItemClick = { selectedSiswa ->
                                val intent = Intent(this@DataPelanggarActivity, CatatPelanggaranActivity::class.java)
                                intent.putExtra(
                                    CatatPelanggaranActivity.DATA_PELANGGAR,
                                    selectedSiswa
                                )
                                startActivity(intent)
                            }

                            adapter.onItemDeleteClick = { selectedSiswa ->
                                val builderdelete = AlertDialog.Builder(this@DataPelanggarActivity)
                                builderdelete.setTitle("Warning!")
                                builderdelete.setMessage("Are you sure want to delete ${selectedSiswa!!.nama_siswa} ?")
                                builderdelete.setPositiveButton("Delete") { _, _ ->
                                    database.child("Pelanggar").child(selectedSiswa.nis!!).removeValue()
                                        .addOnCompleteListener {
                                            Toast.makeText(this@DataPelanggarActivity, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                                        }
                                }
                                builderdelete.setNegativeButton("Cancel") { _, _ ->
                                    Toast.makeText(applicationContext, "Data tidak jadi dihapus", Toast.LENGTH_SHORT).show()
                                }
                                val dialogdelete = builderdelete.create()
                                dialogdelete.show()
                            }

                        }
                        else {
                            siswa_empty.visibility = View.VISIBLE
                            list_datapel.visibility = View.GONE
                        }
                    }
                })

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}