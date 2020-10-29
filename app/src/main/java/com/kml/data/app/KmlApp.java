package com.kml.data.app;

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

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.multidex.MultiDexApplication;

import java.util.Arrays;
import java.util.List;

public class KmlApp extends MultiDexApplication
{
    public static final String CHANNEL_1_ID = "serviceChannel";
    public static int loginId;
    public static String firstName;
    public static String lastName;
    public static boolean isFromRecycleViewActivity;
    public static boolean isFromControlPanel;
    public static List<Integer> adminIds = Arrays.asList(9 ,14 ,18, 13);


    @Override
    public void onCreate()
    {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID,"Service Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }

}
