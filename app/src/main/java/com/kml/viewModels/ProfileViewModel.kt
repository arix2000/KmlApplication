package com.kml.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kml.R
import com.kml.data.externalDbOperations.DbChangePass
import com.kml.data.models.Profile
import com.kml.data.utilities.FileFactory
import com.kml.data.utilities.FormatEngine
import com.kml.repositories.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(val fileFactory: FileFactory) : ViewModel() {

    companion object {
        const val VALIDATION_SUCCESSFUL = -1
    }

    private val repository = ProfileRepository(fileFactory)

    val profileData = MutableLiveData<Profile>()

    var isFromFile = false
    var isNoDataFound = false
    var isDatabaseUnavailable = false

    private val ioScope = CoroutineScope(Dispatchers.IO)

    init {
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
            createProfileFrom(dataFromFile)
        } else {
            isNoDataFound = true
            Profile.EMPTY_PROFILE
        }
    }

    private fun getDataFromDatabase(result: String): Profile {
        return if (result.trim().isNotEmpty()) {
            createProfileFrom(result)
        } else {
            isDatabaseUnavailable = true
            Profile.EMPTY_PROFILE
        }
    }

    private fun createProfileFrom(result: String): Profile {
        val format = FormatEngine()
        val splitData = result.split(";".toRegex()).toTypedArray()
        return Profile(splitData[0], splitData[1], splitData[2],
                format.convertToReadable(splitData[3]),
                format.formatSections(splitData[4]), splitData[5],
                format.convertToReadable(splitData[6]))
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

    fun getProfilePhotoPath(): String {
        return repository.getProfilePhotoPath()
    }

    fun saveProfilePhoto(path: String) {
        repository.saveProfilePhoto(path)
    }

    fun resolvePasswordChanging(newPassword: String, oldPassword: String): DbChangePass {
        return repository.resolvePasswordChanging(newPassword, oldPassword)
    }
}