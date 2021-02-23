package com.catatpelanggaran.gurubk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.adriandery.catatpelanggaran.LoginActivity
import com.adriandery.catatpelanggaran.SharedPreferences
import com.catatpelanggaran.gurubk.dashboard.DashboardFragment
import com.catatpelanggaran.gurubk.profile.ProfileFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_gurubk.*
import kotlinx.android.synthetic.main.app_bar_bk.*
import kotlinx.android.synthetic.main.nav_header_bk.*

class GurubkActivity : AppCompatActivity() {

    lateinit var nip: String
    private val homeFrag = DashboardFragment()
    private val profileFrag = ProfileFragment()
    private val fragmentManager = supportFragmentManager
    var fragment: Fragment = homeFrag
    var fragmentName = "home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gurubk)
        setSupportActionBar(toolbar)

        nip = intent.getStringExtra("NIP").toString()
        getData(nip)

        fragmentManager.beginTransaction().add(R.id.nav_host_fragment_guru, profileFrag, "2")
            .hide(profileFrag).commit()
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment_guru, homeFrag, "1").commit()

        bottom_nav_guru.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    fragmentName = "home"
                    fragmentManager.beginTransaction().hide(fragment).show(homeFrag).commit()
                    fragment = homeFrag
                    setting_guru.visibility = View.GONE
                }
                R.id.profile -> {
                    fragmentName = "profile"
                    fragmentManager.beginTransaction().hide(fragment).show(profileFrag).commit()
                    fragment = profileFrag
                    setting_guru.visibility = View.VISIBLE
                }
            }
            true
        }

    }

    private fun getData(nip: String) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("Guru").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(nip).exists()) {
                    val nama = snapshot.child(nip).child("nama").value.toString()
                    gurubk_name.text = nama
                } else {
                    val intent = Intent(this@GurubkActivity, LoginActivity::class.java)
                    startActivity(intent)
                    SharedPreferences.clearData(this@GurubkActivity)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@GurubkActivity, "Error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}