package com.kml.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kml.R;
import com.kml.data.app.FileFactory;
import com.kml.data.app.KmlApp;
import com.kml.data.externalDbOperations.DbLogin;

public class LoginScreen extends AppCompatActivity
{
    private EditText editTextLogin, editTextPassword;
    private Button logIn;
    private FileFactory cache;
    private ProgressBar progressBar;
    public static boolean isLog;
    private SwitchCompat rememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        cache = new FileFactory(this);
        editTextLogin = findViewById(R.id.login);
        editTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.login_screen_progres_bar);
        rememberMe = findViewById(R.id.login_remember_me);

        editTextLogin.setSelectAllOnFocus(true);
        editTextPassword.setSelectAllOnFocus(true);

        logIn = findViewById(R.id.log_in_button);
        logIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        logIn();
                    }
                }).start();
            }
        });
    }

    protected void logIn()
    {
        long timeOnStart = SystemClock.elapsedRealtime();
        Intent intent = new Intent(this, MainActivity.class);
        int toast = 0;
        String result;

        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();

        DbLogin dbLogin = new DbLogin(login, password);
        dbLogin.start();
        result = dbLogin.getResult();


        long timeOnEnd = SystemClock.elapsedRealtime();
        long elapsedTime = timeOnEnd - timeOnStart;

        if (result.contains("true")) {
            decideAboutSavingLogData(login, password);
            getLoginId(result);
            startActivity(intent);
        } else if (elapsedTime > 6000) {
            toast = R.string.external_database_unavailable;
        } else {
            toast = R.string.wrong_form_info;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        final int finalToast = toast;
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if(finalToast != 0)
                Toast.makeText(LoginScreen.this, finalToast, Toast.LENGTH_SHORT).show();

                editTextPassword.setText("");
                progressBar.setVisibility(ProgressBar.GONE);
            }
        });

    }

    private void decideAboutSavingLogData(String login, String password)
    {
        if (rememberMe.isChecked()) {
            cache.saveStateToFile(login + ";" + password, FileFactory.DATA_TXT);
        } else {
            cache.saveStateToFile("", FileFactory.DATA_TXT); //clear File
        }

        cache.saveStateToFile(String.valueOf(rememberMe.isChecked()), FileFactory.LOGIN_KEEP_SWITCH_CHOICE_TXT);
    }


    private void getLoginId(String result)
    {
        result = result.substring(result.length() - 3);
        result = result.trim();

        KmlApp.loginId = Integer.parseInt(result);
    }

    @Override
    protected void onResume()
    {
        tryToAutoLogIn();
        isLog = true;
        restoreSwitchState();
        super.onResume();
    }

    private void tryToAutoLogIn()
    {
        if (cache.readFromFile(FileFactory.DATA_TXT) != null) {
            if (cache.readFromFile(FileFactory.DATA_TXT).contains(";")) {
                String[] content = cache.readFromFile(FileFactory.DATA_TXT).split(";");
                editTextLogin.setText(content[0]);
                editTextPassword.setText(content[1]);
            }
        }
    }

    private void restoreSwitchState()
    {
        String fromFile = cache.readFromFile(FileFactory.LOGIN_KEEP_SWITCH_CHOICE_TXT);

        if (fromFile != null) {
            boolean previousSwitchState = Boolean.parseBoolean(fromFile);
            rememberMe.setChecked(previousSwitchState);
            Log.d("MYQUICKTAG", "restoreSwitchState: " + fromFile + "  AND  " + previousSwitchState);
        }


    }
}
