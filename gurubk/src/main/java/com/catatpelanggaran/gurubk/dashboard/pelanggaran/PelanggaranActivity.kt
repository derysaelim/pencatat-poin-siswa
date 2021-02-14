package com.catatpelanggaran.gurubk.dashboard.pelanggaran

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.adapter.AdapterPelanggaran
import com.catatpelanggaran.gurubk.model.Catat
import com.catatpelanggaran.gurubk.model.Pelanggaran
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_catat_pelanggaran.*
import kotlinx.android.synthetic.main.activity_pelanggaran.*

class PelanggaranActivity : AppCompatActivity() {

    var listPelanggaran: ArrayList<Pelanggaran>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelanggaran)
        setSupportActionBar(toolbar_pelanggaran)

        back_button.setOnClickListener {
            onBackPressed()
        }
        getData(null)
    }

    override fun onResume() {
        super.onResume()
        getData(null)
    }

    private fun getData(query: String?){
        progress_bar.visibility = View.VISIBLE
        list_pelanggaran.layoutManager = LinearLayoutManager(this)
        list_pelanggaran.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null){
            listPelanggaran = arrayListOf()
            database.child("jenis_pelanggaran").orderByChild("namaPelanggaran").startAt(query).endAt(query + "\uf8ff")
                .addValueEventListener(
                    object : ValueEventListener {
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
                                pelanggaran_empty.visibility = View.VISIBLE
                                list_pelanggaran.visibility = View.GONE
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@PelanggaranActivity,
                                "check ur inet",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
        }
        else {
            listPelanggaran = arrayListOf()
            database.child("jenis_pelanggaran").orderByChild("namaPelanggaran")
                .addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                listPelanggaran!!.clear()
                                for (i in snapshot.children) {
                                    val pelanggaran = i.getValue(Pelanggaran::class.java)
                                    listPelanggaran?.add(pelanggaran!!)
                                }

                                val adapter = AdapterPelanggaran(listPelanggaran!!)
                                list_pelanggaran.adapter = adapter
                                progress_bar.visibility = View.GONE
                                pelanggaran_empty.visibility = View.GONE
                                list_pelanggaran.visibility = View.VISIBLE
                            } else {
                                pelanggaran_empty.visibility = View.VISIBLE
                                list_pelanggaran.visibility = View.GONE
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@PelanggaranActivity,
                                "check ur inet",
                                Toast.LENGTH_SHORT
                            ).show()
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
                getData(query)
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