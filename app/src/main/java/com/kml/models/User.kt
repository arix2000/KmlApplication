package com.kml.models

data class User constructor(val loginId: Int, val firstName: String, val lastName: String) {

    fun isEmpty() = loginId == 0 && firstName.isBlank() && lastName.isBlank()

    companion object {
        val EMPTY = User(0,"","")
    }
}
