package com.kml.data.externalDbOperations;

import android.util.Log;

import java.net.HttpURLConnection;

public class DbGetAllUsersData extends ExternalDbHelper
{
    private final String fileName = "getAllDataAboutUser.php";

    String address;
    String result;
    HttpURLConnection conn;

    public DbGetAllUsersData()
    {
        address = BASE_URL+fileName;
    }

    @Override
    public void run()
    {
        String dataFromDb;

        conn = setConnection(address);
        dataFromDb = readResult(conn);

        setResult(dataFromDb);
    }

    private void setResult(String result)
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
        return result;
    }
}
