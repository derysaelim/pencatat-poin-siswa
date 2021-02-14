package com.catatpelanggaran.gurubk.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Login(
    val nip: String? = null,
    val password: String? = null,
    val role: String? = null
) : Parcelable