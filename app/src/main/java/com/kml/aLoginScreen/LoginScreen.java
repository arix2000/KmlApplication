package com.kml.aLoginScreen;

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

import com.kml.MainActivity;
import com.kml.R;
import com.kml.aGlobalUses.FileFactory;
import com.kml.aGlobalUses.KmlApp;
import com.kml.aGlobalUses.externalDbOperations.DbLogin;

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
        String toastTXT = "";
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
            toastTXT = "Brak połączenia z bazą danych! Spróbuj ponownie później";
        } else {
            toastTXT = "Nieprawidłowy login, hasło lub brak połaczenia z intenetem!";
            Log.d("SIEMA", "zaloguj: " + result);
        }

        Handler handler = new Handler(Looper.getMainLooper());
        final String finalToastTXT = toastTXT;
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (!finalToastTXT.isEmpty())
                    Toast.makeText(LoginScreen.this, finalToastTXT, Toast.LENGTH_SHORT).show();
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
