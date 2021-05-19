package com.kml.repositories

import androidx.datastore.preferences.preferencesDataStore
import com.kml.Constants.Keys.DATA_STORE_NAME

open class BaseRepository {
    val dataStore = preferencesDataStore(DATA_STORE_NAME)
}