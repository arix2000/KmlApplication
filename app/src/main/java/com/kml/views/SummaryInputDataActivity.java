package com.kml.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kml.R;
import com.kml.data.externalDbOperations.DbAddingToChosen;
import com.kml.data.models.Volunteer;

import java.util.ArrayList;

public class SummaryInputDataActivity extends AppCompatActivity
{
    ArrayList<Volunteer> chosenVolunteers;
    TextView chosenTextView;
    EditText hoursEditText;
    EditText minutesEditText;
    EditText workNameEditText;
    DbAddingToChosen dbAddingToChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_selected);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Dokończ wpisywanie:");

        chosenTextView = findViewById(R.id.summary_activity_chosen_volunteers);
        writeChosenVolunteers();
        hoursEditText = findViewById(R.id.summary_activity_hours);
        minutesEditText = findViewById(R.id.summary_activity_minutes);
        workNameEditText = findViewById(R.id.summary_activity_work_name);

        Button sendWorkButton = findViewById(R.id.summary_activity_send_work);
        sendWorkButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String hours = hoursEditText.getText().toString();
                String minutes = minutesEditText.getText().toString();
                String workName = workNameEditText.getText().toString();
                if(hours.trim().isEmpty() || minutes.trim().isEmpty() || workName.trim().isEmpty())
                {
                    Toast.makeText(SummaryInputDataActivity.this, R.string.no_empty_fields, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addWorkToDatabase(Integer.parseInt(hours), Integer.parseInt(minutes), workName);
                    resetPools();
                    backToChose();
                }
            }
        });

    }

    private void backToChose()
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void resetPools()
    {
        hoursEditText.setText("");
        minutesEditText.setText("");
        workNameEditText.setText("");
    }

    private void writeChosenVolunteers()
    {
        Intent intent = getIntent();
        chosenVolunteers = intent.getParcelableArrayListExtra(SelectVolunteersActivity.EXTRA_CHECKED_VOLUNTEERS);
        String chosenVolunteersMerged = createStringFromList(chosenVolunteers);
        chosenVolunteersMerged = chosenVolunteersMerged.substring(0,chosenVolunteersMerged.length()-2)+".";
        chosenTextView.setText(chosenVolunteersMerged);
    }

    private void addWorkToDatabase(int hours, int minutes, String workName)
    {
        sendWorkToDb(hours,minutes,workName);
        receiveResultFromDb(dbAddingToChosen);
    }

    private void sendWorkToDb(int hours, int minutes, String workName)
    {
        String ids = getIdsStringFromVolunteers();
        String volunteersNames = createStringFromList(chosenVolunteers);

        dbAddingToChosen = new DbAddingToChosen(ids,volunteersNames,workName,minutes,hours);
        dbAddingToChosen.start();
    }

    private void receiveResultFromDb(DbAddingToChosen dbAddingToChosen)
    {
        String result = dbAddingToChosen.getResult();
        if(result.equals("true"))
        {
            Toast.makeText(this, R.string.adding_work_confirmation, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, R.string.adding_work_error, Toast.LENGTH_SHORT).show();
        }
    }

    private String getIdsStringFromVolunteers()
    {
        StringBuilder ids = new StringBuilder();
        String cache;
        for(Volunteer volunteer : chosenVolunteers)
        {
            if(!ids.toString().contains(String.valueOf(volunteer.getId()))) {
                cache = volunteer.getId() + ",";
                ids.append(cache);
            }
        }
        ids.replace(ids.length()-1,ids.length(),"");

        return ids.toString();
    }



    private String createStringFromList(ArrayList<Volunteer> chosenVolunteers)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(Volunteer volunteer: chosenVolunteers)
        {
            String oneVolunteer = volunteer.getFirstName()+" "+volunteer.getLastName()+", ";
            stringBuilder.append(oneVolunteer);
        }
        return stringBuilder.toString();
    }
}