package com.kml.data.externalDbOperations;

import android.util.Log;

import com.kml.data.app.KmlApp;
import com.kml.data.models.WorkToAdd;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import static com.kml.data.app.KmlApp.firstName;
import static com.kml.data.app.KmlApp.lastName;

public class DbSendWork extends ExternalDbHelper
{
    private final String fileName = "updateCzasPracy.php";

    WorkToAdd work;
    public boolean mResult;

    public DbSendWork(WorkToAdd work)
    {
        this.work = work;
    }

    @Override
    public void run()
    {
        mResult = false;
        float timeToSend;
        timeToSend = work.getHours() + (float) work.getMinutes() / 60;
        timeToSend = roundToTwoDecimalPoint(timeToSend);

        String address = BASE_URL+fileName;;
        HttpURLConnection httpConnection = setConnection(address);
        try {
            sendData(httpConnection, timeToSend);
            mResult = readResult(httpConnection).equals("true");

        } catch (IOException e) {
            Log.d("IO_EXCEPTION", "run: " + e.getMessage());
        }
    }

    private float roundToTwoDecimalPoint(float timeToSend)
    {
        timeToSend = timeToSend * 100;
        timeToSend = Math.round(timeToSend);
        timeToSend = timeToSend / 100;
        return timeToSend;
    }

    private void sendData(HttpURLConnection connection, float timeToSend) throws IOException
    {
        OutputStream outStream = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));

        String dataToSend = setDataToSend(timeToSend);

        writer.write(dataToSend);
        writer.flush();
        writer.close();
        outStream.close();

    }

    private String setDataToSend(float timeToSend) throws UnsupportedEncodingException
    {
        String workTimeExact = work.getHours() + "h " + work.getMinutes() + "min";
        return URLEncoder.encode("czasPracy", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(timeToSend), "UTF-8")
                + "&&" + URLEncoder.encode("loginId", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(KmlApp.loginId), "UTF-8")
                + "&&" + URLEncoder.encode("nazwaZadania", "UTF-8") + "=" + URLEncoder.encode(work.getName(), "UTF-8")
                + "&&" + URLEncoder.encode("opisZadania", "UTF-8") + "=" + URLEncoder.encode(work.getDescription(), "UTF-8")
                + "&&" + URLEncoder.encode("czasPracyDokladny", "UTF-8") + "=" + URLEncoder.encode(workTimeExact, "UTF-8")
                + "&&" + URLEncoder.encode("imie", "UTF-8") + "=" + URLEncoder.encode(firstName, "UTF-8")
                + "&&" + URLEncoder.encode("nazwisko", "UTF-8") + "=" + URLEncoder.encode(lastName, "UTF-8");
    }

    public boolean getResult()
    {
        try {
            this.join();
        } catch (InterruptedException e) {
            Log.d("INTERRUPTEDEXCEPTION", "getResult: " + e.getMessage());
        }
        return mResult;
    }

}
