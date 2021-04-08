package com.kml.repositories

import androidx.lifecycle.LiveData
import com.kml.data.internalRoomDatabase.GameDao
import com.kml.models.Game

class GameRepository(gameDao: GameDao) {
    val games: LiveData<List<Game>> = gameDao.allGames
}