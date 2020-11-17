package com.kml.data.externalDbOperations;

import android.util.Log;

import com.kml.data.app.KmlApp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class DbGetWorksHistory extends ExternalDbHelper
{
    private final String fileName = "getWorkHistory.php";

    private String address;
    private String result;
    private HttpURLConnection conn;

    public DbGetWorksHistory()
    {
        address = BASE_URL+fileName;;
    }

    @Override
    public void run()
    {
        conn = setConnection(address);
        sendData();
        result = readResult(conn);
    }

    private void sendData()
    {
        try {
            OutputStream outStream = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
            String dataToSend = URLEncoder.encode("firstName", "UTF-8") + "=" + URLEncoder.encode(KmlApp.firstName, "UTF-8")
                    + "&&" + URLEncoder.encode("lastName", "UTF-8") + "=" + URLEncoder.encode(KmlApp.lastName, "UTF-8");

            writer.write(dataToSend);
            writer.flush();
            writer.close();
            outStream.close();

        } catch (IOException e) {
            Log.d(IO_EXCEPTION_TAG, "sendData: " + e.getMessage());
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
