package com.kml.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.kml.models.entitiy.Game

@Dao
interface GameDao {
    @get:Query("SELECT * FROM gameTable")
    val allGames: LiveData<List<Game>>
}