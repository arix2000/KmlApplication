package com.kml.profile;


import android.util.Log;

import com.kml.aGlobalUses.ExternalDbHelper;
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

public class DbGetUserData extends ExternalDbHelper
{
    private String result;

    @Override
    public void run()
    {
        String address = "http://sobos.ssd-linuxpl.com/getDataAboutUser.php";

        try {
            HttpURLConnection connection = setConnection(address);
            sendData(connection);
            result = readResult(connection);
        } catch (Exception e) {
            Log.e("DB_GET_USERDATA_ERROR", "run: "+e.getMessage(), e);
        }
    }

    private void sendData(HttpURLConnection connection) throws IOException
    {
        OutputStream outStream = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
        String dataToSend = URLEncoder.encode("loginId","UTF-8")+"="+ URLEncoder.encode(String.valueOf(KmlApp.loginId),"UTF-8");

        writer.write(dataToSend);
        writer.flush();
        writer.close(); outStream.close();
    }

    public String getResult()
    {
        try {
            this.join();
        } catch (InterruptedException e) {
            Log.d("INTERRUPTED_EXCEPTION", "getResult: "+e.getMessage());
        }
        return result;
    }
}


