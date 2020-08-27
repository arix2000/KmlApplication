package com.kml.workTimer;

import android.util.Log;

import com.kml.aGlobalUses.KmlApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DbSendWork extends Thread
{

    String workName;
    String workDescription;
    String firstName;
    String lastName;
    int minutes, hours;
    public boolean mResult;


    public DbSendWork(String workName, String workDescription, String name, String lastName, int minutes, int hours)
    {
        this.workName = workName;
        this.workDescription = workDescription;
        this.firstName = name;
        this.lastName = lastName;
        this.minutes = minutes;
        this.hours = hours;
    }

    @Override
    public void run()

    {
        mResult = false;
        float timeToSend;
        int loginId = KmlApp.loginId;
        timeToSend = hours + (float) minutes / 60;

        // zaokraglenie do dwóch miejsc po przecinku, inny sposób po prostu nie działał
        timeToSend = timeToSend*100;
        timeToSend= Math.round(timeToSend);
        timeToSend = timeToSend / 100;

        String czasPracyDokladny = hours+"h "+minutes+"min";
        StringBuilder result = new StringBuilder();
        Log.d("TIME_TO_SEND", "run: "+timeToSend);
        String address = "http://sobos.ssd-linuxpl.com/updateCzasPracy.php";

        try {
            URL connUrl = new URL(address);

            HttpURLConnection httpConnection = (HttpURLConnection) connUrl.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);

            OutputStream outStream = httpConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
            String dataToSend = URLEncoder.encode("czasPracy","UTF-8")+"="+URLEncoder.encode(String.valueOf(timeToSend),"UTF-8")
                    +"&&"+URLEncoder.encode("loginId","UTF-8")+"="+ URLEncoder.encode(String.valueOf(loginId),"UTF-8")
                    +"&&"+URLEncoder.encode("nazwaZadania","UTF-8")+"="+URLEncoder.encode(workName,"UTF-8")
                    +"&&"+URLEncoder.encode("opisZadania","UTF-8")+"="+ URLEncoder.encode(workDescription,"UTF-8")
                    +"&&"+URLEncoder.encode("czasPracyDokladny","UTF-8")+"="+ URLEncoder.encode(czasPracyDokladny,"UTF-8")
                    +"&&"+URLEncoder.encode("imie","UTF-8")+"="+ URLEncoder.encode(firstName,"UTF-8")
                    +"&&"+URLEncoder.encode("nazwisko","UTF-8")+"="+ URLEncoder.encode(lastName,"UTF-8");

            writer.write(dataToSend);
            writer.flush();
            writer.close(); outStream.close();

            InputStream inputStream = httpConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String line;

            while((line=reader.readLine()) != null)
            {
                result.append(line);
            }

            inputStream.close(); reader.close();
            httpConnection.disconnect();

            mResult = result.toString().equals("true");

        } catch (IOException e) {
            result.append(e.getMessage());
        }
        Log.d("RUNEXCEPTION", "run: "+result);
    }

    public boolean getResult()
    {
        try {
            this.join();
        } catch (InterruptedException e) {
            Log.d("INTERRUPTEDEXCEPTION", "getResult: "+e.getMessage());
        }
        return mResult;
    }

}
