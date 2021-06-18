package com.kml.di

import com.kml.viewModels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { GameViewModel(get()) }

    viewModel { BrowserVolunteerWorksViewModel(get()) }

    viewModel { LoginViewModel(get()) }

    viewModel { MainViewModel(get()) }

    viewModel { ProfileViewModel(get()) }

    viewModel { SplashScreenViewModel(get()) }

    viewModel { SummaryVolunteerViewModel(get()) }

    viewModel { VolunteersBrowserDetailsViewModel(get()) }

    viewModel { VolunteersBrowserViewModel(get()) }

    viewModel { VolunteersViewModel(get()) }

    viewModel { WorkAddingViewModel(get()) }

    viewModel { WorksHistoryViewModel(get()) }

    viewModel { BrowserVolunteerMeetingsViewModel(get()) }
}