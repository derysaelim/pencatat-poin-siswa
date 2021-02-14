package com.catatpelanggaran.admin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Guru(
    val nip: String? = null,
    val nama: String? = null,
    val nohp: String? = null
) : Parcelable