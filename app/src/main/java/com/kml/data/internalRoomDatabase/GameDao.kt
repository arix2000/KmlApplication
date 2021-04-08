package com.kml.data.internalRoomDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.kml.models.Game

@Dao
interface GameDao {
    @get:Query("SELECT * FROM gameTable")
    val allGames: LiveData<List<Game>>
}