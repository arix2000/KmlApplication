package com.kml;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kml.controlPanel.AddToChosenActivity;
import com.kml.controlPanel.Volunteer;

import java.util.ArrayList;

public class SummaryInputData extends AppCompatActivity
{
    ArrayList<Volunteer> chosenVolunteers;
    TextView chosenTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_input_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Dokończ wpisywanie:");

        chosenTextView = findViewById(R.id.summary_input_chosen_volunteers);

        writeChosenVolunteers();


    }

    private void writeChosenVolunteers()
    {
        Intent intent = getIntent();
        chosenVolunteers = intent.getParcelableArrayListExtra(AddToChosenActivity.EXTRA_CHECKED_VOLUNTEERS);
        String chosenVolunteersMerged = createStringFromList(chosenVolunteers);

        chosenTextView.setText(chosenVolunteersMerged);
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