package com.catatpelanggaran.gurubk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Siswa(
    val id_kelas: String? = null,
    val nis: String? = null,
    val nama_siswa: String? = null,
    val jenkel: String? = null,
    val alamat: String? = null,
    val telp_ortu: String? = null,

    val idKelas: String? = null,
    val tingkat: String? = null,
    val jurusan: String? = null,
    val kelas: String? = null,
    val nip: String? = null
) : Parcelable