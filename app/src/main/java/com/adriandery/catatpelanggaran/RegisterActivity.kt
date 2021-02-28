package com.adriandery.catatpelanggaran

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adriandery.catatpelanggaran.model.Login
import com.adriandery.catatpelanggaran.model.Ortu
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    lateinit var popUpMMain: Dialog
    lateinit var okButtton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        popUpMMain = Dialog(this)

        button_daftar.setOnClickListener {
            insertData()
        }

        back_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun insertData() {
        val database = FirebaseDatabase.getInstance().reference
        val nis = input_nis.text.toString()
        val nama = input_nama.text.toString()
        val nohp = input_nohp.text.toString()
        val password = input_password.text.toString()
        val konfirmasi = input_konfirmasi.text.toString()

        val data = Ortu(nis, nama, nohp)
        val dataLogin = Login(nis, password)

        if (nis.isEmpty() || nama.isEmpty() || nohp.isEmpty() || password.isEmpty() || konfirmasi.isEmpty()) {
            if (nis.isEmpty()) {
                input_nis.error = "Mohon Diisi"
            }
            if (nama.isEmpty()) {
                input_nama.error = "Mohon Diisi"
            }
            if (nohp.isEmpty()) {
                input_nohp.error = "Mohon Diisi"
            }
            if (password.isEmpty()) {
                input_password.error = "Mohon Diisi"
            }
            if (konfirmasi.isEmpty()) {
                input_konfirmasi.error = "Mohon Diisi"
            }
            if (konfirmasi != password) {
                input_password.error = "tidak sama"
                input_konfirmasi.error = "tidak sama"
            }
        }
        else {
            database.child("Siswa").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(nis).exists()) {
                        database.child("Orang_Tua")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.child(nis).exists()) {
                                        Toast.makeText(
                                            this@RegisterActivity, "Sudah ada", Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        database.child("Orang_Tua").child(nis).setValue(data)
                                            .addOnCompleteListener {
                                                database.child("Login").child(nis)
                                                    .setValue(dataLogin)
                                                createPopUpRegister()
                                            }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(
                                        this@RegisterActivity, "Something Wrong", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    } else {
                        Toast.makeText(
                            this@RegisterActivity, "NIS Tidak Terdaftar", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun createPopUpRegister() {
        popUpMMain.setContentView(R.layout.popup_main)
        okButtton = popUpMMain.findViewById(R.id.ok_main)
        popUpMMain.setOnCancelListener { finish() }

        okButtton.setOnClickListener {
            popUpMMain.dismiss()
            finish()
        }

        popUpMMain.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popUpMMain.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}