package com.kml.repositories

import com.kml.data.app.KmlApp
import com.kml.data.externalDbOperations.DbChangePass
import com.kml.data.externalDbOperations.DbGetUserData
import com.kml.data.utilities.FileFactory
import com.kml.models.Profile

class ProfileRepository(val fileFactory: FileFactory) {

    fun getUserInfoFromDb():String
    {
        val dbGetUserData = DbGetUserData()
        dbGetUserData.start()
        return dbGetUserData.result
    }

    fun resolvePasswordChanging(newPassword:String, oldPassword:String): DbChangePass {
        val dbChangePass = DbChangePass(newPassword, oldPassword, KmlApp.loginId)
        dbChangePass.start()
        return dbChangePass
    }

    fun getUserInfoFromFile(): String {
        return fileFactory.readFromFile(FileFactory.PROFILE_KEEP_DATA_TXT)
    }

    fun saveProfileValues(profile: Profile) {
        fileFactory.saveStateToFile(profile.firstName + ";" + profile.lastName + ";" + profile.joinYear + ";" + profile.timeOfWorkSeason
                + ";" + profile.sections + ";" + profile.type + ";" + profile.timeOfWorkMonth, FileFactory.PROFILE_KEEP_DATA_TXT)
    }

    fun getProfilePhotoPath():String {
        return fileFactory.readFromFile(FileFactory.PROFILE_PHOTO_PATH_TXT)
    }

    fun saveProfilePhoto(path:String) {
       fileFactory.saveStateToFile(path, FileFactory.PROFILE_PHOTO_PATH_TXT)
    }
}