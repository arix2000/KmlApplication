package com.kml.models.dto

data class WorkToAdd(
    val name: String,
    val description: String,
    val hours: Int,
    val minutes: Int,
    val type: String = ""
)