package com.kml.viewModels

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kml.data.internalRoomDatabase.GameDao
import com.kml.data.models.Game
import com.kml.repositories.GameRepository
import com.kml.views.SearchEngineFragment
import java.util.*

class GameViewModel(gameDao: GameDao, intent: Intent) : ViewModel() {

    private lateinit var name: String
    private lateinit var numberOfKids: String
    private lateinit var kidsAge: String
    private lateinit var place: String
    private lateinit var typeOfGame: String
    private lateinit var category: String

    companion object {
        const val DEFAULT_ALL_RANGES = "Wszystkie przedziały"
        const val DEFAULT_ALL_OPTIONS = "Wszystkie"
        const val DEFAULT_ANYTHING = "Każde"
    }

    private val repository = GameRepository(gameDao)
    val allGames: LiveData<List<Game>> = repository.allGames

    init {
        getDataFrom(intent)
    }

    private fun getDataFrom(intent: Intent) {
        intent.apply {
            name = getStringExtra(SearchEngineFragment.EXTRA_NAME).toString()
            numberOfKids = getStringExtra(SearchEngineFragment.EXTRA_NUMBER_OF_KIDS).toString()
            kidsAge = getStringExtra(SearchEngineFragment.EXTRA_KIDS_AGE).toString()
            place = getStringExtra(SearchEngineFragment.EXTRA_PLACE).toString()
            typeOfGame = getStringExtra(SearchEngineFragment.EXTRA_TYPE_OF_GAMES).toString()
            category = getStringExtra(SearchEngineFragment.EXTRA_CATEGORY).toString()
        }
    }

    fun filterGames(gamesVal: List<Game>): List<Game> {
        var games = gamesVal

        if (name.trim().isNotEmpty()) games = filterByName(games)
        if (numberOfKids != DEFAULT_ALL_RANGES) games = filterByNumberOfKids(games)
        if (kidsAge != DEFAULT_ALL_RANGES) games = filterByKidsAge(games)
        if (typeOfGame != DEFAULT_ALL_OPTIONS) games = filterByTypeOfGame(games)
        if (place != DEFAULT_ANYTHING) games = filterByPlace(games)
        if (category != DEFAULT_ALL_OPTIONS) games = filterByCategory(games)

        return games
    }

    private fun filterByName(games: List<Game>): List<Game> {

        return games.filter { it.name.contains(name.toUpperCase(Locale.getDefault())) }
    }

    private fun filterByNumberOfKids(games: List<Game>): List<Game> {

        return games.filter { it.numberOfKids.contains(numberOfKids.replace("+", "<")) }
    }

    private fun filterByKidsAge(games: List<Game>): List<Game> {

        return games.filter { it.kidsAge.contains(kidsAge.replace("+", "<")) }
    }

    private fun filterByTypeOfGame(games: List<Game>): List<Game> {

        return games.filter { it.typeOfGame.contains(typeOfGame) }
    }

    private fun filterByPlace(games: List<Game>): List<Game> {

        return games.filter { it.place.contains(place) }
    }

    private fun filterByCategory(games: List<Game>): List<Game> {

        return games.filter { it.category.contains(category) }
    }
}