package com.kml.data.externalDbOperations;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kml.data.listeners.OnResultListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class ExternalDbHelper extends Thread
{
    private OnResultListener listener;

    public static final String IO_EXCEPTION_TAG = "IO_EXCEPTION_TAG";
    public static final String BASE_URL = "http://sobos.ssd-linuxpl.com/";

    protected HttpURLConnection setConnection(String address)
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

    protected String readResult(@NonNull HttpURLConnection conn)
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

    protected void invokeOnReceive(String result)
    {
        if(listener != null)
        new Handler(Looper.getMainLooper()).post(()-> listener.onReceive(result));
    }

    public void setOnResultListener(OnResultListener listener) {
        this.listener = listener;
    }
}
