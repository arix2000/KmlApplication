package com.kml.internalRoomDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;


import java.util.List;

@Dao
public interface GameDao
{
    @Query("SELECT * FROM gameTable")
    LiveData<List<Game>> getAllGames();
}
