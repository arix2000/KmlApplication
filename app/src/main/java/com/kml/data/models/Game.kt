package com.kml.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gameTable")
class Game(var name: String,
           var description: String,
           var requirements: String,
           var numberOfKids: String,
           var kidsAge: String,
           var place: String,
           var typeOfGame: String,
           var category: String): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(requirements)
        parcel.writeString(numberOfKids)
        parcel.writeString(kidsAge)
        parcel.writeString(place)
        parcel.writeString(typeOfGame)
        parcel.writeString(category)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Game> {
        override fun createFromParcel(parcel: Parcel): Game {
            return Game(parcel)
        }

        override fun newArray(size: Int): Array<Game?> {
            return arrayOfNulls(size)
        }
    }

}