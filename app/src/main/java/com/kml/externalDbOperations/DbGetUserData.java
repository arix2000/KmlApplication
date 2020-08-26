package com.kml.externalDbOperations;


import android.util.Log;

import com.kml.KmlApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DbGetUserData extends Thread
{
    private StringBuilder result;

    @Override
    public void run()
    {
        int loginId = KmlApp.loginId;
        result = new StringBuilder();
        String address = "http://sobos.ssd-linuxpl.com/getDataAboutUser.php";

        try {

            URL connUrl = new URL(address);

            HttpURLConnection connection = (HttpURLConnection) connUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream outStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
            String dataToSend = URLEncoder.encode("loginId","UTF-8")+"="+ URLEncoder.encode(String.valueOf(loginId),"UTF-8");

            writer.write(dataToSend);
            writer.flush();
            writer.close(); outStream.close();

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String line;

            while((line=reader.readLine()) != null)
            {
                result.append(line);
            }

            inputStream.close(); reader.close();
            connection.disconnect();

            Log.d("RESULT_OF_PROFILE", "run: "+result);


        } catch (Exception e) {
            Log.e("DB_GET_USERDATA_ERROR", "run: "+e.getMessage(), e);
        }
    }

    public String getResult()
    {
        try {
            this.join();
        } catch (InterruptedException e) {
            Log.d("INTERRUPTED_EXCEPTION", "getResult: "+e.getMessage());
        }
        return result.toString();
    }
}


