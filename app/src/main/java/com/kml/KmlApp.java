package com.kml;

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

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

//TODO Stworzenie panelu sterowania z którego będzie można przeglądać oraz edytować baze danych
        //TODO Na Poczatek stworzenie panelu wyświetlającego tylko liste ostatnio wykonanych zadań
                //TODO zmiana wygladu formularza #FUTURE
                        //TODO System ostatnich wiadomości (z zewnetrznych powiadomien wysyłanych z firebase)
public class KmlApp extends Application
{
    public static final String CHANNEL_1_ID = "serviceChannel";
    public static int loginId;
    public static String name;
    public static String lastName;
    public static boolean isFromRecycleViewActivity;


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
