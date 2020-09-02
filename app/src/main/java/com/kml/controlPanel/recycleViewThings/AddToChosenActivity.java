package com.kml.controlPanel.recycleViewThings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kml.aGlobalUses.KmlApp;
import com.kml.R;
import com.kml.controlPanel.SummaryInputDataActivity;
import com.kml.controlPanel.dbOperations.DbGetAllUsersData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddToChosenActivity extends AppCompatActivity
{
    public static final String EXTRA_CHECKED_VOLUNTEERS = "com.kml.controlPanel.EXTRA_CHECKED_VOLUNTEERS";

    RecyclerView recyclerView;
    VolunteerAdapter adapter;
    ArrayList<Volunteer> checkedVolunteers;
    List<Volunteer> volunteers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_choosed);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Zaznacz wolontariuszy:");
        KmlApp.isFromControlPanel = true;

        checkedVolunteers = new ArrayList<>();
        DbGetAllUsersData dbGetAllUsersData = new DbGetAllUsersData();
        dbGetAllUsersData.start();
        String result = dbGetAllUsersData.getResult();

        fillRecycleView(result);

        volunteers = adapter.getVolunteers();

        TextView selectAllTextView = findViewById(R.id.control_panel_select_all);
        selectAllTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                selectAllVolunteers();
            }
        });

        TextView deselectAllTextView = findViewById(R.id.control_panel_deselect);
        deselectAllTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                deselectAllVolunteers();
            }
        });

        EditText searchEditText = findViewById(R.id.control_panel_search_by_first_name);
        searchEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                filterArrayByName(editable.toString());
            }
        });

        FloatingActionButton actionButton = findViewById(R.id.control_panel_floating_button);
        actionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sendIntentWithCheckedList();
            }
        });
    }

    private void sendIntentWithCheckedList()
    {
        if (checkedVolunteers.size() > 0) {
            Intent intent = new Intent(this, SummaryInputDataActivity.class);
            intent.putParcelableArrayListExtra(EXTRA_CHECKED_VOLUNTEERS, checkedVolunteers);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Musisz wybrać przynajmniej jedną osobe!", Toast.LENGTH_SHORT).show();
        }
    }

    private void filterArrayByName(String typedText)
    {
        List<Volunteer> filteredVolunteers = new ArrayList<>();
        for (Volunteer volunteer : volunteers) {
            if (volunteer.getFirstName().toLowerCase().contains(typedText.toLowerCase())) {
                filteredVolunteers.add(volunteer);
            }
        }
        adapter.setVolunteers(filteredVolunteers);
    }

    private void selectAllVolunteers()
    {
        for (Volunteer volunteer : volunteers) {
            volunteer.setChecked(true);
            addToCheckedVolunteers(volunteer);
        }

        adapter.setVolunteers(volunteers);
    }

    private void deselectAllVolunteers()
    {
        for (Volunteer volunteer : volunteers) {
            volunteer.setChecked(false);
        }
        checkedVolunteers.removeAll(checkedVolunteers);
        adapter.setVolunteers(volunteers);
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
                if (volunteer.isChecked()) {
                    volunteer.setChecked(false);
                    removeFromCheckedVolunteers(volunteer);
                } else {
                    volunteer.setChecked(true);
                    addToCheckedVolunteers(volunteer);
                }

            }
        });
    }

    private void addToCheckedVolunteers(Volunteer volunteer)
    {
        if(!checkedVolunteers.contains(volunteer))
        checkedVolunteers.add(volunteer);
    }

    private void removeFromCheckedVolunteers(Volunteer volunteer)
    {
        for (int i = 0; i < checkedVolunteers.size(); i++) {
            if (volunteer.getId() == checkedVolunteers.get(i).getId()) {
                checkedVolunteers.remove(i);
            }
        }
    }

    private List<Volunteer> createListFromJson(String result)
    {

        List<Volunteer> volunteers = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            volunteers = fillArrayFromJson(jsonArray);
        } catch (JSONException e) {
            Log.d("RESULT_FROM_JSON", "onException: " + e.getMessage());
        }
        return volunteers;
    }

    private List<Volunteer> fillArrayFromJson(JSONArray jsonArray) throws JSONException
    {
        JSONObject jsonObject;
        List<Volunteer> volunteers = new ArrayList<>();
        for (int i = 0; i <= jsonArray.length() - 1; i++) {
            jsonObject = jsonArray.getJSONObject(i);
            volunteers.add(new Volunteer
                    (jsonObject.getInt("id"), jsonObject.getString("imie"), jsonObject.getString("nazwisko"), false));
        }
        return volunteers;
    }
}