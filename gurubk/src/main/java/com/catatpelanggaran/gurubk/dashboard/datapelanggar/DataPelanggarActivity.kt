package com.catatpelanggaran.gurubk.dashboard.datapelanggar

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.adapter.AdapterDataPelanggar
import com.catatpelanggaran.gurubk.model.Catat
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
        const val DATA_SISWA = "DATA_SISWA"
    }

    var listSiswa: ArrayList<Catat>? = null
    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView
    lateinit var data: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_pelanggar)
        setSupportActionBar(toolbar_datapel)

        data = intent.getStringExtra(DATA_SISWA).toString()

        back_data.setOnClickListener {
            onBackPressed()
        }

        if (data == "langgar") {
            getDataPelanggaran(null)
        } else {
            getDataPenghargaan(null)
        }
    }

    override fun onResume() {
        super.onResume()
        if (data == "langgar") {
            getDataPelanggaran(null)
        } else {
            getDataPenghargaan(null)
        }
    }

    private fun getDataPelanggaran(query: String?) {
        progress_bar.visibility = View.VISIBLE
        list_datapel.layoutManager = LinearLayoutManager(this)
        list_datapel.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null) {
            listSiswa = arrayListOf()
            database.child("Pelanggar").orderByChild("nama_siswa").startAt(query)
                .endAt(query + "\uf8ff")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@DataPelanggarActivity,
                            "Somethings wrong",
                            Toast.LENGTH_SHORT
                        )
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
                                val intent = Intent(
                                    this@DataPelanggarActivity,
                                    DetailPelanggarActivity::class.java
                                )
                                intent.putExtra(DetailPelanggarActivity.DATA_NIS, selectedSiswa.nis)
                                intent.putExtra(DetailPelanggarActivity.DATA_SISWA, "langgar")
                                startActivity(intent)
                            }

                            adapter.onItemDeleteClick = { selectedSiswa ->
                                val builderdelete = AlertDialog.Builder(this@DataPelanggarActivity)
                                builderdelete.setTitle("Warning!")
                                builderdelete.setMessage("Are you sure want to delete ${selectedSiswa.nama_siswa} ?")
                                builderdelete.setPositiveButton("Delete") { _, _ ->
                                    database.child("Pelanggar").child(selectedSiswa.nis!!)
                                        .removeValue()
                                        .addOnCompleteListener {
                                            Toast.makeText(
                                                this@DataPelanggarActivity,
                                                "Berhasil dihapus",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                                builderdelete.setNegativeButton("Cancel") { _, _ ->
                                    Toast.makeText(
                                        applicationContext,
                                        "Data tidak jadi dihapus",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                val dialogdelete = builderdelete.create()
                                dialogdelete.show()
                            }

                        } else {
                            siswa_empty.visibility = View.VISIBLE
                            list_datapel.visibility = View.GONE
                        }
                    }
                })
        } else {
            listSiswa = arrayListOf()
            database.child("Pelanggar").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@DataPelanggarActivity,
                        "Somethings wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        listSiswa!!.clear()
                        for (x in snapshot.children) {
                            val siswa = x.getValue(Catat::class.java)
                            listSiswa!!.add(siswa!!)
                        }
                        val adapter = AdapterDataPelanggar(listSiswa!!)
                        list_datapel.adapter = adapter
                        progress_bar.visibility = View.GONE
                        siswa_empty.visibility = View.GONE
                        list_datapel.visibility = View.VISIBLE

                        adapter.onItemClick = { selectedSiswa ->
                            val intent = Intent(
                                this@DataPelanggarActivity,
                                DetailPelanggarActivity::class.java
                            )
                            intent.putExtra(DetailPelanggarActivity.DATA_NIS, selectedSiswa.nis)
                            intent.putExtra(DetailPelanggarActivity.DATA_SISWA, "langgar")
                            startActivity(intent)
                        }

                        adapter.onItemDeleteClick = { selectedSiswa ->
                            val builderdelete = AlertDialog.Builder(this@DataPelanggarActivity)
                            builderdelete.setTitle("Warning!")
                            builderdelete.setMessage("Are you sure want to delete ${selectedSiswa.nama_siswa} ?")
                            builderdelete.setPositiveButton("Delete") { _, _ ->
                                database.child("Pelanggar").child(selectedSiswa.nis!!)
                                    .removeValue()
                                    .addOnCompleteListener {
                                        Toast.makeText(
                                            this@DataPelanggarActivity,
                                            "Berhasil dihapus",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                            builderdelete.setNegativeButton("Cancel") { _, _ ->
                                Toast.makeText(
                                    applicationContext,
                                    "Data tidak jadi dihapus",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            val dialogdelete = builderdelete.create()
                            dialogdelete.show()
                        }
                    } else {
                        siswa_empty.visibility = View.VISIBLE
                        list_datapel.visibility = View.GONE
                    }
                }
            })
        }
    }

    private fun getDataPenghargaan(query: String?) {
        progress_bar.visibility = View.VISIBLE
        list_datapel.layoutManager = LinearLayoutManager(this)
        list_datapel.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null) {
            listSiswa = arrayListOf()
            database.child("Penghargaan").orderByChild("nama_siswa").startAt(query)
                .endAt(query + "\uf8ff")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@DataPelanggarActivity,
                            "Somethings wrong",
                            Toast.LENGTH_SHORT
                        )
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
                                val intent = Intent(
                                    this@DataPelanggarActivity,
                                    DetailPelanggarActivity::class.java
                                )
                                intent.putExtra(DetailPelanggarActivity.DATA_NIS, selectedSiswa.nis)
                                intent.putExtra(DetailPelanggarActivity.DATA_SISWA, "reward")
                                startActivity(intent)
                            }

                            adapter.onItemDeleteClick = { selectedSiswa ->
                                val builderdelete = AlertDialog.Builder(this@DataPelanggarActivity)
                                builderdelete.setTitle("Warning!")
                                builderdelete.setMessage("Are you sure want to delete ${selectedSiswa.nama_siswa} ?")
                                builderdelete.setPositiveButton("Delete") { _, _ ->
                                    database.child("Pelanggar").child(selectedSiswa.nis!!)
                                        .removeValue()
                                        .addOnCompleteListener {
                                            Toast.makeText(
                                                this@DataPelanggarActivity,
                                                "Berhasil dihapus",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                                builderdelete.setNegativeButton("Cancel") { _, _ ->
                                    Toast.makeText(
                                        applicationContext,
                                        "Data tidak jadi dihapus",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                val dialogdelete = builderdelete.create()
                                dialogdelete.show()
                            }

                        } else {
                            siswa_empty.visibility = View.VISIBLE
                            list_datapel.visibility = View.GONE
                        }
                    }
                })
        } else {
            listSiswa = arrayListOf()
            database.child("Penghargaan").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@DataPelanggarActivity,
                        "Somethings wrong",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        listSiswa!!.clear()
                        for (x in snapshot.children) {
                            val siswa = x.getValue(Catat::class.java)
                            listSiswa!!.add(siswa!!)
                        }
                        val adapter = AdapterDataPelanggar(listSiswa!!)
                        list_datapel.adapter = adapter
                        progress_bar.visibility = View.GONE
                        siswa_empty.visibility = View.GONE
                        list_datapel.visibility = View.VISIBLE

                        adapter.onItemClick = { selectedSiswa ->
                            val intent = Intent(
                                this@DataPelanggarActivity,
                                DetailPelanggarActivity::class.java
                            )
                            intent.putExtra(DetailPelanggarActivity.DATA_NIS, selectedSiswa.nis)
                            intent.putExtra(DetailPelanggarActivity.DATA_SISWA, "reward")
                            startActivity(intent)
                        }

                        adapter.onItemDeleteClick = { selectedSiswa ->
                            val builderdelete = AlertDialog.Builder(this@DataPelanggarActivity)
                            builderdelete.setTitle("Warning!")
                            builderdelete.setMessage("Are you sure want to delete ${selectedSiswa.nama_siswa} ?")
                            builderdelete.setPositiveButton("Delete") { _, _ ->
                                database.child("Pelanggar").child(selectedSiswa.nis!!).removeValue()
                                    .addOnCompleteListener {
                                        Toast.makeText(
                                            this@DataPelanggarActivity,
                                            "Berhasil dihapus",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                            builderdelete.setNegativeButton("Cancel") { _, _ ->
                                Toast.makeText(
                                    applicationContext,
                                    "Data tidak jadi dihapus",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            val dialogdelete = builderdelete.create()
                            dialogdelete.show()
                        }
                    } else {
                        siswa_empty.visibility = View.VISIBLE
                        list_datapel.visibility = View.GONE
                    }
                }
            })

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search_bar).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Cari"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (data == "langgar") {
                    getDataPelanggaran(query)
                } else {
                    getDataPenghargaan(query)
                }
                return true
            }
        })
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}