package com.kml.data.externalDbOperations;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class DbLogin extends ExternalDbHelper
{
    private final String fileName = "login.php";

    private HttpURLConnection httpConnection;
    private String result;
    private String address;
    private String login;
    private String password;

    public DbLogin(String login, String password)
    {
        address = BASE_URL+fileName;
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
