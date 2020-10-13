package com.kml.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.kml.data.app.KmlApp;
import com.kml.R;
import com.kml.adapters.GameAdapter;
import com.kml.data.models.Game;
import com.kml.viewModels.GameViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShowRecycleViewActivity extends AppCompatActivity
{
    public static final String DEFAULT_ALL_RANGES = "Wszystkie przedziały";
    public static final String DEFAULT_ALL_OPTIONS = "Wszystkie";

    private GameViewModel gameViewModel;
    private Intent takenData;
    private String name;
    private String numberOfKids;
    private String kidsAge;
    private String place;
    private String typeOfGame;
    private String category;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recycle_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        takenData = getIntent();
        getDataFromIntent();

        RecyclerView recyclerView = findViewById(R.id.search_engine_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final GameAdapter adapter = new GameAdapter();
        recyclerView.setAdapter(adapter);



        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        gameViewModel.getAllGames().observe(this, new Observer<List<Game>>()
        {
            @Override
            public void onChanged(@NonNull List<Game> games)
            {
                if (!name.trim().isEmpty()) {
                    games = filterByName(games);
                }
                if (!numberOfKids.equals(DEFAULT_ALL_RANGES)) {
                    games = filterByNumberOfKids(games);
                }
                if (!kidsAge.equals(DEFAULT_ALL_RANGES)) {
                    games = filterByKidsAge(games);
                }
                if (!typeOfGame.equals(DEFAULT_ALL_OPTIONS)) {
                    games = filterByTypeOfGame(games);
                }
                if (!place.equals("Każde")) {
                    games = filterByPlace(games);
                }
                if (!category.equals(DEFAULT_ALL_OPTIONS)) {
                    games = filterByCategory(games);
                }
                if (games.size() == 0) {
                    Toast.makeText(ShowRecycleViewActivity.this, "Nic nie znaleziono :(", Toast.LENGTH_SHORT).show();
                }
                adapter.setGames(games);

                setTitle("Znaleziono " + games.size() + " wyników");
            }
        });

        adapter.setOnItemClickListener(new GameAdapter.OnItemClickListener()
        {
            @Override
            public void OnItemClick(Game game)
            {
                Intent intent = new Intent(ShowRecycleViewActivity.this, PropertiesOfGameActivity.class);
                intent.putExtra(SearchEngineFragment.EXTRA_NAME, game.getName());
                intent.putExtra(SearchEngineFragment.EXTRA_DESCRIPTION, game.getDescription());
                intent.putExtra(SearchEngineFragment.EXTRA_REQUIREMENTS, game.getRequirements());
                intent.putExtra(SearchEngineFragment.EXTRA_NUMBER_OF_KIDS, game.getNumberOfKids());
                intent.putExtra(SearchEngineFragment.EXTRA_KIDS_AGE, game.getKidsAge());
                intent.putExtra(SearchEngineFragment.EXTRA_PLACE, game.getPlace());
                intent.putExtra(SearchEngineFragment.EXTRA_TYPE_OF_GAMES, game.getTypeOfGame());
                intent.putExtra(SearchEngineFragment.EXTRA_CATEGORY, game.getCategory());

                startActivity(intent);
            }
        });

        KmlApp.isFromRecycleViewActivity = true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.about_colors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.about_colors:
                showDialogAboutColors();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialogAboutColors()
    {
        Dialog aboutDialog = new Dialog(this);
        aboutDialog.setContentView(R.layout.dialog_about_colors);
        aboutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aboutDialog.show();
    }

    private List<Game> filterByName(List<Game> games)
    {
        List<Game> filteredGames = new ArrayList<>();

        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getName().contains(name.toUpperCase())) {
                filteredGames.add(games.get(i));
            }
        }

        return filteredGames;
    }

    private List<Game> filterByNumberOfKids(List<Game> games)
    {
        List<Game> filteredGames = new ArrayList<>();

        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getNumberOfKids().contains(numberOfKids.replace("+", "<"))) {
                filteredGames.add(games.get(i));
            }
        }

        return filteredGames;
    }

    private List<Game> filterByKidsAge(List<Game> games)
    {
        List<Game> filteredGames = new ArrayList<>();

        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getKidsAge().contains(kidsAge.replace("+", "<"))) {
                filteredGames.add(games.get(i));
            }
        }

        return filteredGames;
    }

    private List<Game> filterByTypeOfGame(List<Game> games)
    {
        List<Game> filteredGames = new ArrayList<>();

        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getTypeOfGame().contains(typeOfGame)) {
                filteredGames.add(games.get(i));
            }
        }

        return filteredGames;
    }

    private List<Game> filterByPlace(List<Game> games)
    {
        List<Game> filteredGames = new ArrayList<>();

        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getPlace().contains(place)) {
                filteredGames.add(games.get(i));
            }
        }

        return filteredGames;
    }

    private List<Game> filterByCategory(List<Game> games)
    {
        List<Game> filteredGames = new ArrayList<>();

        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getCategory().contains(category)) {
                filteredGames.add(games.get(i));
            }
        }

        return filteredGames;
    }

    private void getDataFromIntent()
    {
        name = takenData.getStringExtra(SearchEngineFragment.EXTRA_NAME);
        numberOfKids = takenData.getStringExtra(SearchEngineFragment.EXTRA_NUMBER_OF_KIDS);
        kidsAge = takenData.getStringExtra(SearchEngineFragment.EXTRA_KIDS_AGE);
        place = takenData.getStringExtra(SearchEngineFragment.EXTRA_PLACE);
        typeOfGame = takenData.getStringExtra(SearchEngineFragment.EXTRA_TYPE_OF_GAMES);
        category = takenData.getStringExtra(SearchEngineFragment.EXTRA_CATEGORY);
    }


}

