package com.catatpelanggaran.admin

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
import com.catatpelanggaran.admin.dashboard.DashboardFragment
import com.catatpelanggaran.admin.profile.EditFragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {

    lateinit var nip: String
    lateinit var adminPopUp: Dialog
    lateinit var buttonCancelAdmin: MaterialButton
    lateinit var buttonExitAdmin: MaterialButton

    private val homeFrag = DashboardFragment()
    private val profileFrag = EditFragment()
    private val fragmentManager = supportFragmentManager
    var fragment: Fragment = homeFrag
    var fragmentName = "home"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        setSupportActionBar(toolbar)

        adminPopUp = Dialog(this)

        nip = intent.getStringExtra("NIP").toString()
        getData(nip)

        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, profileFrag, "2")
            .hide(profileFrag).commit()
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, homeFrag, "1").commit()

        bottom_nav_admin.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    fragmentName = "home"
                    fragmentManager.beginTransaction().hide(fragment).show(homeFrag).commit()
                    fragment = homeFrag
                    setting_admin.visibility = View.GONE
                }
                R.id.profile -> {
                    fragmentName = "profile"
                    fragmentManager.beginTransaction().hide(fragment).show(profileFrag).commit()
                    fragment = profileFrag
                    setting_admin.visibility = View.VISIBLE
                    setting_admin.setOnClickListener { createPopUpAdmin() }
                }
            }
            true
        }
    }

    private fun createPopUpAdmin() {
        adminPopUp.setContentView(R.layout.popup_admin)
        buttonCancelAdmin = adminPopUp.findViewById(R.id.cancel_admin)
        buttonExitAdmin = adminPopUp.findViewById(R.id.exit_admin)

        buttonCancelAdmin.setOnClickListener {
            adminPopUp.dismiss()
        }
        buttonExitAdmin.setOnClickListener {
            adminPopUp.dismiss()
            val intent = Intent(this@AdminActivity, LoginActivity::class.java)
            startActivity(intent)
            SharedPreferences.clearData(this@AdminActivity)
            finish()
        }

        adminPopUp.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        adminPopUp.show()
    }

    private fun getData(nip: String) {
        val database = FirebaseDatabase.getInstance().reference

        database.child("Guru").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(nip).exists()) {
                    val nama = snapshot.child(nip).child("nama").value.toString()
                    admin_name.text = nama
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

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        var fragment: Fragment? = null
//        var title = "Dashboard"
//        when (item.itemId) {
//            R.id.nav_dashboard -> {
//                fragment = DashboardFragment()
//                title = "Dashboard"
//            }
//            R.id.nav_edit -> {
//                fragment = EditFragment()
//                title = "Edit"
//            }
//            R.id.nav_logout -> {
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//                SharedPreferences.clearData(this)
//                finish()
//            }
//        }
//        if (fragment != null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.nav_host_fragment, fragment)
//                .commit()
//        }
//        supportActionBar?.title = title
//
//        drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}