package com.kml.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Volunteer(val id: Int,
                     @SerializedName("imie")
                     val firstName: String,
                     @SerializedName("nazwisko")
                     val lastName: String,
                     var isChecked: Boolean) : Parcelable