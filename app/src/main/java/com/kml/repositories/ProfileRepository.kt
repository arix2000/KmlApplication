package com.kml.repositories

import android.graphics.Bitmap
import com.kml.KmlApp
import com.kml.data.networking.RestApi
import com.kml.extensions.async
import com.kml.extensions.log
import com.kml.extensions.toBitmap
import com.kml.extensions.toEncodedString
import com.kml.models.dto.Profile
import com.kml.utilities.FileFactory
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProfileRepository(
    val fileFactory: FileFactory,
    private val restApi: RestApi
    ): BaseRepository() {

    fun getUserInfoFromDb(): Single<Profile> {
        return restApi.fetchUserInfo(KmlApp.loginId).async()
    }

    fun resolvePasswordChanging(newPassword: String, oldPassword: String): Single<String> {
        return restApi.changePass(newPassword, oldPassword, KmlApp.loginId.toString())
            .async()
    }

    fun getUserInfoFromFile(): String {
        return fileFactory.readFromFile(FileFactory.PROFILE_KEEP_DATA_TXT)
    }

    fun saveProfileValues(profile: Profile) {
        fileFactory.saveStateToFile(profile.firstName + ";" + profile.lastName + ";" + profile.joinYear + ";" + profile.timeOfWorkSeason
                + ";" + profile.sections + ";" + profile.type + ";" + profile.timeOfWorkMonth, FileFactory.PROFILE_KEEP_DATA_TXT)
    }

    fun getProfilePhoto(onBitmapReady: (Bitmap?) -> Unit) {
        ioScope.launch {
            val bitmap = fileFactory.readFromFile(FileFactory.PROFILE_PHOTO_PATH_TXT).toBitmap()
            bitmap?.height?.let { log(it) }
            CoroutineScope(Dispatchers.Main).launch {
                onBitmapReady(bitmap)
            }
        }
    }

    fun saveProfilePhoto(path: Bitmap): Job {
        return ioScope.launch {
            fileFactory.saveStateToFile(path.toEncodedString(), FileFactory.PROFILE_PHOTO_PATH_TXT)
        }
    }
}