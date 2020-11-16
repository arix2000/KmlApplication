package com.kml.data.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication

/*
* Aplikacja stworzona dla Fundacji Klubu Młodych Liderow do wpisywania sobie godzin pracy a także oglądania aktualnych postępów
* dodatowo zawiera wyszukiwarke animacji. Stworzona dla wolontariuszy, konta zakładane są z góry dlatego nie ma formularza
* rejestracji.
*
* Wykonawca i właściciel: Arek Mądry
*
* Application created for organization: "Fundacja Klub Młodych Liderów" for count work time and see current progress
* additional includes search engine animation for kids. Created for volunteers,
* accounts are set up in advance, therefore there is no registration form.
*
* Creator and owner: Arek Mądry
* Copyright © 2020
*/
class KmlApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) //default app mode its light
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(CHANNEL_1_ID, "Service Channel", NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel1)
        }
    }

    companion object {
        const val MARTA_ID = 11
        const val SEBASTIAN_ID = 13

        const val CHANNEL_1_ID = "serviceChannel"

        @JvmField
        var loginId = 0

        @JvmField
        var firstName: String = ""

        @JvmField
        var lastName: String = ""

        var isFromRecycleViewActivity = false
        var isFromControlPanel = false
        var adminIds = listOf(9, 14, 18, 13)
    }
}