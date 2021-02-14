package com.catatpelanggaran.orangtua.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pelanggaran(
    val idPelanggaran: String? = null,
    val namaPelanggaran: String? = null,
    val poinPelanggaran: Int? = null,
    val hukuman: String? = null,
    val keterangan: String? = null,
    val tanggal: String? = null
) : Parcelable