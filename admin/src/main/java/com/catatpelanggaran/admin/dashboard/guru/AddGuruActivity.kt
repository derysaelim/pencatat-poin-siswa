package com.catatpelanggaran.admin.dashboard.guru

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Guru
import com.catatpelanggaran.admin.model.Login
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_guru.*
import kotlinx.android.synthetic.main.activity_add_guru.back_button
import kotlinx.android.synthetic.main.activity_add_guru.delete_button
import kotlinx.android.synthetic.main.activity_add_guru.input_konfirmasi
import kotlinx.android.synthetic.main.activity_add_guru.input_password
import kotlinx.android.synthetic.main.activity_add_guru.input_role
import java.lang.Exception

class AddGuruActivity : AppCompatActivity() {

    companion object {
        const val DATA_GURU = "DATAGURU"
        const val NIP_PETUGAS = "NIP"
    }

    lateinit var nipPetugas: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_guru)

        val dataGuru = intent.getParcelableExtra<Guru>(DATA_GURU)
        nipPetugas = intent.getStringExtra(NIP_PETUGAS).toString()

        if (dataGuru != null) {
            setText(dataGuru)
            setStatus(true)

            if (dataGuru.nip == nipPetugas) {
                delete_button.visibility = View.GONE
            } else {
                delete_button.visibility = View.VISIBLE
            }

        } else {
            setStatus(false)
        }

        button_simpanguru.setOnClickListener {
            if (dataGuru != null) {
                editData(dataGuru)
            } else {
                insertData()
            }
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        delete_button.setOnClickListener {
            deleteData(dataGuru)
        }

    }

    private fun setStatus(status: Boolean) {
        if (status) {
            input_nipguru.isEnabled = false
            input_role.isEnabled = false
            button_simpanguru.text = "Edit"
            title_guru.text = "Edit Petugas/Guru"
        }
    }

    private fun editData(dataGuru: Guru) {
        val nip = input_nipguru.text.toString()
        val name = input_namaguru.text.toString()
        val nohp = input_nohpguru.text.toString()
        val password = input_password.text.toString()
        val konfirmasi = input_konfirmasi.text.toString()
        val role = input_role.selectedItem.toString()
        val database = FirebaseDatabase.getInstance().reference
        val data = Guru(dataGuru.nip, name, nohp)
        val dataLogin = Login(dataGuru.nip, password, role)

        if (nip.isEmpty() || name.isEmpty() || nohp.isEmpty() || password.isEmpty() || konfirmasi.isEmpty() || konfirmasi != password) {

            if (nip.isEmpty()) {
                input_nipguru.error = "Isi"
            }
            if (name.isEmpty()) {
                input_namaguru.error = "Isi"
            }
            if (nohp.isEmpty()) {
                input_nohpguru.error = "Isi"
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

        } else {
            database.child("Guru").child(nip).setValue(data).addOnCompleteListener {
                database.child("Login").child(nip).setValue(dataLogin)
                Toast.makeText(this, "berhasil", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun insertData() {
        val nip = input_nipguru.text.toString()
        val name = input_namaguru.text.toString()
        val nohp = input_nohpguru.text.toString()
        val password = input_password.text.toString()
        val konfirmasi = input_konfirmasi.text.toString()
        val role = input_role.selectedItem.toString()
        val database = FirebaseDatabase.getInstance().reference
        val data = Guru(nip, name, nohp)
        val dataLogin = Login(nip, password, role)

        if (nip.isEmpty() || name.isEmpty() || nohp.isEmpty() || password.isEmpty() || konfirmasi.isEmpty() || konfirmasi != password) {

            if (nip.isEmpty()) {
                input_nipguru.error = "Isi"
            }
            if (name.isEmpty()) {
                input_namaguru.error = "Isi"
            }
            if (nohp.isEmpty()) {
                input_nohpguru.error = "Isi"
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

        } else {
            database.child("Guru")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(nip).exists()) {
                            Toast.makeText(
                                this@AddGuruActivity,
                                "Sudah ada",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            database.child("Guru").child(nip).setValue(data)
                                .addOnCompleteListener {
                                    database.child("Login").child(nip).setValue(dataLogin)
                                    Toast.makeText(
                                        this@AddGuruActivity,
                                        "berhasil",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@AddGuruActivity,
                            "Something Wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
            finish()
        }
    }

    private fun deleteData(dataGuru: Guru?) {
        val database = FirebaseDatabase.getInstance().reference
        val builderdelete = AlertDialog.Builder(this@AddGuruActivity)
        builderdelete.setTitle("Warning!")
        builderdelete.setMessage("Are you sure want to delete ${dataGuru!!.nama} ?")
        builderdelete.setPositiveButton("Delete") { _, _ ->
            database.child("Guru").child(dataGuru.nip!!).removeValue()
                .addOnCompleteListener {
                    database.child("Login").child(dataGuru.nip).removeValue()
                        .addOnCompleteListener {
                            Toast.makeText(
                                this,
                                "Berhasil dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                }
        }
        builderdelete.setNegativeButton("Cancel") { _, _ ->
            Toast.makeText(
                applicationContext,
                "Data tidak jadi dihapus",
                Toast.LENGTH_LONG
            ).show()
        }
        val dialogdelete = builderdelete.create()
        dialogdelete.show()
    }

    private fun setText(dataGuru: Guru) {
        dataGuru?.let {
            val database = FirebaseDatabase.getInstance().reference
            database.child("Login").child(dataGuru.nip!!).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        input_password.setText(snapshot.child("password").value.toString())
                        input_konfirmasi.setText(snapshot.child("password").value.toString())
                        if (snapshot.child("role").value.toString() == "Admin") {
                            input_role.setSelection(0)
                        } else {
                            input_role.setSelection(1)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AddGuruActivity, "something wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            input_nipguru.setText(dataGuru.nip)
            input_namaguru.setText(dataGuru.nama)
            input_nohpguru.setText(dataGuru.nohp)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}