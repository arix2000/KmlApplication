package com.kml.controlPanel;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.internal.schedulers.IoScheduler;

public class DbGetAllUsersData extends Thread
{
    String address;
    StringBuilder result;
    HttpURLConnection conn;

    public DbGetAllUsersData()
    {
        address = "http://192.168.1.106/KmlApp_WebApi/getAllDataAboutUser.php";
        result = new StringBuilder();
    }

    @Override
    public void run()
    {
        StringBuilder dataFromDb = new StringBuilder();

        try {
            setConnection();
            dataFromDb = getAllDataFromDb();
        } catch (IOException e) {
            Log.d("IO_EXCEPTION", "run: " + e.getMessage());
        }

        setResult(dataFromDb);

    }

    private void setConnection() throws IOException
    {
        URL url = new URL(address);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

    }

    private StringBuilder getAllDataFromDb() throws IOException
    {
        InputStream inStream = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String line;
        StringBuilder allDataFromDb = new StringBuilder();
        while((line=reader.readLine()) != null)
        {
            allDataFromDb.append(line);
        }
        return allDataFromDb;
    }

    private void setResult(StringBuilder result)
    {
        this.result = result;
    }

    public String getResult()
    {
        try {
            this.join();
        } catch (InterruptedException e) {
            Log.d("INTERRUPTED_EXCEPTION", "getResult: " + result + " -> " + e.getMessage());
        }
        Log.d("RESULT_GET_ALL", "getResult: "+result);
        return result.toString();
    }
}
