package com.catatpelanggaran.admin.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catatpelanggaran.admin.R
import com.catatpelanggaran.admin.model.Guru
import com.catatpelanggaran.admin.model.Login
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_petugas.*
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_edit.input_konfirmasi
import kotlinx.android.synthetic.main.fragment_edit.input_namapetugas
import kotlinx.android.synthetic.main.fragment_edit.input_nippetugas
import kotlinx.android.synthetic.main.fragment_edit.input_nohppetugas
import kotlinx.android.synthetic.main.fragment_edit.input_password

class EditFragment : Fragment() {

    lateinit var nip: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nip = activity?.intent?.getStringExtra("NIP").toString()
        getData(nip)

        button_editpetugas.setOnClickListener {
            editData()
        }
    }

    private fun editData() {
        val nip = input_nippetugas.text.toString()
        val nama = input_namapetugas.text.toString()
        val nohp = input_nohppetugas.text.toString()
        val password = input_password.text.toString()
        val konfirmasi = input_konfirmasi.text.toString()
        val role = "Admin"
        val database = FirebaseDatabase.getInstance().reference

        if (nip.isEmpty() || nama.isEmpty() || nohp.isEmpty() || password.isEmpty() || konfirmasi.isEmpty() || konfirmasi != password) {
            if (nip.isEmpty()) {
                input_nippetugas.error = "Isi"
            }
            if (nama.isEmpty()) {
                input_namapetugas.error = "Isi"
            }
            if (nohp.isEmpty()) {
                input_nohppetugas.error = "Isi"
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
            val dataPetugas = Guru(nip, nama, nohp)
            val dataLogin = Login(nip, password, role)

            database.child("Guru").child(nip).setValue(dataPetugas).addOnCompleteListener {
                database.child("Login").child(nip).setValue(dataLogin)
            }
        }
    }

    private fun getData(nip: String) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("Guru").child(nip).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                input_nippetugas.setText(snapshot.child("nip").value.toString())
                input_namapetugas.setText(snapshot.child("nama").value.toString())
                input_nohppetugas.setText(snapshot.child("nohp").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        database.child("Login").child(nip).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                input_password.setText(snapshot.child("password").value.toString())
                input_konfirmasi.setText(snapshot.child("password").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }
}