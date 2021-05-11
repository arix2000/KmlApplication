package com.kml.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kml.Constants.Signal.VALIDATION_SUCCESSFUL
import com.kml.R
import com.kml.data.networking.DbChangePass
import com.kml.models.Profile
import com.kml.repositories.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    val profileData = MutableLiveData<Profile>()
    var isFromFile = false
    var isNoDataFound = false
    var isDatabaseUnavailable = false

    private val ioScope = CoroutineScope(Dispatchers.IO)

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        ioScope.launch { profileData.postValue(getProfileData()) }
    }

    private fun getProfileData(): Profile {
        val result = repository.getUserInfoFromDb()
        return if (result.trim().isEmpty()) {
            isFromFile = true
            getDataFromFile()
        } else {
            getDataFromDatabase(result)
        }
    }

    private fun getDataFromFile(): Profile {
        val dataFromFile = repository.getUserInfoFromFile()

        return if (dataFromFile.isNotEmpty() || dataFromFile != "") {
            Profile.createFrom(dataFromFile)
        } else {
            isNoDataFound = true
            Profile.EMPTY_PROFILE
        }
    }

    private fun getDataFromDatabase(result: String): Profile {
        return if (result.trim().isNotEmpty()) {
            Profile.createFrom(result)
        } else {
            isDatabaseUnavailable = true
            Profile.EMPTY_PROFILE
        }
    }

    fun refreshProfile() {
        fetchProfile()
    }

    fun validatePassword(oldPassword: String, newPassword: String): Int {
        return if (newPassword.isEmpty() || oldPassword.isEmpty()) {
            R.string.no_empty_fields
        } else if (newPassword.length > 64) {
            R.string.too_many_chars
        } else VALIDATION_SUCCESSFUL
    }

    fun saveProfileValues(profile: Profile) {
        ioScope.launch { repository.saveProfileValues(profile) }
    }

    fun getProfilePhoto(onBitmapReady: (Bitmap?) -> Unit) {
        repository.getProfilePhoto(onBitmapReady)
    }

    fun saveProfilePhoto(path: Bitmap): Job {
        return repository.saveProfilePhoto(path)
    }

    fun resolvePasswordChanging(newPassword: String, oldPassword: String): DbChangePass {
        return repository.resolvePasswordChanging(newPassword, oldPassword)
    }
}