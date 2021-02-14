package com.adriandery.catatpelanggaran.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ortu(
    val nis: String? = null,
    val nama: String? = null,
    val nohp: String? = null
) : Parcelable