package com.kml.repositories

import androidx.lifecycle.LiveData
import com.kml.data.database.GameDao
import com.kml.models.Game

class GameRepository(gameDao: GameDao): BaseRepository() {
    val games: LiveData<List<Game>> = gameDao.allGames
}