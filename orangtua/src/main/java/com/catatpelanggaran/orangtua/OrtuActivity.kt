package com.catatpelanggaran.orangtua

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.adriandery.catatpelanggaran.LoginActivity
import com.adriandery.catatpelanggaran.SharedPreferences
import com.catatpelanggaran.orangtua.dashboard.DashboardFragment
import com.catatpelanggaran.orangtua.profile.EditFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_ortu.*
import kotlinx.android.synthetic.main.app_bar_main.*

class OrtuActivity : AppCompatActivity() {

    lateinit var nis: String
    private val homeFrag = DashboardFragment()
    private val profileFrag = EditFragment()
    private val fragmentManager = supportFragmentManager
    var fragment: Fragment = homeFrag
    var fragmentName = "home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ortu)
        setSupportActionBar(toolbar)

        nis = intent.getStringExtra("NIS").toString()
        getData(nis)

        fragmentManager.beginTransaction().add(R.id.frame_layout_ortu, profileFrag, "2")
            .hide(profileFrag).commit()
        fragmentManager.beginTransaction().add(R.id.frame_layout_ortu, homeFrag, "1").commit()

        ortu_bottom_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    fragmentName = "home"
                    fragmentManager.beginTransaction().hide(fragment).show(homeFrag).commit()
                    fragment = homeFrag
                    setting_ortu.visibility = View.GONE
                }
                R.id.profile -> {
                    fragmentName = "profile"
                    fragmentManager.beginTransaction().hide(fragment).show(profileFrag).commit()
                    fragment = profileFrag
                    setting_ortu.visibility = View.VISIBLE
                }
            }
            true
        }

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
                } else {
                    val intent = Intent(this@OrtuActivity, LoginActivity::class.java)
                    startActivity(intent)
                    SharedPreferences.clearData(this@OrtuActivity)
                    finish()
                }
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}