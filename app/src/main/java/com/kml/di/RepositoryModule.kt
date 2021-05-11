package com.kml.di

import com.kml.repositories.*
import org.koin.dsl.module

val repositoryModule = module {

    single { MainRepository(get()) }

    single { BrowserVolunteerWorksRepository() }

    single { GameRepository(get()) }

    single { ProfileRepository(get()) }

    single { SplashScreenRepository(get()) }

    single { LoginRepository(get()) }

    single { SummaryVolunteerRepository() }

    single { VolunteerBrowserRepository() }

    single { VolunteerRepository() }

    single { VolunteersBrowserDetailsRepository() }

    single { WorkAddingRepository(get()) }

    single { WorksHistoryRepository(get()) }
}