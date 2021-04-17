package com.kml.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Work(@field:SerializedName("imie") val firstName: String?,
                @field:SerializedName("nazwisko") val lastName: String?,
                @field:SerializedName("nazwaZadania") val workName: String,
                @field:SerializedName("opisZadania") val workDescription: String,
                @field:SerializedName("data") val workDate: String,
                @field:SerializedName("czasWykonania") val executionTime: String,
                @field:SerializedName("osoby") val people: String?
) : Parcelable {
    fun isEmpty() = firstName.isNullOrBlank() && lastName.isNullOrBlank()
            && workName.isBlank() && workDescription.isBlank() && workDate.isBlank()
            && executionTime.isBlank() && people.isNullOrBlank()

}