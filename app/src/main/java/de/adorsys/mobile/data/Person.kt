package de.adorsys.mobile.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person (
    val id: String,
    val firstName: String,
    val secondName: String,
    val phone: String?,
    val adress: String?,
    val company: String?,
    val email: String?,
    val url: String?,
    val imageUrl: String? = null
): Parcelable
