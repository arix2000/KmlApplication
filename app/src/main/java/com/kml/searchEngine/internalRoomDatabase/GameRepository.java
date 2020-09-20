package com.kml.searchEngine.internalRoomDatabase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class GameRepository
{
    private GameDao gameDao;
    private LiveData<List<Game>> allGames;
    public GameRepository(Application application)
    {
        GameDatabase database = GameDatabase.getInstance(application);
        gameDao = database.gameDao();
        allGames = gameDao.getAllGames();
    }

    public LiveData<List<Game>> getAllGames() {
        return allGames;
    }

    

}
