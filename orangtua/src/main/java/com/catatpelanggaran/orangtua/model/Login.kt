package com.catatpelanggaran.orangtua.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Login(
    val nis: String? = null,
    val password: String? = null,
    val role: String? = null
) : Parcelable