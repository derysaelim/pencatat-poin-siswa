package com.catatpelanggaran.admin.dashboard.petugas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_add_petugas.*

class AddPetugasActivity : AppCompatActivity() {

    companion object {
        const val DATA_PETUGAS = "DATAPETUGAS"
        const val NIP_PETUGAS = "NIP"
    }

    lateinit var nipPetugas: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_petugas)

        val dataPetugas = intent.getParcelableExtra<Guru>(DATA_PETUGAS)
        nipPetugas = intent.getStringExtra(NIP_PETUGAS).toString()

        if (dataPetugas != null) {
            setText(dataPetugas)
            setStatus(true)

            if (dataPetugas.nip == nipPetugas) {
                delete_button.visibility = View.GONE
            } else {
                delete_button.visibility = View.VISIBLE
            }

        } else {
            setStatus(false)
        }

        button_simpanpetugas.setOnClickListener {
            if (dataPetugas != null) {
                editData(dataPetugas)
            } else {
                insertData()
            }
        }

        back_button.setOnClickListener {
            onBackPressed()
        }

        delete_button.setOnClickListener {
            deleteData(dataPetugas)
        }

    }

    private fun setStatus(status: Boolean) {
        if (status) {
            input_nippetugas.isEnabled = false
            input_role.isEnabled = false
            button_simpanpetugas.text = "Edit"
            title_petugas.text = "Edit Petugas/Guru BK"
        }
    }

    private fun editData(dataPetugas: Guru) {
        val nip = input_nippetugas.text.toString()
        val name = input_namapetugas.text.toString()
        val nohp = input_nohppetugas.text.toString()
        val password = input_password.text.toString()
        val konfirmasi = input_konfirmasi.text.toString()
        val role = input_role.selectedItem.toString()
        val database = FirebaseDatabase.getInstance().reference
        val data = Guru(dataPetugas.nip, name, nohp)
        val dataLogin = Login(dataPetugas.nip, password, role)

        if (nip.isEmpty() || name.isEmpty() || nohp.isEmpty() || password.isEmpty() || konfirmasi.isEmpty() || konfirmasi != password) {

            if (nip.isEmpty()) {
                input_nippetugas.error = "Isi Flish"
            }
            if (name.isEmpty()) {
                input_namapetugas.error = "Isi Flish"
            }
            if (nohp.isEmpty()) {
                input_nohppetugas.error = "Isi Flish"
            }
            if (password.isEmpty()) {
                input_password.error = "Isi Flish"
            }
            if (konfirmasi.isEmpty()) {
                input_konfirmasi.error = "Isi Flish"
            }
            if (konfirmasi != password) {
                input_password.error = "tidak sama"
                input_konfirmasi.error = "tidak sama"
            }

        } else {
            if (role == "Admin") {
                database.child("petugas").child(nip).setValue(data).addOnCompleteListener {
                    database.child("Login").child(nip).setValue(dataLogin)
                    Toast.makeText(
                        this@AddPetugasActivity,
                        "berhasil",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                database.child("Guru_BK").child(nip).setValue(data).addOnCompleteListener {
                    database.child("Login").child(nip).setValue(dataLogin)
                    Toast.makeText(this@AddPetugasActivity, "berhasil", Toast.LENGTH_SHORT).show()
                }
            }
            finish()
        }
    }

    private fun insertData() {
        val nip = input_nippetugas.text.toString()
        val name = input_namapetugas.text.toString()
        val nohp = input_nohppetugas.text.toString()
        val password = input_password.text.toString()
        val konfirmasi = input_konfirmasi.text.toString()
        val role = input_role.selectedItem.toString()
        val database = FirebaseDatabase.getInstance().reference
        val data = Guru(nip, name, nohp)
        val dataLogin = Login(nip, password, role)

        if (nip.isEmpty() || name.isEmpty() || nohp.isEmpty() || password.isEmpty() || konfirmasi.isEmpty() || konfirmasi != password) {

            if (nip.isEmpty()) {
                input_nippetugas.error = "Isi Flish"
            }
            if (name.isEmpty()) {
                input_namapetugas.error = "Isi Flish"
            }
            if (nohp.isEmpty()) {
                input_nohppetugas.error = "Isi Flish"
            }
            if (password.isEmpty()) {
                input_password.error = "Isi Flish"
            }
            if (konfirmasi.isEmpty()) {
                input_konfirmasi.error = "Isi Flish"
            }
            if (konfirmasi != password) {
                input_password.error = "tidak sama"
                input_konfirmasi.error = "tidak sama"
            }

        } else {
            if (role == "Admin") {
                database.child("petugas").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(nip).exists()) {
                            Toast.makeText(this@AddPetugasActivity, "Sudah ada", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            database.child("petugas").child(nip).setValue(data)
                                .addOnCompleteListener {
                                    database.child("Login").child(nip).setValue(dataLogin)
                                    Toast.makeText(
                                        this@AddPetugasActivity,
                                        "berhasil",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@AddPetugasActivity,
                            "Something Wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
            } else {
                database.child("Guru_BK")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.child(nip).exists()) {
                                Toast.makeText(
                                    this@AddPetugasActivity,
                                    "Sudah ada",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                database.child("Guru_BK").child(nip).setValue(data)
                                    .addOnCompleteListener {
                                        database.child("Login").child(nip).setValue(dataLogin)
                                        Toast.makeText(
                                            this@AddPetugasActivity,
                                            "berhasil",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@AddPetugasActivity,
                                "Something Wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
            }
            finish()
        }
    }

    private fun deleteData(dataPetugas: Guru?) {
        val database = FirebaseDatabase.getInstance().reference
        var role = ""
        var child = ""

        database.child("Login").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                role = snapshot.child(dataPetugas!!.nip!!).child("role").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        if (role == "Admin") {
            child = "petugas"
        } else {
            child = "Guru_BK"
        }

        val builderdelete = AlertDialog.Builder(this@AddPetugasActivity)
        builderdelete.setTitle("Warning!")
        builderdelete.setMessage("Are you sure want to delete ${dataPetugas!!.nama} ?")
        builderdelete.setPositiveButton("Delete") { i, _ ->
            database.child(child).child(dataPetugas.nip!!).removeValue()
                .addOnCompleteListener {
                    database.child("Login").child(dataPetugas.nip).removeValue()
                        .addOnCompleteListener {
                            Toast.makeText(
                                this@AddPetugasActivity,
                                "Berhasil dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                }
        }
        builderdelete.setNegativeButton("Cancel") { i, _ ->
            Toast.makeText(
                applicationContext,
                "Data tidak jadi dihapus",
                Toast.LENGTH_LONG
            ).show()
        }
        val dialogdelete = builderdelete.create()
        dialogdelete.show()
    }

    private fun setText(dataPetugas: Guru) {
        dataPetugas?.let {
            val database = FirebaseDatabase.getInstance().reference
            database.child("Login").child(dataPetugas.nip!!).addListenerForSingleValueEvent(object :
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
                    Toast.makeText(this@AddPetugasActivity, "something wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            })
            input_nippetugas.setText(dataPetugas.nip)
            input_namapetugas.setText(dataPetugas.nama)
            input_nohppetugas.setText(dataPetugas.nohp)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}