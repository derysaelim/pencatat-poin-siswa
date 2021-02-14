package com.catatpelanggaran.admin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Penghargaan(
    val id_penghargaan: String? = null,
    val namaPenghargaan: String? = null,
    val poin: Int? = null
) : Parcelable