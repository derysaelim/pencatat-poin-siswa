package com.catatpelanggaran.admin.dashboard.pelanggaran

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.adapter.AdapterGuru
import com.catatpelanggaran.admin.adapter.AdapterPelanggaran
import com.catatpelanggaran.admin.dashboard.guru.AddGuruActivity
import com.catatpelanggaran.admin.model.Guru
import com.catatpelanggaran.admin.model.Pelanggaran
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_guru.*
import kotlinx.android.synthetic.main.activity_pelanggaran.*
import kotlinx.android.synthetic.main.activity_pelanggaran.back_button
import kotlinx.android.synthetic.main.activity_pelanggaran.progress_bar

class PelanggaranActivity : AppCompatActivity() {

    var listPelanggaran: ArrayList<Pelanggaran>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelanggaran)
        setSupportActionBar(toolbar_pelanggaran)

        back_button.setOnClickListener { finish() }
        getData(null)
    }

    override fun onResume() {
        super.onResume()
        getData(null)
    }

    private fun getData(query: String?) {
        progress_bar.visibility = View.VISIBLE
        list_pelanggaran.layoutManager = LinearLayoutManager(this)
        list_pelanggaran.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null) {
            listPelanggaran = arrayListOf()
            database.child("jenis_pelanggaran").orderByChild("namaPelanggaran").startAt(query)
                .endAt(query + "\uf8ff").addValueEventListener(
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

                                adapter.onItemClick = { selectedPelanggaran ->
                                    val intent = Intent(
                                        this@PelanggaranActivity,
                                        AddPelanggaranActivity::class.java
                                    )
                                    intent.putExtra(
                                        AddPelanggaranActivity.DATA_PELANGGARAN,
                                        selectedPelanggaran
                                    )
                                    startActivity(intent)
                                }

                                adapter.onItemDeleteClick = { selectedPelanggaran ->
                                    val builderdelete =
                                        AlertDialog.Builder(this@PelanggaranActivity)
                                    builderdelete.setTitle("Warning!")
                                    builderdelete.setMessage("Are you sure want to delete ${selectedPelanggaran.namaPelanggaran} ?")
                                    builderdelete.setPositiveButton("Delete") { _, _ ->
                                        database.child("jenis_pelanggaran")
                                            .child(selectedPelanggaran.idPelanggaran!!)
                                            .removeValue()
                                            .addOnCompleteListener {
                                                Toast.makeText(
                                                    this@PelanggaranActivity,
                                                    "Berhasil dihapus",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                    builderdelete.setNegativeButton("Cancel") { i, _ ->
                                        Toast.makeText(
                                            applicationContext,
                                            "Data tidak jadi dihapus",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    val dialogdelete = builderdelete.create()
                                    dialogdelete.show()
                                }

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
        } else {
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

                                adapter.onItemClick = { selectedPelanggaran ->
                                    val intent = Intent(
                                        this@PelanggaranActivity,
                                        AddPelanggaranActivity::class.java
                                    )
                                    intent.putExtra(
                                        AddPelanggaranActivity.DATA_PELANGGARAN,
                                        selectedPelanggaran
                                    )
                                    startActivity(intent)
                                }

                                adapter.onItemDeleteClick = { selectedPelanggaran ->
                                    val builderdelete =
                                        AlertDialog.Builder(this@PelanggaranActivity)
                                    builderdelete.setTitle("Warning!")
                                    builderdelete.setMessage("Are you sure want to delete ${selectedPelanggaran.namaPelanggaran} ?")
                                    builderdelete.setPositiveButton("Delete") { i, _ ->
                                        database.child("jenis_pelanggaran")
                                            .child(selectedPelanggaran.idPelanggaran!!)
                                            .removeValue()
                                            .addOnCompleteListener {
                                                Toast.makeText(
                                                    this@PelanggaranActivity,
                                                    "Berhasil dihapus",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                    builderdelete.setNegativeButton("Cancel") { i, _ ->
                                        Toast.makeText(
                                            applicationContext,
                                            "Data tidak jadi dihapus",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    val dialogdelete = builderdelete.create()
                                    dialogdelete.show()
                                }

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
//                getData(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                getData(query)
                return true
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_button -> {
                val intent = Intent(this, AddPelanggaranActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}