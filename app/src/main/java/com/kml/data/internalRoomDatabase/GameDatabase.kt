package com.kml.data.internalRoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kml.models.Game

@Database(entities = [Game::class], version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract val gameDao: GameDao

    companion object {
        private var INSTANCE: GameDatabase? = null

        fun getInstance(context: Context): GameDatabase {
            synchronized(this)
            {
                var instance = INSTANCE
                if (instance == null) {

                    instance = Room.databaseBuilder(context.applicationContext,
                            GameDatabase::class.java, "gameDatabase.db")
                            .createFromAsset("databases/gameDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build()

                    INSTANCE = instance
                }

                return instance
            }

        }
    }
}