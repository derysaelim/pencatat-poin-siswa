package com.catatpelanggaran.admin.dashboard.penghargaan

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
import com.catatpelanggaran.admin.adapter.AdapterPenghargaan
import com.catatpelanggaran.admin.model.Penghargaan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_pelanggaran.*
import kotlinx.android.synthetic.main.activity_penghargaan.*
import kotlinx.android.synthetic.main.activity_penghargaan.back_button
import kotlinx.android.synthetic.main.activity_penghargaan.progress_bar

class PenghargaanActivity : AppCompatActivity() {

    var listPenghargaan: ArrayList<Penghargaan>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penghargaan)
        setSupportActionBar(toolbar_penghargaan)

        back_button.setOnClickListener { finish() }

        getData(null)
    }

    override fun onResume() {
        super.onResume()
        getData(null)
    }

    fun getData(query: String?) {
        progress_bar.visibility = View.VISIBLE
        list_penghargaan.layoutManager = LinearLayoutManager(this)
        list_penghargaan.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null) {
            listPenghargaan = arrayListOf()
            database.child("jenis_penghargaan").orderByChild("namaPenghargaan").startAt(query)
                .endAt(query + "\uf8ff").addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                listPenghargaan!!.clear()
                                for (i in snapshot.children) {
                                    val penghargaan = i.getValue(Penghargaan::class.java)
                                    listPenghargaan?.add(penghargaan!!)
                                }

                                val adapter = AdapterPenghargaan(listPenghargaan!!)
                                list_penghargaan.adapter = adapter
                                progress_bar.visibility = View.GONE
                                penghargaan_empty.visibility = View.GONE
                                list_penghargaan.visibility = View.VISIBLE

                                adapter.onItemClick = { selectedPenghargaan ->
                                    val intent = Intent(
                                        this@PenghargaanActivity,
                                        AddPenghargaanActivity::class.java
                                    )
                                    intent.putExtra(
                                        AddPenghargaanActivity.DATA_PENGHARGAAN,
                                        selectedPenghargaan
                                    )
                                    startActivity(intent)
                                }

                                adapter.onItemDeleteClick = { selectedPenghargaan ->
                                    val builderdelete =
                                        AlertDialog.Builder(this@PenghargaanActivity)
                                    builderdelete.setTitle("Warning!")
                                    builderdelete.setMessage("Are you sure want to delete ${selectedPenghargaan.namaPenghargaan} ?")
                                    builderdelete.setPositiveButton("Delete") { _, _ ->
                                        database.child("jenis_penghargaan")
                                            .child(selectedPenghargaan.id_penghargaan!!)
                                            .removeValue()
                                            .addOnCompleteListener {
                                                Toast.makeText(
                                                    this@PenghargaanActivity,
                                                    "Berhasil dihapus",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                    builderdelete.setNegativeButton("Cancel") { _, _ ->
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
                                penghargaan_empty.visibility = View.VISIBLE
                                list_penghargaan.visibility = View.GONE
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@PenghargaanActivity,
                                "check ur inet",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
        } else {
            listPenghargaan = arrayListOf()
            database.child("jenis_penghargaan").orderByChild("namaPenghargaan")
                .addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                listPenghargaan!!.clear()
                                for (i in snapshot.children) {
                                    val penghargaan = i.getValue(Penghargaan::class.java)
                                    listPenghargaan?.add(penghargaan!!)
                                }

                                val adapter = AdapterPenghargaan(listPenghargaan!!)
                                list_penghargaan.adapter = adapter
                                progress_bar.visibility = View.GONE
                                penghargaan_empty.visibility = View.GONE
                                list_penghargaan.visibility = View.VISIBLE

                                adapter.onItemClick = { selectedPenghargaan ->
                                    val intent = Intent(
                                        this@PenghargaanActivity,
                                        AddPenghargaanActivity::class.java
                                    )
                                    intent.putExtra(
                                        AddPenghargaanActivity.DATA_PENGHARGAAN,
                                        selectedPenghargaan
                                    )
                                    startActivity(intent)
                                }

                                adapter.onItemDeleteClick = { selectedPenghargaan ->
                                    val builderdelete =
                                        AlertDialog.Builder(this@PenghargaanActivity)
                                    builderdelete.setTitle("Warning!")
                                    builderdelete.setMessage("Are you sure want to delete ${selectedPenghargaan.namaPenghargaan} ?")
                                    builderdelete.setPositiveButton("Delete") { _, _ ->
                                        database.child("jenis_penghargaan")
                                            .child(selectedPenghargaan.id_penghargaan!!)
                                            .removeValue()
                                            .addOnCompleteListener {
                                                Toast.makeText(
                                                    this@PenghargaanActivity,
                                                    "Berhasil dihapus",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                    builderdelete.setNegativeButton("Cancel") { _, _ ->
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
                                penghargaan_empty.visibility = View.VISIBLE
                                list_penghargaan.visibility = View.GONE
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@PenghargaanActivity,
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_button -> {
                val intent = Intent(this, AddPenghargaanActivity::class.java)
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