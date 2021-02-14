package com.catatpelanggaran.admin.dashboard.petugas

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.adapter.AdapterGuru
import com.catatpelanggaran.admin.model.Guru
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_petugas.*
import kotlinx.android.synthetic.main.activity_petugas.back_button
import kotlinx.android.synthetic.main.activity_petugas.progress_bar
import kotlin.math.log

class PetugasActivity : AppCompatActivity() {

    companion object {
        const val NIP_PETUGAS = "nippetugas"
    }

    var listPetugas: ArrayList<Guru>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView
    lateinit var nip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petugas)
        setSupportActionBar(toolbar_petugas)

        back_button.setOnClickListener { finish() }

        nip = intent.getStringExtra(NIP_PETUGAS).toString()

        getData(null)
    }

    private fun getData(query: String?) {
        progress_bar.visibility = View.VISIBLE
        list_petugas.layoutManager = LinearLayoutManager(this)
        list_petugas.hasFixedSize()
        val database = FirebaseDatabase.getInstance().reference

        if (query != null) {
            listPetugas = arrayListOf<Guru>()
            database.child("petugas").orderByChild("nama").startAt(query).endAt(query + "\uf8ff")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listPetugas!!.clear()
                            for (i in snapshot.children) {
                                val petugas = i.getValue(Guru::class.java)
                                listPetugas!!.add(petugas!!)
                            }
                            val adapter = AdapterGuru(listPetugas!!)
                            list_petugas.adapter = adapter
                            progress_bar.visibility = View.GONE
                            petugas_empty.visibility = View.GONE
                            list_petugas.visibility = View.VISIBLE

                            adapter.onItemClick = {
                                val intent =
                                    Intent(this@PetugasActivity, AddPetugasActivity::class.java)
                                intent.putExtra(AddPetugasActivity.DATA_PETUGAS, it)
                                intent.putExtra(AddPetugasActivity.NIP_PETUGAS, nip)
                                startActivity(intent)
                            }
                            adapter.onItemDeleteClick = { selectedPetugas ->

                                if (selectedPetugas.nip == nip) {
                                    val builderdelete = AlertDialog.Builder(this@PetugasActivity)
                                    builderdelete.setTitle("Warning!")
                                    builderdelete.setMessage("You cant delete your self ${selectedPetugas.nama} ")
                                    builderdelete.setPositiveButton("OK") { _, _ -> }
                                    val dialogdelete = builderdelete.create()
                                    dialogdelete.show()
                                } else {
                                    val builderdelete = AlertDialog.Builder(this@PetugasActivity)
                                    builderdelete.setTitle("Warning!")
                                    builderdelete.setMessage("Are you sure want to delete ${selectedPetugas.nama} ?")
                                    builderdelete.setPositiveButton("Delete") { i, _ ->
                                        database.child("petugas").child(selectedPetugas.nip!!)
                                            .removeValue()
                                            .addOnCompleteListener {
                                                database.child("Login").child(selectedPetugas.nip)
                                                    .removeValue().addOnCompleteListener {
                                                        Toast.makeText(
                                                            this@PetugasActivity,
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

                            }

                        } else {
                            petugas_empty.visibility = View.VISIBLE
                            list_petugas.visibility = View.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        } else {
            listPetugas = arrayListOf<Guru>()
            database.child("petugas").orderByChild("nama")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listPetugas!!.clear()
                            for (i in snapshot.children) {
                                val petugas = i.getValue(Guru::class.java)
                                listPetugas!!.add(petugas!!)
                            }
                            val adapter = AdapterGuru(listPetugas!!)
                            list_petugas.adapter = adapter
                            progress_bar.visibility = View.GONE
                            petugas_empty.visibility = View.GONE
                            list_petugas.visibility = View.VISIBLE

                            adapter.onItemClick = {
                                val intent =
                                    Intent(this@PetugasActivity, AddPetugasActivity::class.java)
                                intent.putExtra(AddPetugasActivity.DATA_PETUGAS, it)
                                intent.putExtra(AddPetugasActivity.NIP_PETUGAS, nip)
                                startActivity(intent)
                            }
                            adapter.onItemDeleteClick = { selectedPetugas ->

                                if (selectedPetugas.nip == nip) {
                                    val builderdelete = AlertDialog.Builder(this@PetugasActivity)
                                    builderdelete.setTitle("Warning!")
                                    builderdelete.setMessage("You cant delete your self ${selectedPetugas.nama} ")
                                    builderdelete.setPositiveButton("OK") { _, _ -> }
                                    val dialogdelete = builderdelete.create()
                                    dialogdelete.show()
                                } else {
                                    val builderdelete = AlertDialog.Builder(this@PetugasActivity)
                                    builderdelete.setTitle("Warning!")
                                    builderdelete.setMessage("Are you sure want to delete ${selectedPetugas.nama} ?")
                                    builderdelete.setPositiveButton("Delete") { i, _ ->
                                        database.child("petugas").child(selectedPetugas.nip!!)
                                            .removeValue()
                                            .addOnCompleteListener {
                                                database.child("Login").child(selectedPetugas.nip)
                                                    .removeValue().addOnCompleteListener {
                                                        Toast.makeText(
                                                            this@PetugasActivity,
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

                            }

                        } else {
                            petugas_empty.visibility = View.VISIBLE
                            list_petugas.visibility = View.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
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