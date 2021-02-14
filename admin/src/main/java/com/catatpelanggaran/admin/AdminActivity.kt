package com.catatpelanggaran.admin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.adriandery.catatpelanggaran.LoginActivity
import com.adriandery.catatpelanggaran.SharedPreferences
import com.catatpelanggaran.admin.dashboard.DashboardFragment
import com.catatpelanggaran.admin.profile.EditFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.app_bar_admin.*
import kotlinx.android.synthetic.main.nav_header_admin.*

class AdminActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var nip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        setSupportActionBar(toolbar)

        nip = intent.getStringExtra("NIP").toString()
        getData(nip)

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
                .replace(R.id.nav_host_fragment, DashboardFragment())
                .commit()
            supportActionBar?.title = "Dashboard"
        }
    }

    private fun getData(nip: String) {
        val database = FirebaseDatabase.getInstance().reference

        database.child("Guru").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(nip).exists()) {
                    val nama = snapshot.child(nip).child("nama").value.toString()
                    admin_name.text = nama
                    admin_name_nav.text = nama
                    admin_nip.text = nip
                } else {
                    val intent = Intent(this@AdminActivity, LoginActivity::class.java)
                    startActivity(intent)
                    SharedPreferences.clearData(this@AdminActivity)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminActivity, "Error", Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        var title = "Dashboard"
        when (item.itemId) {
            R.id.nav_dashboard -> {
                fragment = DashboardFragment()
                title = "Dashboard"
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
                .replace(R.id.nav_host_fragment, fragment)
                .commit()
        }
        supportActionBar?.title = title

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}