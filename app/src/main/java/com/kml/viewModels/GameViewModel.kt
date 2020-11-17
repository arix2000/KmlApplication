package com.kml.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kml.data.internalRoomDatabase.GameDao
import com.kml.data.models.Game
import com.kml.data.models.GameFilterInfo
import com.kml.data.utilities.Filter
import com.kml.repositories.GameRepository

class GameViewModel(gameDao: GameDao,
                    private val filterInfo: GameFilterInfo) : ViewModel() {

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