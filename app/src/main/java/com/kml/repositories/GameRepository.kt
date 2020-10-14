package com.kml.repositories

import androidx.lifecycle.LiveData
import com.kml.data.internalRoomDatabase.GameDao
import com.kml.data.models.Game

class GameRepository(gameDao: GameDao) {
    val allGames: LiveData<List<Game>> = gameDao.allGames
}