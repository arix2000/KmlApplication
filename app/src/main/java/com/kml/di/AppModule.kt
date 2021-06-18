package com.kml.di

import com.kml.extensions.dataStore
import com.kml.utilities.FileFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { FileFactory(get()) }

    single { androidContext().dataStore }
}