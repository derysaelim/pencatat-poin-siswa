package com.adriandery.catatpelanggaran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.adriandery.catatpelanggaran.model.Login
import com.adriandery.catatpelanggaran.model.Ortu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        const val DATA_ORTU = "dataOrtu"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var dataOrtu = intent.getParcelableExtra<Ortu>(DATA_ORTU)

        button_daftar.setOnClickListener {
            if (dataOrtu != null) {

            } else {
                insertData()
            }
        }

        back_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
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
                input_nis.error = "Isi"
            }
            if (nama.isEmpty()) {
                input_nama.error = "Isi"
            }
            if (nohp.isEmpty()) {
                input_nohp.error = "Isi"
            }
            if (password.isEmpty()) {
                input_password.error = "Isi"
            }
            if (konfirmasi.isEmpty()) {
                input_konfirmasi.error = "Isi"
            }
            if (konfirmasi != password) {
                input_password.error = "tidak sama"
                input_konfirmasi.error = "tidak sama"
            }
        }
        else {
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
                                    database.child("Login").child(nis).setValue(dataLogin)
                                    Toast.makeText(
                                        this@RegisterActivity, "Berhasil", Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@RegisterActivity, "Something Wrong", Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}