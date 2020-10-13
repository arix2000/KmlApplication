package com.kml.data.externalDbOperations;

import android.util.Log;

import com.kml.data.app.KmlApp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class DbAddingToChosen extends ExternalDbHelper
{
    private final String fileName = "addTimeOfWorkToChosen.php";

    private String address;
    private String result;
    private HttpURLConnection conn;
    private String ids;
    private String workName;
    private String volunteersName;
    private int minutes;
    private int hours;
    private float workTime;



    public DbAddingToChosen(String ids, String volunteersName, String workName, int minutes, int hours)
    {
        address = BASE_URL+fileName;

        this.ids = ids;
        this.volunteersName = "Dodano godziny wybranym: "+volunteersName;
        this.workName = workName;
        this.minutes = minutes;
        this.hours = hours;
    }

    @Override
    public void run()
    {
        try {
            conn = setConnection(address);
            convertTimeToSend();
            sendData();
            result = readResult(conn);
        } catch (IOException e) {
            Log.d("IO_EXCEPTION", "run: " + e.getMessage());
        }
    }

    private void convertTimeToSend()
    {
        workTime = hours + (float) minutes / 60;
        // zaokraglenie do dwóch miejsc po przecinku, inny sposób po prostu nie działał
        workTime = workTime *100;
        workTime = Math.round(workTime);
        workTime = workTime / 100;
    }

    private void sendData() throws IOException
    {
        String readAbleWorkTime = hours+"h "+minutes+"min";

        OutputStream outStream = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
        String dataToSend = URLEncoder.encode("workTime","UTF-8")+"="+URLEncoder.encode(String.valueOf(workTime),"UTF-8")
                +"&&"+URLEncoder.encode("ids","UTF-8")+"="+ URLEncoder.encode(ids,"UTF-8")
                +"&&"+URLEncoder.encode("workName","UTF-8")+"="+URLEncoder.encode(workName,"UTF-8")
                +"&&"+URLEncoder.encode("volunteersName","UTF-8")+"="+ URLEncoder.encode(volunteersName,"UTF-8")
                +"&&"+URLEncoder.encode("readAbleWorkTime","UTF-8")+"="+ URLEncoder.encode(readAbleWorkTime,"UTF-8")
                +"&&"+URLEncoder.encode("firstName","UTF-8")+"="+ URLEncoder.encode(KmlApp.firstName,"UTF-8")
                +"&&"+URLEncoder.encode("lastName","UTF-8")+"="+ URLEncoder.encode(KmlApp.lastName,"UTF-8");

        writer.write(dataToSend);
        writer.flush();
        writer.close(); outStream.close();
    }

    public String getResult()
    {
        try {
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
