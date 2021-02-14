package com.catatpelanggaran.gurubk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Catat(
    val nis: String? = null,
    val nama_siswa: String? = null,
    val poin: Int? = null
) : Parcelable