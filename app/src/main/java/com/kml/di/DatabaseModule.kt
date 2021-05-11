package com.kml.di

import com.kml.data.database.GameDatabase
import org.koin.dsl.module

val databaseModule = module {

    single { GameDatabase.getInstance(get()) }

    single { get<GameDatabase>().gameDao }
}