package com.catatpelanggaran.orangtua.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.catatpelanggaran.orangtua.R
import com.catatpelanggaran.orangtua.dashboard.pelanggaran.PelanggaranActivity
import com.catatpelanggaran.orangtua.dashboard.penghargaan.PenghargaanActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment(), View.OnClickListener {

    lateinit var nis: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nis = activity?.intent?.getStringExtra("NIS").toString()

        button_pelanggaran.setOnClickListener(this)
        button_penghargaan.setOnClickListener(this)
        button_data_pelanggaran.setOnClickListener(this)
        button_data_penghargaan.setOnClickListener(this)

        getData(nis)
    }

    private fun getData(nis: String) {
        val database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nama = snapshot.child("Siswa").child(nis).child("nama_siswa").value.toString()
                val kelas = snapshot.child("Siswa").child(nis).child("id_kelas").value.toString()
                val tingkat = snapshot.child("kelas").child(kelas).child("tingkat").value.toString()
                val jurusan = snapshot.child("kelas").child(kelas).child("jurusan").value.toString()
                val rombel = snapshot.child("kelas").child(kelas).child("kelas").value.toString()
                nama_siswa.text = nama
                nis_siswa.text = "$tingkat $jurusan $rombel"
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    override fun onClick(view: View) {

        lateinit var pindah: Intent

        when (view.id) {
            R.id.button_pelanggaran -> {
                pindah = Intent(context, PelanggaranActivity::class.java)
                pindah.putExtra(PelanggaranActivity.NIS_SISWA, nis)
                pindah.putExtra(PelanggaranActivity.DATA_ACTIVITY, "siswa")
            }
            R.id.button_penghargaan -> {
                pindah = Intent(context, PenghargaanActivity::class.java)
                pindah.putExtra(PenghargaanActivity.NIS_SISWA, nis)
            }
            R.id.button_data_pelanggaran -> {
                pindah = Intent(context, PelanggaranActivity::class.java)
                pindah.putExtra(PelanggaranActivity.NIS_SISWA, nis)
                pindah.putExtra(PelanggaranActivity.DATA_ACTIVITY, "data")
            }
            R.id.button_data_penghargaan -> {
                pindah = Intent(context, PenghargaanActivity::class.java)
                pindah.putExtra(PenghargaanActivity.NIS_SISWA, nis)
            }
        }

        startActivity(pindah)
    }
}