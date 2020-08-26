package com.kml.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.kml.externalDbOperations.DbChangePass;
import com.kml.externalDbOperations.DbGetUserData;
import com.kml.KmlApp;
import com.kml.LoginScreen;
import com.kml.R;
import com.kml.supportClasses.FileOperations;


public class ProfileFragment extends Fragment
{
    View root;
    public static final int PICK_IMAGE = 1;
    ImageView profilePhoto, changePassImageView;
    FileOperations dataFile;
    TextView textViewFullName, textViewJoinYear, textViewTimeOfWorkSeason, textViewTimeOfWorkMonth, textViewSections, textViewType;
    String firstName, lastName, joinYear, timeOfWorkSeason, sections, type, fullName, timeOfWorkMonth;
    ProgressBar progressBar;
    EditText editTextOldPassword, editTextNewPassword;
    Dialog dialogChangePass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewFullName = root.findViewById(R.id.profile_full_name);
        textViewJoinYear = root.findViewById(R.id.profile_join_year);
        textViewTimeOfWorkSeason = root.findViewById(R.id.profile_time_of_work_season);
        textViewTimeOfWorkMonth = root.findViewById(R.id.profile_time_of_work_month);
        textViewSections = root.findViewById(R.id.profile_sections);
        textViewType = root.findViewById(R.id.profile_type);
        progressBar = root.findViewById(R.id.profile_progress_bar);
        changePassImageView = root.findViewById(R.id.change_pass);

