package com.adriandery.catatpelanggaran.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Login(
    val nis: String? = null,
    val password: String? = null,
    val role: String = "Orang_Tua"
) : Parcelable