package com.catatpelanggaran.orangtua.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catatpelanggaran.orangtua.R
import com.catatpelanggaran.orangtua.model.Login
import com.catatpelanggaran.orangtua.model.Ortu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_edit.*

class EditFragment : Fragment() {

    lateinit var nis: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nis = activity?.intent?.getStringExtra("NIS").toString()
        getData(nis)

        button_editortu.setOnClickListener { editData(nis) }
    }

    private fun getData(nis: String) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("Orang_Tua").child(nis).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                input_nis.setText(snapshot.child("nis").value.toString())
                input_nama.setText(snapshot.child("nama").value.toString())
                input_nohp.setText(snapshot.child("nohp").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        database.child("Login").child(nis).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                input_password.setText(snapshot.child("password").value.toString())
                input_konfirmasi.setText(snapshot.child("password").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun editData(nis: String) {
        val nama = input_nama.text.toString()
        val nohp = input_nohp.text.toString()
        val password = input_password.text.toString()
        val konfirmasi = input_konfirmasi.text.toString()
        val role = "Orang_Tua"
        val database = FirebaseDatabase.getInstance().reference

        if (nama.isEmpty() || nohp.isEmpty() || password.isEmpty() || konfirmasi != password) {
            if (nama.isEmpty()) {
                input_nama.error = "Mohon diisi"
            }
            if (nohp.isEmpty()) {
                input_nohp.error = "Mohon diisi"
            }
            if (password.isEmpty()) {
                input_password.error = "Mohon diisi"
            }
            if (konfirmasi.isEmpty()) {
                input_konfirmasi.error = "Mohon diisi"
            }
            if (konfirmasi != password) {
                input_password.error = "Tidak sama"
                input_konfirmasi.error = "Tidak sama"
            }
        } else {
            val dataOrtu = Ortu(nis, nama, nohp)
            val dataLogin = Login(nis, password, role)

            database.child("Orang_Tua").child(nis).setValue(dataOrtu).addOnCompleteListener {
                database.child("Login").child(nis).setValue(dataLogin)
            }
        }
    }
}