package com.kml.data.models

import android.os.Parcel
import android.os.Parcelable
import java.lang.NullPointerException

data class GameFilterInfo(val name: String, val numberOfKids: String,
                          val kidsAge: String, val place: String, val typeOfGame: String, val category: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(numberOfKids)
        parcel.writeString(kidsAge)
        parcel.writeString(place)
        parcel.writeString(typeOfGame)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameFilterInfo> {
        override fun createFromParcel(parcel: Parcel): GameFilterInfo {
            return GameFilterInfo(parcel)
        }

        override fun newArray(size: Int): Array<GameFilterInfo?> {
            return arrayOfNulls(size)
        }
    }
}