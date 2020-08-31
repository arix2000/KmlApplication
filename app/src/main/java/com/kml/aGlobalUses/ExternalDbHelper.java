package com.kml.aGlobalUses;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class ExternalDbHelper extends Thread
{
    public static final String IO_EXCEPTION_TAG = "IO_EXCEPTION_TAG";

    public HttpURLConnection setConnection(String address)
    {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(address);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

        }
        catch (IOException e)
        {
            Log.d(IO_EXCEPTION_TAG, "IOException setConnection: "+e.getMessage());
        }
        return conn;
    }

    public String readResult(HttpURLConnection conn)
    {
        InputStream inputStream;
        StringBuilder readResult = new StringBuilder();
        try {
            inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String line;

            while((line=reader.readLine()) != null)
            {
                readResult.append(line);
            }
            inputStream.close(); reader.close();
            conn.disconnect();
        } catch (IOException e) {
            Log.d(IO_EXCEPTION_TAG, "readResult: "+e.getMessage());
        }

        return readResult.toString();
    }

}
