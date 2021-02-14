package com.catatpelanggaran.orangtua

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.adriandery.catatpelanggaran.LoginActivity
import com.adriandery.catatpelanggaran.SharedPreferences
import com.catatpelanggaran.orangtua.dashboard.DashboardFragment
import com.catatpelanggaran.orangtua.profile.EditFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_ortu.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class OrtuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var nis: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ortu)
        setSupportActionBar(toolbar)

        nis = intent.getStringExtra("NIS").toString()
        getData(nis)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, DashboardFragment())
                .commit()
            supportActionBar?.title = "Dashboard"
        }

    }

    override fun onResume() {
        super.onResume()
        getData(nis)
    }

    private fun getData(nis: String) {
        val database = FirebaseDatabase.getInstance().reference

        database.child("Orang_Tua").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OrtuActivity, "Error", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(nis).exists()) {
                    val nama = snapshot.child(nis).child("nama").value.toString()
                    nama_orangtua.text = nama
                    nama_ortu.text = nama
                    nis_anak.text = nis
                } else {
                    val intent = Intent(this@OrtuActivity, LoginActivity::class.java)
                    startActivity(intent)
                    SharedPreferences.clearData(this@OrtuActivity)
                    finish()
                }
            }

        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        var title = "Dashboard"
        when (item.itemId) {
            R.id.nav_dashboard -> {
                fragment = DashboardFragment()
                title = "Dashbpard"
            }
            R.id.nav_edit -> {
                fragment = EditFragment()
                title = "Edit"
            }
            R.id.nav_logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                SharedPreferences.clearData(this)
                finish()
            }
        }
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit()
        }
        supportActionBar?.title = title

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}