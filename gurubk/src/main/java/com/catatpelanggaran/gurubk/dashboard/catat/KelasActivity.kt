package com.catatpelanggaran.gurubk.dashboard.catat

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
import com.catatpelanggaran.gurubk.adapter.AdapterKelas
import com.catatpelanggaran.gurubk.model.Kelas
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_catat_pelanggaran.*
import kotlinx.android.synthetic.main.activity_kelas_guru.*

class KelasActivity : AppCompatActivity() {

    companion object {
        const val DATA_SISWA = "DATA_SISWA"
    }

    var listKelas: ArrayList<Kelas>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView
    lateinit var data: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelas_guru)
        setSupportActionBar(toolbar_kelas)

        data = intent.getStringExtra(DATA_SISWA).toString()

        back_button.setOnClickListener {
            onBackPressed()
        }
        getData(null, data)
    }

    override fun onResume() {
        super.onResume()
        getData(null, data)
    }

    private fun getData(query: String?, data: String) {
        progress_bar.visibility = View.VISIBLE
        list_kelas.layoutManager = LinearLayoutManager(this)
        list_kelas.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null){
            val search = query.replace("\\s".toRegex(), "")
            listKelas = arrayListOf()
            database.child("kelas").orderByChild("idKelas").startAt(search).endAt(search + "\uf8ff")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@KelasActivity, "Somethings wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listKelas!!.clear()
                            for (i in snapshot.children) {
                                val kelas = i.getValue(Kelas::class.java)
                                listKelas!!.add(kelas!!)
                            }
                            val adapter = AdapterKelas(listKelas!!)
                            list_kelas.adapter = adapter
                            progress_bar.visibility = View.GONE
                            kelas_empty.visibility = View.GONE
                            list_kelas.visibility = View.VISIBLE

                            adapter.onItemClick = { selectedKelas ->
                                val intent = Intent(this@KelasActivity, SiswaActivity::class.java)
                                intent.putExtra(SiswaActivity.DATA_KELAS, selectedKelas)
                                intent.putExtra(SiswaActivity.DATA_SISWA, data)
                                startActivity(intent)
                            }
                        } else {
                            kelas_empty.visibility = View.VISIBLE
                            list_kelas.visibility = View.GONE
                        }
                    }
                })
        }
        else {
            listKelas = arrayListOf()
            database.child("kelas").orderByChild("idKelas")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@KelasActivity, "Somethings wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listKelas!!.clear()
                            for (i in snapshot.children) {
                                val kelas = i.getValue(Kelas::class.java)
                                listKelas!!.add(kelas!!)
                            }
                            val adapter = AdapterKelas(listKelas!!)
                            list_kelas.adapter = adapter
                            progress_bar.visibility = View.GONE
                            kelas_empty.visibility = View.GONE
                            list_kelas.visibility = View.VISIBLE

                            adapter.onItemClick = { selectedKelas ->
                                val intent = Intent(this@KelasActivity, SiswaActivity::class.java)
                                intent.putExtra(SiswaActivity.DATA_KELAS, selectedKelas)
                                intent.putExtra(SiswaActivity.DATA_SISWA, this@KelasActivity.data)
                                startActivity(intent)
                            }
                        } else {
                            kelas_empty.visibility = View.VISIBLE
                            list_kelas.visibility = View.GONE
                        }
                    }
                })

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_guru, menu)

        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search_bar).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Cari"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                getData(query, data)
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