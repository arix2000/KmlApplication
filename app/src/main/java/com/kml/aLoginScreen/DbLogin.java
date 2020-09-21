package com.kml.aLoginScreen;

import android.os.AsyncTask;
import android.util.Log;

import com.kml.aGlobalUses.ExternalDbHelper;

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

public class DbLogin extends ExternalDbHelper
{
    private HttpURLConnection httpConnection;
    private String result;
    private final String address = "http://sobos.ssd-linuxpl.com/login.php";
    private String login;
    private String password;

    public DbLogin(String login, String password)
    {
        this.login = login;
        this.password = password;
    }

    @Override
    public void run()
    {
        httpConnection = setConnection(address);
        sendData();
        result = readResult(httpConnection);
    }


    private void sendData()
    {
        try {
            OutputStream outStream = httpConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
            String dataToSend = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8")
                    + "&&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            writer.write(dataToSend);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            Log.d(IO_EXCEPTION_TAG, "sendData: "+e.getMessage());
        }
    }

    public String getResult()
    {
        try {
            join();
        } catch (InterruptedException e) {
            Log.d(IO_EXCEPTION_TAG, "getResult: "+e.getMessage());
        }
        return result;
    }
}
