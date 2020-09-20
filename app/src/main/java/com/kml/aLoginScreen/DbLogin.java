package com.kml.aLoginScreen;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DbLogin extends AsyncTask<String,Void,String>
{

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        //only for testing dialog_about_app
        //dialog_about_app= new AlertDialog.Builder(context).create();
        //dialog_about_app.setTitle("login Status");
        //dialog_about_app.show();
    }

    @Override
    protected String doInBackground(String... strings) //Old way
    {
        StringBuilder result = new StringBuilder();
        String login = strings[0];
        String password = strings[1];

        String address = "http://sobos.ssd-linuxpl.com/login.php";

        try
        {
            URL connUrl = new URL(address);

            HttpURLConnection httpConnection = (HttpURLConnection)connUrl.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);

            OutputStream outStream = httpConnection.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream,"UTF-8"));
            String dataToSend = URLEncoder.encode("user","UTF-8")+"="+URLEncoder.encode(login,"UTF-8")
                    +"&&"+URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

            writer.write(dataToSend);
            writer.flush();
            writer.close(); outStream.close();

            InputStream inStream = httpConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "ISO-8859-1"));
            String line;

            while((line=reader.readLine())!=null)
            {
                result.append(line);
            }

            inStream.close(); reader.close();
            httpConnection.disconnect();


        }
        catch (Exception e)
        {
            result.append(e.getMessage());
        }

        Log.d("DB_LOGIN_RESULT", "doInBackground: "+result);
        return result.toString();
    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        //only for testing
        //endResult = result;
        //dialog_about_app.setMessage(endResult);

    }

}
