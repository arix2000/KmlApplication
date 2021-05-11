package com.kml.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kml.data.database.GameDao
import com.kml.data.utilities.Filter
import com.kml.models.Game
import com.kml.models.GameFilterInfo
import com.kml.repositories.GameRepository

class GameViewModel(gameDao: GameDao) : ViewModel() {
     lateinit var filterInfo: GameFilterInfo

    companion object {
        const val DEFAULT_ALL_RANGES = "Wszystkie przedziały"
        const val DEFAULT_ALL_OPTIONS = "Wszystkie"
        const val DEFAULT_ANYTHING = "Każde"
    }

    private val repository = GameRepository(gameDao)
    val games: LiveData<List<Game>> = repository.games

    fun filterGames(gamesVal: List<Game>): List<Game> {
        var games = gamesVal
        val filter = Filter(filterInfo)

        filterInfo.apply {
            if (name.trim().isNotEmpty()) games = filter.byName(games)
            if (numberOfKids != DEFAULT_ALL_RANGES) games = filter.byNumberOfKids(games)
            if (kidsAge != DEFAULT_ALL_RANGES) games = filter.byKidsAge(games)
            if (typeOfGame != DEFAULT_ALL_OPTIONS) games = filter.byTypeOfGame(games)
            if (place != DEFAULT_ANYTHING) games = filter.byPlace(games)
            if (category != DEFAULT_ALL_OPTIONS) games = filter.byCategory(games)
        }

        return games
    }

}