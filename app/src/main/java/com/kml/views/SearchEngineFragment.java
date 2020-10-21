package com.kml.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kml.R;
import com.kml.data.models.GameFilterInfo;

import org.jetbrains.annotations.NotNull;

public class SearchEngineFragment extends Fragment
{
    public static final String EXTRA_GAME_FILTER_INFO = "com.kml.fragments.EXTRA_GAME_FILTER_INFO";


    private View rootView;
    private Context context;

    private Spinner spinnerNumberOfKids;
    private Spinner spinnerKidsAge;
    private Spinner spinnerPlace;
    private Spinner spinnerTypeOfGames;
    private Spinner spinnerCategory;

    private String name;
    private String numberOfKids;
    private String kidsAge;
    private String place;
    private String typeOfGames;
    private String category;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_form_animacje, container, false);
        context = rootView.getContext();

        createSpinnerInstances();

        Button button = rootView.findViewById(R.id.btn_search);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getFormInfo();
                GameFilterInfo filterInfo = new GameFilterInfo(name,numberOfKids,kidsAge,place,typeOfGames,category);

                Intent intent = new Intent(rootView.getContext(), GameRecycleViewActivity.class);
                intent.putExtra(EXTRA_GAME_FILTER_INFO, filterInfo);
                startActivity(intent);
            }
        });


        return rootView;

    }


    private void getFormInfo()
    {
        EditText editTextName = rootView.findViewById(R.id.form_edit_text_name);
        name = editTextName.getText().toString();

        numberOfKids = spinnerNumberOfKids.getSelectedItem().toString();
        kidsAge = spinnerKidsAge.getSelectedItem().toString();
        place = spinnerPlace.getSelectedItem().toString();
        typeOfGames = spinnerTypeOfGames.getSelectedItem().toString();
        category = spinnerCategory.getSelectedItem().toString();

    }

    private void createSpinnerInstances()
    {
        spinnerNumberOfKids = rootView.findViewById(R.id.form_spinner_number_of_kids);
        spinnerKidsAge = rootView.findViewById(R.id.form_spinner_kids_age);
        spinnerPlace = rootView.findViewById(R.id.form_spinner_place);
        spinnerTypeOfGames = rootView.findViewById(R.id.form_spinner_type_of_games);
        spinnerCategory = rootView.findViewById(R.id.form_spinner_category);

        Spinner[] spinners = {spinnerNumberOfKids, spinnerKidsAge, spinnerPlace, spinnerTypeOfGames, spinnerCategory};
        int[] stringArrayIds =
                {R.array.number_of_kids_options, R.array.kids_age_options, R.array.place_options, R.array.type_of_games_options, R.array.category_options};

        for (int i = 0; i < 5; i++) {
            setSingleSpinnerInstance(stringArrayIds[i], spinners[i]);
        }
    }

    private void setSingleSpinnerInstance(int arrayResource, Spinner spinner)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                arrayResource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

}
