package com.kml.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Volunteer(val id: Int,
                @SerializedName("imie") val firstName: String,
                @SerializedName("nazwisko") val lastName: String,
                var isChecked: Boolean) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Volunteer> {
        override fun createFromParcel(parcel: Parcel): Volunteer {
            return Volunteer(parcel)
        }

        override fun newArray(size: Int): Array<Volunteer?> {
            return arrayOfNulls(size)
        }
    }
}