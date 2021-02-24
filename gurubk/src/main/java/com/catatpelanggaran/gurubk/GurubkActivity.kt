package com.catatpelanggaran.gurubk

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.adriandery.catatpelanggaran.LoginActivity
import com.adriandery.catatpelanggaran.SharedPreferences
import com.catatpelanggaran.gurubk.dashboard.DashboardFragment
import com.catatpelanggaran.gurubk.profile.ProfileFragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_gurubk.*
import kotlinx.android.synthetic.main.app_bar_bk.*
import kotlinx.android.synthetic.main.nav_header_bk.*

class GurubkActivity : AppCompatActivity() {

    lateinit var nip: String
    lateinit var guruPopUp: Dialog
    lateinit var buttonCancelGuru: MaterialButton
    lateinit var buttonExitGuru: MaterialButton

    private val homeFrag = DashboardFragment()
    private val profileFrag = ProfileFragment()
    private val fragmentManager = supportFragmentManager
    var fragment: Fragment = homeFrag
    var fragmentName = "home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gurubk)
        setSupportActionBar(toolbar)

        guruPopUp = Dialog(this)

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
                    setting_guru.setOnClickListener { createExitPopUp() }
                }
            }
            true
        }
    }

    private fun createExitPopUp() {
        guruPopUp.setContentView(R.layout.popup_guru)
        buttonCancelGuru = guruPopUp.findViewById(R.id.cancel_guru)
        buttonExitGuru = guruPopUp.findViewById(R.id.exit_guru)

        buttonCancelGuru.setOnClickListener {
            guruPopUp.dismiss()
        }
        buttonExitGuru.setOnClickListener {
            guruPopUp.dismiss()
            val intent = Intent(this@GurubkActivity, LoginActivity::class.java)
            startActivity(intent)
            SharedPreferences.clearData(this@GurubkActivity)
            finish()
        }

        guruPopUp.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        guruPopUp.show()
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