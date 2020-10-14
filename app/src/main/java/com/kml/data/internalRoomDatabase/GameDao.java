package com.kml.data.internalRoomDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.kml.data.models.Game;
import java.util.List;

@Dao
public interface GameDao
{
    @Query("SELECT * FROM gameTable")
    LiveData<List<Game>> getAllGames();
}
