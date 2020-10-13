package com.kml.data.externalDbOperations;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class DbChangePass extends ExternalDbHelper
{
    private final String fileName = "changePass.php";

    private Context context;
    private String newPassword, oldPassword;
    private int loginId;
    private String toastText;
    private String result;

    public DbChangePass(Context context, String newPassword, String oldPassword, int loginId)
    {
        this.context = context;
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
        this.loginId = loginId;
    }

    @Override
    public void run()
    {
        super.run();
        String address = BASE_URL+fileName;

        try {

            HttpURLConnection conn = setConnection(address);
            sendData(conn);
            result = readResult(conn);

            if(result.equals("1"))
            {
                toastText = "Pomyślnie zmieniono hasło!";
            }
            else if(result.equals("0"))
            {
                toastText = "Coś poszło nie tak!";
            }
            else toastText = result;

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
                }
            });

            Log.d("RESULT_CHANGE", "run: "+result);
        } catch (IOException e) {
            Log.d("IOEXCEPTION_CHANGEPASS", "run: "+e.getMessage());
        }
    }

    private void sendData(HttpURLConnection conn) throws IOException
    {
        OutputStream outSteam = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outSteam,"UTF-8"));
        String dataToSend = URLEncoder.encode("newPassword","UTF-8")+"="+URLEncoder.encode(newPassword,"UTF-8")
                +"&&"+URLEncoder.encode("oldPassword","UTF-8")+"="+ URLEncoder.encode(oldPassword,"UTF-8")
                +"&&"+URLEncoder.encode("loginId","UTF-8")+"="+URLEncoder.encode(String.valueOf(loginId),"UTF-8");
        writer.write(dataToSend);
        writer.flush();
        writer.close(); outSteam.close();
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
