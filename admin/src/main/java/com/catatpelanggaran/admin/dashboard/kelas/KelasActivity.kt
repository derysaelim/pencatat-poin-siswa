package com.catatpelanggaran.admin.dashboard.kelas

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
import com.catatpelanggaran.admin.adapter.AdapterKelas
import com.catatpelanggaran.admin.dashboard.guru.AddGuruActivity
import com.catatpelanggaran.admin.model.Kelas
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_kelas.*

class KelasActivity : AppCompatActivity() {

    var listKelas: ArrayList<Kelas>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelas)
        setSupportActionBar(toolbar_kelas)

        back_button.setOnClickListener { finish() }

        getData(null)
    }

    private fun getData(query: String?) {
        progress_bar.visibility = View.VISIBLE
        list_kelas.layoutManager = LinearLayoutManager(this)
        list_kelas.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null) {
            val search = query.replace("\\s".toRegex(), "")
            listKelas = arrayListOf<Kelas>()
            database.child("kelas").orderByChild("idKelas").startAt(search).endAt(search + "\uf8ff")
                .addValueEventListener(object : ValueEventListener {
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
                                val intent =
                                    Intent(this@KelasActivity, AddKelasActivity::class.java)
                                intent.putExtra(AddKelasActivity.DATA_KELAS, selectedKelas)
                                startActivity(intent)
                            }

                            adapter.onItemDeleteClick = { selectedKelas ->
                                val builderdelete = AlertDialog.Builder(this@KelasActivity)
                                database.child("daftar_kelas")
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.child(selectedKelas.idKelas!!).exists()) {
                                                builderdelete.setTitle("Warning!")
                                                builderdelete.setMessage("Maaf kelas ini ada muridnya")
                                                builderdelete.setPositiveButton("OK") { i, _ -> }
                                            } else {
                                                builderdelete.setTitle("Warning!")
                                                builderdelete.setMessage("Are you sure want to delete ${selectedKelas.tingkat} ${selectedKelas.jurusan} ${selectedKelas.kelas} ?")
                                                builderdelete.setPositiveButton("Delete") { i, _ ->
                                                    database.child("kelas")
                                                        .child(selectedKelas.idKelas).removeValue()
                                                        .addOnCompleteListener {
                                                            database.child("walikelas")
                                                                .child(selectedKelas.nip!!)
                                                                .removeValue()
                                                                .addOnCompleteListener {
                                                                    Toast.makeText(
                                                                        this@KelasActivity,
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
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(
                                                this@KelasActivity,
                                                "Somethings wrong",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                                val dialogdelete = builderdelete.create()
                                dialogdelete.show()
                            }

                        } else {
                            kelas_empty.visibility = View.VISIBLE
                            list_kelas.visibility = View.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@KelasActivity, "Somethings wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                })
        } else {
            listKelas = arrayListOf<Kelas>()
            database.child("kelas").orderByChild("idKelas")
                .addValueEventListener(object : ValueEventListener {
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
                                val intent =
                                    Intent(this@KelasActivity, AddKelasActivity::class.java)
                                intent.putExtra(AddKelasActivity.DATA_KELAS, selectedKelas)
                                startActivity(intent)
                            }

                            adapter.onItemDeleteClick = { selectedKelas ->
                                val builderdelete = AlertDialog.Builder(this@KelasActivity)
                                database.child("daftar_kelas")
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.child(selectedKelas.idKelas!!).exists()) {
                                                builderdelete.setTitle("Warning!")
                                                builderdelete.setMessage("Maaf kelas ini ada muridnya")
                                                builderdelete.setPositiveButton("OK") { i, _ -> }
                                            } else {
                                                builderdelete.setTitle("Warning!")
                                                builderdelete.setMessage("Are you sure want to delete ${selectedKelas.tingkat} ${selectedKelas.jurusan} ${selectedKelas.kelas} ?")
                                                builderdelete.setPositiveButton("Delete") { i, _ ->
                                                    database.child("kelas")
                                                        .child(selectedKelas.idKelas).removeValue()
                                                        .addOnCompleteListener {
                                                            database.child("walikelas")
                                                                .child(selectedKelas.nip!!)
                                                                .removeValue()
                                                                .addOnCompleteListener {
                                                                    Toast.makeText(
                                                                        this@KelasActivity,
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
                                            }
                                            val dialogdelete = builderdelete.create()
                                            dialogdelete.show()
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(
                                                this@KelasActivity,
                                                "Somethings wrong",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                            }

                        } else {
                            kelas_empty.visibility = View.VISIBLE
                            list_kelas.visibility = View.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@KelasActivity, "Somethings wrong", Toast.LENGTH_SHORT)
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
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                getData(query)
                return true
            }

        })
        return true
    }

    override fun onResume() {
        super.onResume()
        getData(null)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_button -> {
                val intent = Intent(this, AddKelasActivity::class.java)
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