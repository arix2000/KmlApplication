package com.kml.models

data class WorkToAdd(
    val name: String,
    val description: String,
    val hours: Int,
    val minutes: Int,
    val type: String = ""
)