package com.kml.data.internalRoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kml.data.models.Game

@Database(entities = [Game::class], version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao?

    companion object {
        private var instance: GameDatabase? = null
        @Synchronized
         fun getInstance(context: Context): GameDatabase? {
            if (instance == null) {
               synchronized(this) {
                   instance = Room.databaseBuilder(context.applicationContext,
                           GameDatabase::class.java, "gameDatabase.db")
                           .createFromAsset("databases/gameDatabase.db")
                           .fallbackToDestructiveMigration()
                           .build()
               }
            }
            return instance
        }
    }
}