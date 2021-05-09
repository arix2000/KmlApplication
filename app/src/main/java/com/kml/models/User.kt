package com.kml.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User constructor(val loginId: Int, val firstName: String, val lastName: String): Parcelable {

    fun isEmpty() = loginId == 0 && firstName.isBlank() && lastName.isBlank()

    companion object {
        val EMPTY = User(0,"","")
    }
}
