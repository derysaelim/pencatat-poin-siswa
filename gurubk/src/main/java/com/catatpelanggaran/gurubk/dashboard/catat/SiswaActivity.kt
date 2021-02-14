package com.catatpelanggaran.gurubk.dashboard.catat

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.adapter.AdapterSiswa
import com.catatpelanggaran.gurubk.model.Catat
import com.catatpelanggaran.gurubk.model.Kelas
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_siswa.*

class SiswaActivity : AppCompatActivity() {

    companion object {
        const val DATA_KELAS = "DATA_KELAS"
    }

    var listSiswa: ArrayList<Catat>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView
    lateinit var dataKelas: Kelas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siswa)
        setSupportActionBar(toolbar_siswa)

        dataKelas = intent.getParcelableExtra(DATA_KELAS)!!
        text_kelas.text = "${dataKelas.tingkat + " " + dataKelas.jurusan + " " + dataKelas.kelas}"

        back_button.setOnClickListener {
            onBackPressed()
        }

        getData(null, dataKelas)
    }

    override fun onResume() {
        super.onResume()
        getData(null, dataKelas)
    }

    private fun getData(query: String?, dataKelas: Kelas) {
        progress_bar.visibility = View.VISIBLE
        list_siswa.layoutManager = LinearLayoutManager(this)
        list_siswa.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null) {
            val search = query.replace("\\s".toRegex(), "")
            listSiswa = arrayListOf()
            database.child("daftar_kelas").child(dataKelas.idKelas!!).orderByChild("nama_siswa")
                .startAt(search).endAt(search + "\uf8ff")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@SiswaActivity, "Somethings wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listSiswa!!.clear()
                            for (i in snapshot.children) {
                                val siswa = i.getValue(Catat::class.java)
                                listSiswa!!.add(siswa!!)
                            }
                            val adapter = AdapterSiswa(listSiswa!!)
                            list_siswa.adapter = adapter
                            progress_bar.visibility = View.GONE
                            siswa_empty.visibility = View.GONE
                            list_siswa.visibility = View.VISIBLE

                            adapter.onItemClick = { selectedSiswa ->
                                val alertBuilder = AlertDialog.Builder(this@SiswaActivity)
                                alertBuilder.setTitle("Catat")
                                alertBuilder.setMessage("Pilih salah satu")
                                alertBuilder.setPositiveButton("Pelanggaran") { _, _ ->
                                    val siswa = Intent(
                                        this@SiswaActivity,
                                        CatatPelanggaranActivity::class.java
                                    )
                                    siswa.putExtra(
                                        CatatPelanggaranActivity.DATA_PELANGGAR,
                                        selectedSiswa
                                    )
                                    startActivity(siswa)
                                }
                                alertBuilder.setNegativeButton("Penghargaan") { _, _ ->
                                    val siswa = Intent(
                                        this@SiswaActivity,
                                        CatatPenghargaanActivity::class.java
                                    )
                                    siswa.putExtra(
                                        CatatPenghargaanActivity.DATA_PENGHARGAAN,
                                        selectedSiswa
                                    )
                                    startActivity(siswa)
                                }
                                val dialogCatat = alertBuilder.create()
                                dialogCatat.show()
                            }
                        } else {
                            siswa_empty.visibility = View.VISIBLE
                            list_siswa.visibility = View.GONE
                        }
                    }
                })
        }
        else {
            listSiswa = arrayListOf()
            database.child("daftar_kelas").child(dataKelas.idKelas!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@SiswaActivity, "Somethings wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listSiswa!!.clear()
                            for (i in snapshot.children) {
                                val siswa = i.getValue(Catat::class.java)
                                listSiswa!!.add(siswa!!)
                            }
                            val adapter = AdapterSiswa(listSiswa!!)
                            list_siswa.adapter = adapter
                            progress_bar.visibility = View.GONE
                            siswa_empty.visibility = View.GONE
                            list_siswa.visibility = View.VISIBLE

                            adapter.onItemClick = { selectedSiswa ->
                                val alertBuilder = AlertDialog.Builder(this@SiswaActivity)
                                alertBuilder.setTitle("Catat")
                                alertBuilder.setMessage("Pilih salah satu")
                                alertBuilder.setPositiveButton("Pelanggaran") { _, _ ->
                                    val siswa = Intent(
                                        this@SiswaActivity,
                                        CatatPelanggaranActivity::class.java
                                    )
                                    siswa.putExtra(
                                        CatatPelanggaranActivity.DATA_PELANGGAR,
                                        selectedSiswa
                                    )
                                    startActivity(siswa)
                                }
                                alertBuilder.setNegativeButton("Penghargaan") { _, _ ->
                                    val siswa = Intent(
                                        this@SiswaActivity,
                                        CatatPenghargaanActivity::class.java
                                    )
                                    siswa.putExtra(
                                        CatatPenghargaanActivity.DATA_PENGHARGAAN,
                                        selectedSiswa
                                    )
                                    startActivity(siswa)
                                }
                                val dialogCatat = alertBuilder.create()
                                dialogCatat.show()
                            }
                        } else {
                            siswa_empty.visibility = View.VISIBLE
                            list_siswa.visibility = View.GONE
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
                getData(query, dataKelas)
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