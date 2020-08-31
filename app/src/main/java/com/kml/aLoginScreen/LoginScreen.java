package com.kml.aLoginScreen;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.concurrent.ExecutionException;

public class LoginScreen extends AppCompatActivity
{
    private EditText editTextLogin, editTextPassword;
    private Button logIn;
    private FileFactory cache;
    private ProgressBar progressBar;
    public static boolean isLog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        cache = new FileFactory(this);
        editTextLogin = findViewById(R.id.login);
        editTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.login_screen_progres_bar);

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
                        try {
                            logIn();
                        } catch (Exception e) {
                            Log.d("LOGIN_EXCEPTION", "run: "+e.getMessage());
                        }
                    }
                }).start();
            }
        });
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

    protected void logIn() throws ExecutionException, InterruptedException
    {
        long timeOnStart = SystemClock.elapsedRealtime();
        Intent intent = new Intent(this, MainActivity.class);
        String toastTXT = "";
        String result;

        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();

        DbLogin dbConnect = new DbLogin();
        dbConnect.execute(login, password);
        result = dbConnect.get();

        long timeOnEnd = SystemClock.elapsedRealtime();
        long elapsedTime = timeOnEnd - timeOnStart;

        if (result.contains("true")) {
            cache.saveStateToFile(login + ";" + password, FileFactory.DATA_TXT);
            getLoginId(result);
            startActivity(intent);
        } else if (elapsedTime > 6000) {
            toastTXT = "Brak połączenia z bazą danych! Spróbuj ponownie później";
        } else {
            toastTXT = "Nieprawidłowy login, hasło lub brak połaczenia z intenetem!";
            Log.d("SIEMA", "zaloguj: "+result);
        }

        Handler handler = new Handler(Looper.getMainLooper());
        final String finalToastTXT = toastTXT;
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if(!finalToastTXT.isEmpty())
                Toast.makeText(LoginScreen.this, finalToastTXT, Toast.LENGTH_SHORT).show();

                editTextPassword.setText("");
                progressBar.setVisibility(ProgressBar.GONE);
            }
        });

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
        super.onResume();
    }
}
