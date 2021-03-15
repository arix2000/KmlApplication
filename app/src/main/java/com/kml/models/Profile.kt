package com.kml.models

data class Profile(var firstName: String,
                   var lastName: String,
                   var joinYear: String,
                   var timeOfWorkSeason: String,
                   var sections: String,
                   var type: String,
                   var timeOfWorkMonth: String) {
    companion object {
        val EMPTY_PROFILE = Profile("","","","","","","")
    }

    var fullName:String = ""
        get() {return "$firstName $lastName"}
}