package com.kml.di

import com.kml.data.networking.NetworkManager
import org.koin.dsl.module


val networkModule = module {
    single { networkManager.provideRetrofit() }
    factory { networkManager.provideApi(get()) }
}

private val networkManager = NetworkManager()