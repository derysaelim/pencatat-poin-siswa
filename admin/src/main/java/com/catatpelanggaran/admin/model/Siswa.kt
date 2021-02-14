package com.catatpelanggaran.admin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Siswa(
    val nis: String? = null,
    val nama_siswa: String? = null,
    val id_kelas: String? = null,
    val jenkel: String? = null,
    val alamat: String? = null
) : Parcelable