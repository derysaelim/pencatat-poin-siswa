package com.catatpelanggaran.admin.dashboard.gurubk

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
import com.catatpelanggaran.admin.dashboard.petugas.AddPetugasActivity
import com.catatpelanggaran.admin.model.Guru
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_bk.*
import kotlinx.android.synthetic.main.activity_bk.back_button
import kotlinx.android.synthetic.main.activity_bk.progress_bar
import kotlinx.android.synthetic.main.activity_petugas.*

class BkActivity : AppCompatActivity() {

    var listGuruBK: ArrayList<Guru>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bk)
        setSupportActionBar(toolbar_gurubk)

        back_button.setOnClickListener { finish() }

        getData(null)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search_bar).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Cari"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                getData(query)
                return true
            }

        })
        return true
    }

    private fun getData(query: String?) {
        progress_bar.visibility = View.VISIBLE
        list_gurubk.layoutManager = LinearLayoutManager(this)
        list_gurubk.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null) {
            listGuruBK = arrayListOf<Guru>()
            database.child("Guru_BK").orderByChild("nama").startAt(query).endAt(query + "\uf8ff")
                .addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                listGuruBK!!.clear()
                                for (i in snapshot.children) {
                                    val gurubk = i.getValue(Guru::class.java)
                                    listGuruBK!!.add(gurubk!!)
                                }
                                val adapter = AdapterGuru(listGuruBK!!)
                                list_gurubk.adapter = adapter
                                progress_bar.visibility = View.GONE
                                list_gurubk.visibility = View.VISIBLE
                                gurubk_empty.visibility = View.GONE

                                adapter.onItemClick = {
                                    val intent =
                                        Intent(this@BkActivity, AddPetugasActivity::class.java)
                                    intent.putExtra(AddPetugasActivity.DATA_PETUGAS, it)
                                    startActivity(intent)
                                }
                                adapter.onItemDeleteClick = { selectedPetugas ->
                                    val builderdelete = AlertDialog.Builder(this@BkActivity)
                                    builderdelete.setTitle("Warning!")
                                    builderdelete.setMessage("Are you sure want to delete ${selectedPetugas.nama} ?")
                                    builderdelete.setPositiveButton("Delete") { i, _ ->
                                        database.child("Guru_BK").child(selectedPetugas.nip!!)
                                            .removeValue()
                                            .addOnCompleteListener {
                                                database.child("Login").child(selectedPetugas.nip)
                                                    .removeValue().addOnCompleteListener {
                                                        Toast.makeText(
                                                            this@BkActivity,
                                                            "Berhasil dihapus",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
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
                                gurubk_empty.visibility = View.VISIBLE
                                list_gurubk.visibility = View.GONE
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@BkActivity, "something wrong", Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
        } else {
            listGuruBK = arrayListOf<Guru>()
            database.child("Guru_BK").orderByChild("nama")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listGuruBK!!.clear()
                            for (i in snapshot.children) {
                                val gurubk = i.getValue(Guru::class.java)
                                listGuruBK!!.add(gurubk!!)
                            }
                            val adapter = AdapterGuru(listGuruBK!!)
                            list_gurubk.adapter = adapter
                            progress_bar.visibility = View.GONE
                            list_gurubk.visibility = View.VISIBLE
                            gurubk_empty.visibility = View.GONE

                            adapter.onItemClick = {
                                val intent = Intent(this@BkActivity, AddPetugasActivity::class.java)
                                intent.putExtra(AddPetugasActivity.DATA_PETUGAS, it)
                                startActivity(intent)
                            }
                            adapter.onItemDeleteClick = { selectedPetugas ->
                                val builderdelete = AlertDialog.Builder(this@BkActivity)
                                builderdelete.setTitle("Warning!")
                                builderdelete.setMessage("Are you sure want to delete ${selectedPetugas.nama} ?")
                                builderdelete.setPositiveButton("Delete") { i, _ ->
                                    database.child("Guru_BK").child(selectedPetugas.nip!!)
                                        .removeValue()
                                        .addOnCompleteListener {
                                            database.child("Login").child(selectedPetugas.nip)
                                                .removeValue().addOnCompleteListener {
                                                    Toast.makeText(
                                                        this@BkActivity,
                                                        "Berhasil dihapus",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
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
                            gurubk_empty.visibility = View.VISIBLE
                            list_gurubk.visibility = View.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@BkActivity, "something wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                })
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_button -> {
                val intent = Intent(this, AddPetugasActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    override fun onResume() {
        super.onResume()
        getData(null)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}