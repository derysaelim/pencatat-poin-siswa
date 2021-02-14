package com.adriandery.catatpelanggaran

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button.setOnClickListener {

            val nip = nip_login.text.toString()
            val password = password_login.text.toString()

            if (nip.isEmpty() || password.isEmpty()) {
                if (nip.isEmpty()) {
                    nip_login.error = "Mohon isi"
                }

                if (password.isEmpty()) {
                    password_login.error = "Mohon isi"
                }
            } else {

                val databaseReference: DatabaseReference =
                    FirebaseDatabase.getInstance().reference
                databaseReference.child("Login")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
//                        cek NIP didatabase
                            if (snapshot.child(nip).exists()) {
//                        cek password didatabase
                                if (snapshot.child(nip).child("password")
                                        .getValue(String::class.java).equals(password)
                                ) {
//                            cek apakah admin
                                    if (snapshot.child(nip).child("role")
                                            .getValue(String::class.java).equals("Admin")
                                    ) {

//                                set user sudah login
                                        SharedPreferences.setDataLogin(this@LoginActivity, true)

//                                set login sebagai admin
                                        SharedPreferences.setDataAs(this@LoginActivity, "admin")
                                        SharedPreferences.setNIP(this@LoginActivity, nip)
                                        goToModule("admin", nip)
                                        finish()
                                    } else if (snapshot.child(nip).child("role")
                                            .getValue(String::class.java).equals("Guru")
                                    ) {

//                                set user sudah login
                                        SharedPreferences.setDataLogin(this@LoginActivity, true)

//                                set login sebagai guru
                                        SharedPreferences.setDataAs(this@LoginActivity, "gurubk")
                                        SharedPreferences.setNIP(this@LoginActivity, nip)
                                        goToModule("gurubk", nip)
                                        finish()
                                    } else {

//                                set user sudah login
                                        SharedPreferences.setDataLogin(this@LoginActivity, true)

//                                set login sebagai orang tua
                                        SharedPreferences.setDataAs(this@LoginActivity, "orangtua")
                                        SharedPreferences.setNIP(this@LoginActivity, nip)
                                        goToModule("orangtua", nip)
                                        finish()
                                    }
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Password salah",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Belum terdaftar",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@LoginActivity, "Belum terdaftar", Toast.LENGTH_LONG)
                                .show()
                        }

                    })

            }
        }

        button_daftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onStart() {
        super.onStart()

//        akan dieksekusi apabila user sudah login dan tidak melakukan logout

        val nip = SharedPreferences.getNIP(this).toString()

        if (SharedPreferences.getDataLogin(this)) {
            if (SharedPreferences.getDataAs(this).equals("admin")) {
                goToModule("admin", nip)
                finish()
            } else if (SharedPreferences.getDataAs(this).equals("gurubk")) {
                goToModule("gurubk", nip)
                finish()
            } else if (SharedPreferences.getDataAs(this).equals("orangtua")) {
                goToModule("orangtua", nip)
                finish()
            }
        }
    }

    private fun goToModule(moduleName: String, nip: String) {
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        if (splitInstallManager.installedModules.contains(moduleName)) {

            if (moduleName == "admin") {
                val intent = Intent(this, Class.forName("com.catatpelanggaran.admin.AdminActivity"))
                intent.putExtra("NIP", nip)
                startActivity(intent)
            } else if (moduleName == "gurubk") {
                val intent =
                    Intent(this, Class.forName("com.catatpelanggaran.gurubk.GurubkActivity"))
                intent.putExtra("NIP", nip)
                startActivity(intent)
            } else if (moduleName == "orangtua") {
                val intent = Intent(this, Class.forName("com.catatpelanggaran.orangtua.OrtuActivity"))
                intent.putExtra("NIS", nip)
                startActivity(intent)
            }
        }
    }
}