        dataFile = new FileOperations(root.getContext());
        profilePhoto = root.findViewById(R.id.profile_photo);
        profilePhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent imageIntent = new Intent();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    imageIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                } else
                    imageIntent.setAction(Intent.ACTION_PICK);

                imageIntent.setType("image/*");
                startActivityForResult(imageIntent, PICK_IMAGE);
            }
        });

        changePassImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showDialogToChangePass();
            }
        });

        progressBar.setVisibility(ProgressBar.VISIBLE);
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                setDataFromExternalDatabase();
            }
        });
        thread.start();

        return root;
    }

    private void showDialogToChangePass()
    {
        dialogChangePass = new Dialog(root.getContext());
        dialogChangePass.setContentView(R.layout.dialog_change_pass);
        dialogChangePass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogChangePass.show();

        editTextOldPassword = dialogChangePass.findViewById(R.id.dialog_old_password);
        editTextNewPassword = dialogChangePass.findViewById(R.id.dialog_new_password);

        Button btnAccept = dialogChangePass.findViewById(R.id.btn_dialog_change_pass_accept);
        Button btnCancel = dialogChangePass.findViewById(R.id.btn_dialog_change_pass_cancel);

        btnAccept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                changePass();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialogChangePass.dismiss();
            }
        });
    }

    private void changePass()
    {
        final ProgressBar dialogProgressBar = dialogChangePass.findViewById(R.id.dialog_change_password_progress_bar);
        dialogProgressBar.setVisibility(View.VISIBLE);


        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Handler handler = new Handler(Looper.getMainLooper());
                String oldPassword = editTextOldPassword.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();
                if (newPassword.isEmpty() || oldPassword.isEmpty()) {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(root.getContext(), "żadne z pól nie może być puste!", Toast.LENGTH_SHORT).show();
                            dialogProgressBar.setVisibility(View.GONE);
                        }
                    });
                    return;
                } else if (newPassword.length() > 64) {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(root.getContext(), "hasło nie może mieć wiecej niż 64 znaki!", Toast.LENGTH_SHORT).show();
                            dialogProgressBar.setVisibility(View.GONE);
                        }
                    });
                    return;
                }


                DbChangePass dbChangePass = new DbChangePass(dialogChangePass.getContext(), newPassword, oldPassword, KmlApp.loginId);
                dbChangePass.start();
                String result = dbChangePass.getResult();
                if (result.equals("1")) {
                    dialogChangePass.dismiss();
                } else {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            editTextOldPassword.setText("");
                            editTextNewPassword.setText("");
                            dialogProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }).start();
    }

    private void setDataFromExternalDatabase()
    {
        Handler handler = new Handler(Looper.getMainLooper());

        DbGetUserData dbGetUserData = new DbGetUserData();
        dbGetUserData.start();

        final String result = dbGetUserData.getResult();

        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (result == null || result.trim().isEmpty()) {

                    fillDataFromFile();

                } else {
                    fillDataFromDatabase(result);
                }
                progressBar.setVisibility(ProgressBar.GONE);
                writeDataOnLayout(); //write data on layout with little changes :D
                KmlApp.firstName = firstName;
                KmlApp.lastName = lastName;
                if (LoginScreen.isLog) {
                    String toastWelcomeText;
                    if (KmlApp.firstName.equals("Marta") && KmlApp.lastName.equals("Nowińska"))
                        toastWelcomeText = "<3 Dzień dobry piękna istotko! <3";
                    else if (KmlApp.firstName.equals("Sebastian") && KmlApp.lastName.equals("Peret"))
                        toastWelcomeText = "Dzień dobry Prezesie!";
                    else
                        toastWelcomeText = "Dzień dobry " + KmlApp.firstName + "!";

                    if(getActivity() != null)
                    Snackbar.make(getActivity().findViewById(android.R.id.content), toastWelcomeText, Snackbar.LENGTH_SHORT).show();
                    LoginScreen.isLog = false;
                }
            }
        });

    }

    private void writeDataOnLayout()
    {
        String readAbleTime = convertToReadable(timeOfWorkSeason);
        String readAbleTimeMonth = convertToReadable(timeOfWorkMonth);
        String changedSection = sections.replaceAll("-", ",");

        if (changedSection.contains("Wolontariusz") && changedSection.contains("Lider"))
            changedSection = changedSection.substring(0, changedSection.indexOf("Wolontariusz")) + "\n\n" + changedSection.substring(changedSection.indexOf("Wolontariusz"));


        textViewFullName.setText(fullName);
        textViewType.setText(type);
        textViewJoinYear.setText(joinYear);
        textViewTimeOfWorkSeason.setText(readAbleTime);
        textViewSections.setText(changedSection);
        textViewTimeOfWorkMonth.setText(readAbleTimeMonth);

        dataFile.saveStateToFile(firstName + ";" + lastName + ";" + joinYear + ";" + timeOfWorkSeason
                + ";" + sections + ";" + type + ";" + timeOfWorkMonth, FileOperations.PROFILE_KEEP_DATA_TXT);
    }

    private String convertToReadable(String timeOfWork)
    {
        String convertedTime;
        float workTimeFloat = Float.parseFloat(timeOfWork);
        int hours = (int) workTimeFloat;
        workTimeFloat = workTimeFloat - hours;
        workTimeFloat = Math.abs(workTimeFloat);

        int helpingInteger = Math.round(workTimeFloat * 100);
        workTimeFloat = (float) helpingInteger / 100;
        int minutes = Math.round(workTimeFloat * 60);

        Log.d("CONVERT_TO_READABLE", "convertToReadable: " + workTimeFloat + " ; " + hours + " ; " + minutes);

        convertedTime = hours + " godz " + minutes + " min";
        return convertedTime;
    }

    private void fillDataFromDatabase(String result)
    {
        String[] splitResult = result.split(";");
        if (splitResult.length == 7) {
            firstName = splitResult[0];
            lastName = splitResult[1];
            joinYear = splitResult[2];
            timeOfWorkSeason = splitResult[3];
            sections = splitResult[4];
            type = splitResult[5];
            timeOfWorkMonth = splitResult[6];
            fullName = firstName + " " + lastName;
        } else
            Toast.makeText(root.getContext(), "Wystąpił błąd podczas połączenia z bazą spróbuj ponownie później", Toast.LENGTH_SHORT).show();
        Log.d("ARRAY_LENGTH", "setDataFromExternalDatabase: " + splitResult.length);
    }

    private void fillDataFromFile()
    {
        Toast.makeText(root.getContext(), "Brak połącznia z internetem! wczytano ostatnie dane", Toast.LENGTH_SHORT).show();
        String dataFromFile = dataFile.readFromFile(FileOperations.PROFILE_KEEP_DATA_TXT);

        if (!dataFromFile.isEmpty() || !dataFromFile.equals("")) {
            String[] splitData = dataFromFile.split(";");
            firstName = splitData[0];
            lastName = splitData[1];
            joinYear = splitData[2];
            timeOfWorkSeason = splitData[3];
            sections = splitData[4];
            type = splitData[5];
            timeOfWorkMonth = splitData[6];
            fullName = firstName + " " + lastName;
        } else
            Toast.makeText(root.getContext(), "Coś poszło nie tak!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(root.getContext(), "???????????", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (data != null)
            dataFile.saveStateToFile(data.getData().toString(), FileOperations.PROFILE_PHOTO_PATCH_TXT);
    }

    @Override
    public void onResume()
    {
        if (dataFile.readFromFile(FileOperations.PROFILE_PHOTO_PATCH_TXT) != null) {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    String path = dataFile.readFromFile(FileOperations.PROFILE_PHOTO_PATCH_TXT);
                    if (path != null && !path.isEmpty()) {
                        final Uri photoUri = Uri.parse(path);

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try {
                                    profilePhoto.setImageURI(photoUri);
                                } catch (Exception e) {
                                    Log.e("PERMISSIONERROR", "onResume: " + e.getMessage());
                                    dataFile.saveStateToFile("", FileOperations.PROFILE_PHOTO_PATCH_TXT); //clear state
                                }
                            }
                        }, 300);
                    }
                }
            }).start();
        }

        super.onResume();
    }
}
