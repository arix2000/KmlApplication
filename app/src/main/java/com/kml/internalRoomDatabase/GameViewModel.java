package com.kml.internalRoomDatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class GameViewModel extends AndroidViewModel
{
    private GameRepository repository;
    private LiveData<List<Game>> allGames;
    public GameViewModel(@NonNull Application application)
    {
        super(application);
        repository = new GameRepository(application);
        allGames = repository.getAllGames();
    }

    public LiveData<List<Game>> getAllGames() {
        return allGames;
    }
}
