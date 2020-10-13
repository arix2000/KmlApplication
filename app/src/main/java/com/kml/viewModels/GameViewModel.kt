package com.kml.viewModels

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kml.data.internalRoomDatabase.GameDao
import com.kml.data.models.Game
import com.kml.repositories.GameRepository

class GameViewModel(gameDao: GameDao) : ViewModel() {

    private val repository = GameRepository(gameDao)
    val allGames: LiveData<List<Game>> = repository.allGames
}