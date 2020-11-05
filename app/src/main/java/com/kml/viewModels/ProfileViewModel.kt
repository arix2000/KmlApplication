package com.kml.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kml.R
import com.kml.data.app.FileFactory
import com.kml.data.models.Profile
import com.kml.repositories.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

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
        val dataFromDb = repository.getUserInfoFromDb()
        return if (dataFromDb.trim().isEmpty()) {
            isFromFile = true
            getDataFromFile()
        } else {
            getDataFromDatabase(dataFromDb)
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
        val splitData = result.split(";".toRegex()).toTypedArray()
        return Profile(splitData[0], splitData[1], splitData[2],
                convertToReadable(splitData[3]),
                formatSections(splitData[4]), splitData[5],
                convertToReadable(splitData[6]))
    }

    private fun convertToReadable(timeOfWork: String): String {
        val convertedTime: String
        var workTimeFloat = timeOfWork.toFloat()
        val hours = workTimeFloat.toInt()

        workTimeFloat -= hours
        workTimeFloat = abs(workTimeFloat)

        val helpingInteger = (workTimeFloat * 100).roundToInt()
        workTimeFloat = helpingInteger.toFloat() / 100

        val minutes = (workTimeFloat * 60).roundToInt()
        convertedTime = "$hours godz $minutes min"

        return convertedTime
    }

    private fun formatSections(sections: String): String {
        var changedSection = sections.replace("-".toRegex(), ",")

        if (changedSection.contains("Wolontariusz") && changedSection.contains("Lider"))
            changedSection = "${changedSection.substring(0, changedSection.indexOf("Wolontariusz"))} \n\n" +
                    changedSection.substring(changedSection.indexOf("Wolontariusz")).trimIndent()

        return changedSection
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

    fun resolvePasswordChanging(newPassword: String, oldPassword: String): String {
        return repository.resolvePasswordChanging(newPassword, oldPassword)
    }
}