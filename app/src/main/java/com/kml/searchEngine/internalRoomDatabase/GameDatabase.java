package com.kml.searchEngine.internalRoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Game.class}, version = 1)
public abstract class GameDatabase extends RoomDatabase
{
    private static GameDatabase instance;
    public abstract GameDao gameDao();
    public static synchronized GameDatabase getInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GameDatabase.class, "gameDatabase.db")
                    .createFromAsset("databases/gameDatabase.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}









