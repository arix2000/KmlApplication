package com.kml.controlPanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import com.kml.aGlobalUses.KmlApp;
import com.kml.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddToChosenActivity extends AppCompatActivity
{

    RecyclerView recyclerView;
    VolunteerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_choosed);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Dodaj godziny wybranym wolontariuszom");
        KmlApp.isFromControlPanel = true;


        DbGetAllUsersData dbGetAllUsersData = new DbGetAllUsersData();
        dbGetAllUsersData.start();
        String result = dbGetAllUsersData.getResult();

        fillRecycleView(result);


    }

    private void fillRecycleView(String result)
    {
        List<Volunteer> volunteers = createListFromJson(result);
        createRecycleView();
        adapter.setVolunteers(volunteers);
        createOnItemClickListener();
    }

    private void createRecycleView()
    {
        recyclerView = findViewById(R.id.control_panel_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new VolunteerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void createOnItemClickListener()
    {
        adapter.setOnItemClickListener(new VolunteerAdapter.OnItemClickListener()
        {
            @Override
            public void OnItemClick(Volunteer volunteer)
            {
                if(volunteer.isChecked())
                {
                    volunteer.setChecked(false);
                    removeFromCheckedUsers(volunteer);
                }
                else
                {
                    volunteer.setChecked(true);
                    addToCheckedUsers(volunteer);
                }
            }
        });
    }

    private void addToCheckedUsers(Volunteer volunteer)
    {

    }

    private void removeFromCheckedUsers(Volunteer volunteer)
    {

    }

    private List<Volunteer> createListFromJson(String result)
    {

        List<Volunteer> volunteers = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            volunteers = fillArrayFromJson(jsonArray);
        } catch (JSONException e) {
            Log.d("RESULT_FROM_JSON", "onException: "+e.getMessage());
        }
        return volunteers;
    }

    private List<Volunteer> fillArrayFromJson(JSONArray jsonArray) throws JSONException
    {
        JSONObject jsonObject;
        List<Volunteer> volunteers = new ArrayList<>();
        for (int i=0;i<=jsonArray.length()-1;i++)
        {
            jsonObject = jsonArray.getJSONObject(i);
            volunteers.add(new Volunteer
                    (jsonObject.getInt("id"),jsonObject.getString("imie"),jsonObject.getString("nazwisko"), false));
        }
        return volunteers;
    }
}