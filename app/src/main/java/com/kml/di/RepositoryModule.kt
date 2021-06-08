package com.kml.di

import com.kml.repositories.*
import org.koin.dsl.module

val repositoryModule = module {

    single { MainRepository(get()) }

    single { GameRepository(get()) }

    single { ProfileRepository(get(), get()) }

    single { SplashScreenRepository(get()) }

    single { LoginRepository(get(), get()) }

    single { SummaryVolunteerRepository(get()) }

    single { VolunteerBrowserRepository(get()) }

    single { VolunteerRepository(get()) }

    single { VolunteersBrowserDetailsRepository(get()) }

    single { WorkAddingRepository(get(), get()) }

    single { WorksHistoryRepository(get(), get()) }

    single { VolunteerLogbookRepository(get()) }
}