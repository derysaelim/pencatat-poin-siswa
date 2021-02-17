package com.catatpelanggaran.gurubk.dashboard.datapelanggar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.catatpelanggaran.gurubk.R

class DetailPelanggarActivity : AppCompatActivity() {

    companion object {
        const val DATA_NIS = "DATA_NIS"
        const val DATA_SISWA = "DATA_SISWA"
    }

    lateinit var nis: String
    lateinit var data: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pelanggar)

        nis = intent.getStringExtra(DATA_NIS).toString()
        data = intent.getStringExtra(DATA_SISWA.toString()).toString()

    }
}