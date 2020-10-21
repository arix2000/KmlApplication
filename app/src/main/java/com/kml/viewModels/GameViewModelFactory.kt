package com.kml.viewModels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kml.data.internalRoomDatabase.GameDao
import com.kml.data.models.GameFilterInfo


class GameViewModelFactory(private val gameDao: GameDao, val gameFilterInfo: GameFilterInfo): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(gameDao, gameFilterInfo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}