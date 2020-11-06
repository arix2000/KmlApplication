package com.kml.data.externalDbOperations;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class DbChangePass extends ExternalDbHelper {
    private final String fileName = "changePass.php";


    public static final String CHANGE_SUCCESSFUL = "Pomyślnie zmieniono hasło!";
    public static final String CHANGE_FAILED = "Coś poszło nie tak!";

    private String newPassword, oldPassword;
    private int loginId;
    private String toastText;
    private String result;

    public DbChangePass(String newPassword, String oldPassword, int loginId) {
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
        this.loginId = loginId;
    }

    @Override
    public void run() {
        super.run();
        String address = BASE_URL + fileName;

        try {
            Thread.sleep(3000);
            HttpURLConnection conn = setConnection(address);
            sendData(conn);
            result = readResult(conn);

            if (result.equals("1")) {
                toastText = CHANGE_SUCCESSFUL;
            } else if (result.equals("0")) {
                toastText = CHANGE_FAILED;
            } else toastText = result;

            result = toastText;

            invokeOnReceive(result);


        } catch (IOException e) {
            Log.d("IOEXCEPTION_CHANGEPASS", "run: " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void sendData(HttpURLConnection conn) throws IOException {
        OutputStream outSteam = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outSteam, "UTF-8"));
        String dataToSend = URLEncoder.encode("newPassword", "UTF-8") + "=" + URLEncoder.encode(newPassword, "UTF-8")
                + "&&" + URLEncoder.encode("oldPassword", "UTF-8") + "=" + URLEncoder.encode(oldPassword, "UTF-8")
                + "&&" + URLEncoder.encode("loginId", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(loginId), "UTF-8");
        writer.write(dataToSend);
        writer.flush();
        writer.close();
        outSteam.close();
    }

    public String getResult() {
        try {
            this.join();
        } catch (InterruptedException e) {
            Log.d("INTERRUPTED_EXCEPTION", "getResult: " + e.getMessage());
        }
        return result;
    }


}
