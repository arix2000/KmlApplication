package com.kml.data.app;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileFactory {

    public static final String ERROR_TAG = "ERROR_TAG";

    public static final String CURRENT_TIME_TXT = "currentTime.txt";
    public static final String DATA_TXT = "data.txt";
    public static final String PROFILE_PHOTO_PATH_TXT = "profilePhotoPath.txt";
    public static final String PROFILE_KEEP_DATA_TXT = "profileKeepData.txt";
    public static final String HISTORY_KEEP_DATA_TXT = "historyKeepData.txt";
    public static final String LOGIN_KEEP_SWITCH_CHOICE_TXT = "loginKeepSwitchChoice.txt";
    private final Context context;

    public FileFactory(Context context) {
        this.context = context;
    }

    public void saveStateToFile(String toSave, String filename) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(toSave.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(ERROR_TAG, "saveStateToFile: " + e.getMessage());
        } catch (IOException e) {
            Log.d(ERROR_TAG, "saveStateToFile: " + e.getMessage());
        }
    }

    public void clearFileState(String filename) {
        saveStateToFile("-1", filename);
    }

    public void clearFileContent(String filename) {
        saveStateToFile("", filename);
    }

    @NotNull
    public String readFromFile(String filename) {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String content;
            while ((content = br.readLine()) != null) {
                sb.append(content);
            }
            fis.close();

        } catch (IOException e) {
            Log.d(ERROR_TAG, "onClick: " + e.getMessage());
        }
        return sb.toString();
    }
}
