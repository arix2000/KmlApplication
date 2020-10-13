package com.kml.views;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.kml.data.app.KmlApp;
import com.kml.data.app.FileFactory;
import com.kml.R;
import com.kml.data.externalDbOperations.DbSendWork;
import com.kml.data.services.TimerService;

import java.util.Timer;
import java.util.TimerTask;


public class WorkTimerFragment extends Fragment
{
    private View root;
    private ImageView timerCircle;
    private ImageButton btnStartWork, btnEndWork, btnResetWork, btnAddWork;
    private Animation circleAnim;
    private TextView textViewSeconds, textViewMinutes, textViewHours;
    private CountingThread countingThread;
    private FileFactory fileFactory;
    private String workName, workDescription, secondsFormatted, minutesFormatted, hoursFormatted;
    private int minutes, seconds, hours;
    private long lastClickTime = 0;
    private boolean isTimerRunning, isThreadAlive, exitThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_work_timer, container, false);
        isTimerRunning = false;

        timerCircle = root.findViewById(R.id.timer_circle);
        circleAnim = AnimationUtils.loadAnimation(root.getContext(), R.anim.timer_circle_anim);
        textViewSeconds = root.findViewById(R.id.timer_counter_seconds);
        textViewMinutes = root.findViewById(R.id.timer_counter_minutes);
        textViewHours = root.findViewById(R.id.timer_counter_hours);
        fileFactory = new FileFactory(root.getContext());

        btnAddWork = root.findViewById(R.id.btn_add_work);
        btnAddWork.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showDialogToInstantAddWork();
            }
        });

        btnStartWork = root.findViewById(R.id.btn_start_work);
        btnStartWork.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //prevent double click
                if (SystemClock.elapsedRealtime() - lastClickTime < 500) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();

                if (!isTimerRunning) {
                    if (MainActivity.isFirstClick) {
                        if (fileFactory.readFromFile(FileFactory.CURRENT_TIME_TXT) != null
                                && fileFactory.readFromFile(FileFactory.CURRENT_TIME_TXT).contains(";")) {
                            showDialogToRestore();
                        } else startCounting();
                        MainActivity.isFirstClick = false;
                    } else {
                        startCounting();
                    }

                } else {
                    pauseCounting();
                }


            }
        });

        btnEndWork = root.findViewById(R.id.btn_end_work);
        btnEndWork.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (hours == 0 && minutes < 1) {
                    Toast.makeText(getContext(), "Nie odliczono ani jednej minuty czasu!", Toast.LENGTH_SHORT).show();
                } else
                    showDialogToSetWork();
            }
        });

        btnResetWork = root.findViewById(R.id.btn_reset_work);
        btnResetWork.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (seconds != 0 || minutes != 0 || hours != 0)
                    showDialogToMakeSure();
            }
        });

        return root;
    }

    @Override
    public void onResume()
    {

        returnStateFromService();
        setTimeOnLayout();

        Log.d("TAG_TAG", "onResume:  " + TimerService.seconds + "  " + TimerService.minutes + "  " + TimerService.hours);

        root.getContext().stopService(new Intent(root.getContext(), TimerService.class));
        if (TimerService.wasPlayClicked) {
            startCounting();
        }
        TimerService.exitServiceThread = true;
        super.onResume();
    }


    @Override
    public void onPause()
    {
        super.onPause();

        Intent intent = new Intent(root.getContext(), TimerService.class);
        TimerService.wasPlayClicked = isTimerRunning;
        exitThread = true;
        saveStateToService();

        if (isTimerRunning) {
            TimerService.exitServiceThread = false;
            root.getContext().startService(intent);
        }
    }


    @Override
    public void onStop()
    {
        if (!isTimerRunning && hours > 0 || minutes > 0 || seconds >= 10) {
            fileFactory.saveStateToFile(seconds + ";" + minutes + ";" + hours, FileFactory.CURRENT_TIME_TXT);
        }
        super.onStop();
    }

    private void startCounting()
    {
        btnStartWork.setImageResource(R.drawable.ic_pause);
        exitThread = false;
        countingThread = new CountingThread();
        timerCircle.startAnimation(circleAnim);
        isTimerRunning = true;
        countingThread.start();
        fileFactory.clearFileState(FileFactory.CURRENT_TIME_TXT);
    }

    private void pauseCounting()
    {
        btnStartWork.setImageResource(R.drawable.ic_play);
        exitThread = true;
        timerCircle.clearAnimation();
        isTimerRunning = false;
    }

    private void resetCounting()
    {
        writeTimeOnLayout("00", "00:", "00:");
        seconds = 0;
        minutes = 0;
        hours = 0;
        pauseCounting();
        fileFactory.clearFileState(FileFactory.CURRENT_TIME_TXT);
    }

    private void writeTimeOnLayout(String secondsFormatted, String minutesFormatted, String hoursFormatted)
    {
        textViewSeconds.setText(secondsFormatted);
        textViewMinutes.setText(minutesFormatted);
        textViewHours.setText(hoursFormatted);
    }

    private void setTimeOnLayout()
    {
        if (seconds < 10) {
            secondsFormatted = "0" + seconds;
        } else if (seconds == 60) {
            secondsFormatted = "00";
        } else {
            secondsFormatted = String.valueOf(seconds);
        }

        if (minutes < 10) {
            minutesFormatted = "0" + minutes + ":";
        } else if (minutes == 60) {
            minutesFormatted = "00:";
        } else {
            minutesFormatted = minutes + ":";
        }

        if (hours < 10) {
            hoursFormatted = "0" + hours + ":";
        } else if (hours == 60) {
            hoursFormatted = "00:";
        } else {
            hoursFormatted = hours + ":";
        }

        writeTimeOnLayout(secondsFormatted, minutesFormatted, hoursFormatted);

    }

    private void showDialogToSetWork()
    {
        final Dialog dialog = new Dialog(root.getContext());
        dialog.setContentView(R.layout.dialog_new_work);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final EditText workNameEditText = dialog.findViewById(R.id.timer_work_name_instant);
        final EditText workDescriptionEditText = dialog.findViewById(R.id.timer_work_description_instant);

        Button cancel = dialog.findViewById(R.id.dialog_timer_cancel);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        Button confirm = dialog.findViewById(R.id.dialog_timer_confirm);
        confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                workName = workNameEditText.getText().toString();
                workDescription = workDescriptionEditText.getText().toString();
                if (workName.trim().isEmpty() || workDescription.trim().isEmpty()) {
                    Toast.makeText(dialog.getContext(), "Jedno z pól jest puste!", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                DbSendWork dbSendWork = new DbSendWork(workName, workDescription, KmlApp.firstName, KmlApp.lastName, minutes, hours);
                dbSendWork.start();

                boolean result = dbSendWork.getResult();

                if (result) {
                    Toast.makeText(root.getContext(), "wysłano!", Toast.LENGTH_SHORT).show();
                    resetCounting();
                } else
                    Toast.makeText(root.getContext(), "brak połączenia z internetem!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialogToInstantAddWork()
    {
        final Dialog dialog = new Dialog(root.getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_new_work_instant);
        dialog.show();

        Button confirm = dialog.findViewById(R.id.summary_activity_send_work);
        confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String instantHours, instantMinutes;
                EditText workNameEditText = dialog.findViewById(R.id.timer_work_name_instant);
                EditText workDescriptionEditText = dialog.findViewById(R.id.timer_work_description_instant);
                EditText hoursEditText = dialog.findViewById(R.id.summary_activity_hours);
                EditText minutesEditText = dialog.findViewById(R.id.summary_activity_minutes);

                workName = workNameEditText.getText().toString();
                workDescription = workDescriptionEditText.getText().toString();
                instantHours = hoursEditText.getText().toString();
                instantMinutes = minutesEditText.getText().toString();

                if (workName.trim().isEmpty() || workDescription.trim().isEmpty() || instantHours.trim().isEmpty() || instantMinutes.trim().isEmpty()) {
                    Toast.makeText(dialog.getContext(), "Jedno z pól jest puste!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Integer.parseInt(instantMinutes) > 60) {
                    Toast.makeText(dialog.getContext(), "nie można wpisać więcej niż 60 minut", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();

                DbSendWork dbSendWork = new DbSendWork
                        (workName, workDescription, KmlApp.firstName, KmlApp.lastName, Integer.parseInt(instantMinutes), Integer.parseInt(instantHours));
                dbSendWork.start();

                boolean result = dbSendWork.getResult();

                if (result) {
                    Toast.makeText(root.getContext(), "wysłano!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(root.getContext(), "brak połączenia z internetem!", Toast.LENGTH_SHORT).show();
            }
        });

        Button cancel = dialog.findViewById(R.id.dialog_timer_cancel_instant);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
    }

    private void showDialogToRestore()
    {
        final Dialog dialog = new Dialog(root.getContext());
        dialog.setContentView(R.layout.dialog_restore_from_file);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        Button btnTak = dialog.findViewById(R.id.btn_dialog_restore_yes);
        btnTak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setTimeFromFile();
                dialog.dismiss();
                startCounting();
            }
        });

        Button btnNie = dialog.findViewById(R.id.btn_dialog_restore_no);
        btnNie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                startCounting();
            }
        });

    }

    private void showDialogToMakeSure()
    {
        final Dialog dialog = new Dialog(root.getContext());
        dialog.setContentView(R.layout.dialog_restore_from_file);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        TextView textViewTitle = dialog.findViewById(R.id.y_y_znaleziono_postep);
        textViewTitle.setText("Uwaga!");
        TextView textViewQuestion = dialog.findViewById(R.id.y_y_czy_przywracac);
        textViewQuestion.setText("Napewno chcesz zresetować czas?\nTo bezpowrotnie usunie aktualny czas");

        Button btnTak = dialog.findViewById(R.id.btn_dialog_restore_yes);
        btnTak.setText("resetuj");
        btnTak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                resetCounting();
            }
        });

        Button btnNie = dialog.findViewById(R.id.btn_dialog_restore_no);
        btnNie.setText("anuluj");
        btnNie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

    }

    private void setTimeFromFile()
    {
        String fromFile = fileFactory.readFromFile(FileFactory.CURRENT_TIME_TXT);
        String[] HMS = fromFile.split(";");
        Log.d("ARRAY_HMS", "setTimeFromFile: " + HMS[0]);
        seconds = Integer.parseInt(HMS[0]);
        minutes = Integer.parseInt(HMS[1]);
        hours = Integer.parseInt(HMS[2]);
        setTimeOnLayout();
    }

    private void returnStateFromService()
    {
        seconds = TimerService.seconds;
        minutes = TimerService.minutes;
        hours = TimerService.hours;
    }

    private void saveStateToService()
    {
        TimerService.seconds = seconds;
        TimerService.minutes = minutes;
        TimerService.hours = hours;
    }

    public class CountingThread extends Thread
    {
        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void run()
        {
            if (isThreadAlive) {
                return;
            }

            isThreadAlive = true;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask()
            {
                @Override
                public void run()
                {
                    if (exitThread) {
                        this.cancel();
                        isThreadAlive = false;
                    } else {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                setTimeOnLayoutRealTime();
                            }
                        });
                        seconds += 1;
                    }
                }
            }, 0, 1000);

        }

        private void setTimeOnLayoutRealTime()
        {
            if (seconds < 10) {
                secondsFormatted = "0" + seconds;
            } else if (seconds == 60) {
                secondsFormatted = "00";
            } else {
                secondsFormatted = String.valueOf(seconds);
            }
            textViewSeconds.setText(secondsFormatted);
            if (seconds >= 60) {
                minutes += 1;
                if (minutes < 10) {
                    minutesFormatted = "0" + minutes + ":";
                } else if (minutes == 60) {
                    minutesFormatted = "00:";
                } else {
                    minutesFormatted = minutes + ":";
                }

                textViewMinutes.setText(minutesFormatted);
                seconds = 0;
            }
            if (minutes >= 60) {
                hours += 1;
                if (hours < 10) {
                    hoursFormatted = "0" + hours + ":";
                } else {
                    hoursFormatted = hours + ":";
                }
                textViewHours.setText(hoursFormatted);
                minutes = 0;
            }
        }
    }
}
