package com.kml.utilities

import com.kml.models.entitiy.Game
import com.kml.models.model.GameFilterInfo
import java.util.*

class Filter(private val filterInfo: GameFilterInfo) {

    fun byName(games: List<Game>): List<Game> {
        return games.filter { it.name.contains(filterInfo.name.uppercase(Locale.getDefault())) }
    }

    fun byNumberOfKids(games: List<Game>): List<Game> {
        return games.filter { it.numberOfKids.contains(filterInfo.numberOfKids.replace("+", "<")) }
    }

    fun byKidsAge(games: List<Game>): List<Game> {
        return games.filter { it.kidsAge.contains(filterInfo.kidsAge.replace("+", "<")) }
    }

    fun byTypeOfGame(games: List<Game>): List<Game> {
        return games.filter { it.typeOfGame.contains(filterInfo.typeOfGame) }
    }

    fun byPlace(games: List<Game>): List<Game> {
        return games.filter { it.place.contains(filterInfo.place) }
    }

    fun byCategory(games: List<Game>): List<Game> {
        return games.filter { it.category.contains(filterInfo.category) }
    }
}