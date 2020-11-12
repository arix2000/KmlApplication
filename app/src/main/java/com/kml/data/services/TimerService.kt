package com.kml.data.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.kml.R
import com.kml.data.app.FileFactory
import com.kml.data.app.KmlApp
import com.kml.views.activities.MainActivity
import java.util.*

class TimerService : Service() {
    var fileFactory: FileFactory? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0)

        fileFactory = FileFactory(this)

        fileFactory!!.saveStateToFile("$seconds;$minutes;$hours", FileFactory.CURRENT_TIME_TXT)
        val notification = NotificationCompat.Builder(this, KmlApp.CHANNEL_1_ID)
                .setContentTitle("Stoper jest uruchomiony")
                .setSmallIcon(R.mipmap.icon_foreground)
                .setContentIntent(pendingIntent)
                .setColorized(true)
                .setColor(Color.argb(255, 63, 95, 190))
                .setContentText("Naciśnij aby go otworzyć")
                .build()

        startForeground(1, notification)

        isServiceRunning = true

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (exitServiceThread) {
                    cancel()
                } else {
                    seconds += 1
                    convertTimeToReadable()
                    if (seconds == 30 || seconds == 0) {
                        saveStateToFile()
                    }
                }
            }
        }, 0, 1000)
        return START_STICKY
    }

    override fun onDestroy() {
        isServiceRunning = false
        exitServiceThread = true
        super.onDestroy()
    }

    fun saveStateToFile() {
        fileFactory!!.saveStateToFile("$seconds;$minutes;$hours", FileFactory.CURRENT_TIME_TXT)
    }

    private fun convertTimeToReadable() {
        if (seconds >= 60) {
            minutes += 1
            seconds = 0
        }
        if (minutes >= 60) {
            hours += 1
            minutes = 0
        }
    }

    companion object {
        var seconds = 0
        var minutes = 0
        var hours = 0
        var isServiceRunning = false
        var exitServiceThread = false
        var wasPlayClicked = false
    }
}