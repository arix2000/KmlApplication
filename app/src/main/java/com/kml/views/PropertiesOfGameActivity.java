package com.kml.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kml.R;
import com.kml.views.SearchEngineFragment;

public class PropertiesOfGameActivity extends AppCompatActivity
{
    ScrollView scrollView;
    TextView textViewName;
    TextView textViewDescription;
    TextView textViewRequirements;
    TextView textViewNumberOfKids;
    TextView textViewKidsAge;
    TextView textViewPlace;
    TextView textViewTypeOfGames;
    TextView textViewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties_game);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        objInstance();
        Intent takenData = getIntent();
        String name = takenData.getStringExtra(SearchEngineFragment.EXTRA_NAME);
        String description = takenData.getStringExtra(SearchEngineFragment.EXTRA_DESCRIPTION);
        String requirements = takenData.getStringExtra(SearchEngineFragment.EXTRA_REQUIREMENTS);
        String numberOfKids = takenData.getStringExtra(SearchEngineFragment.EXTRA_NUMBER_OF_KIDS);
        String kidsAge = takenData.getStringExtra(SearchEngineFragment.EXTRA_KIDS_AGE);
        String place = takenData.getStringExtra(SearchEngineFragment.EXTRA_PLACE);
        String typeOfGames = takenData.getStringExtra(SearchEngineFragment.EXTRA_TYPE_OF_GAMES);
        String category = takenData.getStringExtra(SearchEngineFragment.EXTRA_CATEGORY);

        int getColor = setColorForBackground(category);

        scrollView.setBackgroundColor(getColor);

        textViewName.setText(name);
        textViewDescription.setText(description);
        textViewRequirements.setText(requirements.replaceAll(";", ","));
        textViewNumberOfKids.setText(numberOfKids.replaceAll(";", ","));
        textViewKidsAge.setText(kidsAge.replaceAll(";", ","));
        textViewPlace.setText(place.replaceAll(";", ","));
        textViewTypeOfGames.setText(typeOfGames.replaceAll(";", ","));
        textViewCategory.setText(category);



    }

    private int setColorForBackground(String category)
    {
        int color;

        switch (category)
        {
            case "Zabawy": color = getResources().getColor(R.color.colorZabaw); break;
            case "Drużynowe": color = getResources().getColor(R.color.colorDruzynowe); break;
            case "Lina": color = getResources().getColor(R.color.colorLina); break;
            case "Chusta": color = getResources().getColor(R.color.colorChusta); break;
            case "Tańce": color = getResources().getColor(R.color.colorTance); break;
            default: color = 0; Log.d("ColorBackground","Color is 0!!!!!");
        }

        return color;
    }

    private void objInstance()
    {
        scrollView = findViewById(R.id.properties_container);
        textViewName = findViewById(R.id.prop_name);
        textViewDescription = findViewById(R.id.prop_description);
        textViewRequirements = findViewById(R.id.prop_requirements);
        textViewNumberOfKids = findViewById(R.id.prop_number_of_kids);
        textViewKidsAge = findViewById(R.id.prop_kids_age);
        textViewPlace = findViewById(R.id.prop_place);
        textViewTypeOfGames = findViewById(R.id.prop_type_of_games);
        textViewCategory = findViewById(R.id.prop_category);
    }
}