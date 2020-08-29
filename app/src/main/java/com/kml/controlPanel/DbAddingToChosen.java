package com.kml.controlPanel;

import android.util.Log;

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

public class DbAddingToChosen extends Thread
{
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
        address = "http://sobos.ssd-linuxpl.com/addTimeOfWorkToChosen.php";

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
            setConnection();
            convertTimeToSend();
            sendData();
            result = readResults();
        } catch (IOException e) {
            Log.d("IO_EXCEPTION", "run: " + e.getMessage());
        }
        Log.d("RESULT_FROM_DB", "run: "+result);
    }

    private void setConnection() throws IOException
    {
        URL url = new URL(address);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
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

    private String readResults() throws IOException
    {
        InputStream inputStream = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        String line;
        StringBuilder readResult = new StringBuilder();

        while((line=reader.readLine()) != null)
        {
            readResult.append(line);
        }
        inputStream.close(); reader.close();
        conn.disconnect();

        return readResult.toString();
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
