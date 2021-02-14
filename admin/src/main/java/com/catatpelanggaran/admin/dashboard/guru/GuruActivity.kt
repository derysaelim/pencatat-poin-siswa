package com.catatpelanggaran.admin.dashboard.guru

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.adapter.AdapterGuru
import com.catatpelanggaran.admin.dashboard.petugas.PetugasActivity
import com.catatpelanggaran.admin.model.Guru
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_guru.*

class GuruActivity : AppCompatActivity() {

    companion object {
        const val NIP_PETUGAS = "nippetugas"
    }

    var listGuru: ArrayList<Guru>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView
    lateinit var nip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guru)
        setSupportActionBar(toolbar_guru)

        back_button.setOnClickListener {
            finish()
        }

        nip = intent.getStringExtra(PetugasActivity.NIP_PETUGAS).toString()
        getData(null)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        super.onResume()
        getData(null)
    }

    private fun getData(name: String?) {
        progress_bar.visibility = View.VISIBLE
        list_guru.layoutManager = LinearLayoutManager(this)
        list_guru.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (name != null) {
            listGuru = arrayListOf()
            database.child("Guru").orderByChild("nama").startAt(name).endAt(name + "\uf8ff")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listGuru!!.clear()
                            for (i in snapshot.children) {
                                val guru = i.getValue(Guru::class.java)
                                listGuru!!.add(guru!!)
                            }
                            val adapter = AdapterGuru(listGuru!!)
                            list_guru.adapter = adapter
                            progress_bar.visibility = View.GONE
                            guru_empty.visibility = View.GONE
                            list_guru.visibility = View.VISIBLE

                            adapter.onItemClick = { selectedGuru ->
                                val intent = Intent(this@GuruActivity, AddGuruActivity::class.java)
                                intent.putExtra(AddGuruActivity.DATA_GURU, selectedGuru)
                                intent.putExtra(AddGuruActivity.NIP_PETUGAS, nip)
                                startActivity(intent)
                            }

                            adapter.onItemDeleteClick = { selectedGuru ->
                                val builderdelete = AlertDialog.Builder(this@GuruActivity)
                                if (selectedGuru.nip == nip) {
                                    builderdelete.setTitle("Warning!")
                                    builderdelete.setMessage("You cant delete your self ${selectedGuru.nama} ")
                                    builderdelete.setPositiveButton("OK") { _, _ -> }
                                    val dialogdelete = builderdelete.create()
                                    dialogdelete.show()
                                } else {
                                    database.child("walikelas").addValueEventListener(object :
                                        ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.child(selectedGuru.nip!!).exists()) {
                                                builderdelete.setTitle("Warning!")
                                                builderdelete.setMessage("Tidak bisa menghapus ${selectedGuru.nama} karena terdaftar sebagai walikelas")
                                                builderdelete.setPositiveButton("OK") { i, _ -> }
                                            } else {
                                                builderdelete.setTitle("Warning!")
                                                builderdelete.setMessage("Are you sure want to delete ${selectedGuru.nama} ?")
                                                builderdelete.setPositiveButton("Delete") { i, _ ->
                                                    database.child("Guru").child(selectedGuru.nip!!)
                                                        .removeValue()
                                                        .addOnCompleteListener {
                                                            Toast.makeText(
                                                                this@GuruActivity,
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
                                            }

                                            val dialogdelete = builderdelete.create()
                                            dialogdelete.show()
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(
                                                this@GuruActivity,
                                                "Something Wrong",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    })
                                }
                            }
                        } else {
                            guru_empty.visibility = View.VISIBLE
                            list_guru.visibility = View.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        progress_bar.visibility = View.GONE
                        Toast.makeText(this@GuruActivity, "Somethings wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                })
        } else {
            val database = FirebaseDatabase.getInstance().reference
            listGuru = arrayListOf()
            database.child("Guru").orderByChild("nama")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listGuru!!.clear()
                            for (i in snapshot.children) {
                                val guru = i.getValue(Guru::class.java)
                                listGuru?.add(guru!!)
                            }
                            val adapter = AdapterGuru(listGuru!!)
                            list_guru.adapter = adapter
                            progress_bar.visibility = View.GONE
                            guru_empty.visibility = View.GONE

                            adapter.onItemClick = { selectedGuru ->
                                val intent = Intent(this@GuruActivity, AddGuruActivity::class.java)
                                intent.putExtra(AddGuruActivity.DATA_GURU, selectedGuru)
                                intent.putExtra(AddGuruActivity.NIP_PETUGAS, nip)
                                startActivity(intent)
                            }

                            adapter.onItemDeleteClick = { selectedGuru ->
                                val builderdelete = AlertDialog.Builder(this@GuruActivity)
                                if (selectedGuru.nip == nip) {
                                    builderdelete.setTitle("Warning!")
                                    builderdelete.setMessage("You cant delete your self ${selectedGuru.nama} ")
                                    builderdelete.setPositiveButton("OK") { _, _ -> }
                                    val dialogdelete = builderdelete.create()
                                    dialogdelete.show()
                                } else {
                                    database.child("walikelas").addValueEventListener(object :
                                        ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.child(selectedGuru.nip!!).exists()) {
                                                builderdelete.setTitle("Warning!")
                                                builderdelete.setMessage("Tidak bisa menghapus ${selectedGuru.nama} karena terdaftar sebagai walikelas")
                                                builderdelete.setPositiveButton("OK") { i, _ -> }
                                            } else {
                                                builderdelete.setTitle("Warning!")
                                                builderdelete.setMessage("Are you sure want to delete ${selectedGuru.nama} ?")
                                                builderdelete.setPositiveButton("Delete") { i, _ ->
                                                    database.child("Guru").child(selectedGuru.nip!!)
                                                        .removeValue()
                                                        .addOnCompleteListener {
                                                            Toast.makeText(
                                                                this@GuruActivity,
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
                                            }

                                            val dialogdelete = builderdelete.create()
                                            dialogdelete.show()
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(
                                                this@GuruActivity,
                                                "Something Wrong",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    })
                                }
                            }
                        } else {
                            guru_empty.visibility = View.VISIBLE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        progress_bar.visibility = View.GONE
                        Toast.makeText(this@GuruActivity, "Somethings wrong", Toast.LENGTH_SHORT)
                            .show()
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
                return true
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
                val intent = Intent(this, AddGuruActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }
}