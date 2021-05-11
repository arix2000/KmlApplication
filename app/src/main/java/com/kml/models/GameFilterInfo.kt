package com.kml.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameFilterInfo(
    val name: String,
    val numberOfKids: String,
    val kidsAge: String,
    val place: String,
    val typeOfGame: String,
    val category: String
) : Parcelable {
    companion object {
        val EMPTY = GameFilterInfo("","","","","","")
    }
}
