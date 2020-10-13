package com.kml.repositories

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.kml.data.internalRoomDatabase.GameDao
import com.kml.data.internalRoomDatabase.GameDatabase
import com.kml.data.models.Game

class GameRepository(context: Context) {
    private val gameDao: GameDao?
    val allGames: LiveData<List<Game>>

    init {
        val database = GameDatabase.getInstance(context)
        gameDao = database?.gameDao()
        allGames = gameDao!!.allGames
    }
}