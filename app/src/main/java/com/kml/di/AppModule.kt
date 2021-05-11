package com.kml.di

import com.kml.data.utilities.FileFactory
import org.koin.dsl.module

val appModule = module {
    single { FileFactory(get()) }
}