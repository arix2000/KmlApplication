package com.kml.utilities

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.preferences.core.edit
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.kml.Constants
import com.kml.Constants.Keys.IS_FROM_NOTIFICATION_BUNDLE_KEY
import com.kml.KmlApp
import com.kml.R
import com.kml.extensions.dataStore
import com.kml.views.activities.SplashScreenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RemainderWorker(appContext: Context, params: WorkerParameters)
    : Worker(appContext, params) {

    override fun doWork(): Result {
        showRemainderNotification()
        return Result.success()
    }

    private fun showRemainderNotification() {
        val notificationIntent = Intent(applicationContext, SplashScreenActivity::class.java)
            .putExtra(IS_FROM_NOTIFICATION_BUNDLE_KEY, true)
            .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(applicationContext,
            0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(applicationContext, KmlApp.CHANNEL_1_ID)
            .setContentTitle("Kończy się miesiąc. Czas wpisać godziny! :D")
            .setSmallIcon(R.mipmap.icon_round) //TODO - icon work bad :( maybe try with outline logo or B&W
            .setContentIntent(pendingIntent)
            .setColorized(true)
            .setColor(Color.argb(255, 63, 95, 190))
            .setContentText("Naciśnij aby go otworzyć")
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(231, builder)
    }

    fun saveThatIsFromNotificationValue() {
        CoroutineScope(Dispatchers.IO).launch {
            applicationContext.dataStore.edit { it[Constants.Keys.IS_FROM_NOTIFICATION_KEY] = true }
        }
    }
}