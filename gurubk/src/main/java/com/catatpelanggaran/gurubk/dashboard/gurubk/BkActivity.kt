package com.catatpelanggaran.gurubk.dashboard.gurubk

import android.app.SearchManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.catatpelanggaran.gurubk.R
import com.catatpelanggaran.gurubk.model.Guru
import kotlinx.android.synthetic.main.activity_bk.*

class BkActivity : AppCompatActivity() {

    companion object {
        const val NIP_GURUBK = "nipgurubk"
    }

    var listGuruBK: ArrayList<Guru>? = null

    lateinit var searchManager: SearchManager
    lateinit var searchView: SearchView
    lateinit var nip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bk)
        setSupportActionBar(toolbar_gurubk)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}