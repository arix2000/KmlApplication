package com.kml.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BaseRepository: KoinComponent {
    val dataStore: DataStore<Preferences> by inject()

    val ioScope = CoroutineScope(Dispatchers.IO)
}