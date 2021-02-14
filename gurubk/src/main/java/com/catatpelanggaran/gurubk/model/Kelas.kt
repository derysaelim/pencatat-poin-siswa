package com.catatpelanggaran.gurubk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kelas(
    val idKelas: String? = null,
    val tingkat: String? = null,
    val jurusan: String? = null,
    val kelas: String? = null,
    val nip: String? = null
) : Parcelable