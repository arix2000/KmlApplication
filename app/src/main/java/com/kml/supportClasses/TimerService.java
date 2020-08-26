package com.kml.supportClasses;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.kml.MainActivity;
import com.kml.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.kml.KmlApp.CHANNEL_1_ID;

public class TimerService extends Service
{
    public static int seconds;
    public static int minutes;
    public static int hours;
    public static boolean isServiceRunning;
    public static boolean exitServiceThread;
    public static boolean wasPlayClicked;
    FileOperations fileOperations;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        fileOperations = new FileOperations(this);
        fileOperations.saveStateToFile(seconds + ";" + minutes + ";" + hours, FileOperations.CURRENT_TIME_TXT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setContentTitle("Stoper jest uruchomiony")
                .setSmallIcon(R.mipmap.icon_foreground)
                .setContentIntent(pendingIntent)
                .setColorized(true)
                .setColor(Color.argb(255, 63, 95, 190))
                .setContentText("Naciśnij aby go otworzyć")
                .build();

        startForeground(1, notification);
        isServiceRunning = true;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {

            @Override
            public void run()
            {
                if (exitServiceThread) {
                    this.cancel();
                } else {
                    seconds += 1;
                    convertTimeToReadable();
                    if (seconds == 30 || seconds == 0) {
                        saveStateToFile();
                    }
                }
            }
        }, 0, 1000);

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        isServiceRunning = false;
        exitServiceThread = true;

        super.onDestroy();
    }

    public void saveStateToFile()
    {
        fileOperations.saveStateToFile(seconds + ";" + minutes + ";" + hours, FileOperations.CURRENT_TIME_TXT);
    }


    private void convertTimeToReadable()
    {

        if (seconds >= 60) {
            minutes += 1;
            seconds = 0;
        }
        if (minutes >= 60) {
            hours += 1;
            minutes = 0;
        }
        Log.d("HOW_I_COUNT", "count: Sec: " + seconds + "  Min: " + minutes + "  H: " + hours + " running: " + isServiceRunning + " exit: " + exitServiceThread);
    }

}

