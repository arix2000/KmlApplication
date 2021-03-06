package com.kml.models.entitiy

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "gameTable")
data class Game(var name: String,
           var description: String,
           var requirements: String,
           var numberOfKids: String,
           var kidsAge: String,
           var place: String,
           var typeOfGame: String,
           var category: String) : Parcelable {

    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id = 0
}