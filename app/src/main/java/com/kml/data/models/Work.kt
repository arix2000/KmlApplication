package com.kml.data.models

import com.google.gson.annotations.SerializedName

class Work(@field:SerializedName("nazwaZadania") val workName: String,
           @field:SerializedName("opisZadania") val workDescription: String,
           @field:SerializedName("data") val workDate: String,
           @field:SerializedName("czasWykonania") val executionTime: String)