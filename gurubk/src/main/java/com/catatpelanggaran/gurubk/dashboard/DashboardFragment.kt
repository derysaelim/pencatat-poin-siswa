package com.catatpelanggaran.gurubk.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.dashboard.catat.KelasActivity
import com.catatpelanggaran.gurubk.dashboard.datapelanggar.DataPelanggarActivity
import com.catatpelanggaran.gurubk.dashboard.pelanggaran.PelanggaranActivity
import com.catatpelanggaran.gurubk.dashboard.penghargaan.PenghargaanActivity
import kotlinx.android.synthetic.main.fragment_dashboard_guru.*

class DashboardFragment : Fragment(), View.OnClickListener {

    lateinit var nip: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard_guru, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catat_pelanggaran.setOnClickListener(this)
        catat_penghargaan.setOnClickListener(this)
        data_pelanggar.setOnClickListener(this)
        data_penghargaan.setOnClickListener(this)
        list_pelanggaran.setOnClickListener(this)
        list_penghargaan.setOnClickListener(this)

        nip = activity?.intent?.getStringExtra("NIP").toString()
    }

    override fun onClick(view: View) {
        lateinit var intent: Intent

        when (view.id) {
            R.id.catat_pelanggaran -> {
                intent = Intent(context, KelasActivity::class.java)
                intent.putExtra(KelasActivity.DATA_SISWA, "langgar")
            }
            R.id.catat_penghargaan -> {
                intent = Intent(context, KelasActivity::class.java)
                intent.putExtra(KelasActivity.DATA_SISWA, "reward")
            }
            R.id.list_pelanggaran -> {
                intent = Intent(context, PelanggaranActivity::class.java)
            }
            R.id.list_penghargaan -> {
                intent = Intent(context, PenghargaanActivity::class.java)
            }
            R.id.data_pelanggar -> {
                intent = Intent(context, DataPelanggarActivity::class.java)
                intent.putExtra(DataPelanggarActivity.DATA_SISWA, "langgar")
            }
            R.id.data_penghargaan -> {
                intent = Intent(context, DataPelanggarActivity::class.java)
                intent.putExtra(DataPelanggarActivity.DATA_SISWA, "reward")
            }
        }
        startActivity(intent)

    }
}