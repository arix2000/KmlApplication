package com.kml.views

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kml.R
import com.kml.adapters.GameAdapter
import com.kml.data.app.KmlApp
import com.kml.data.internalRoomDatabase.GameDatabase
import com.kml.data.models.Game
import com.kml.viewModels.GameViewModel
import com.kml.viewModels.GameViewModelFactory
import java.util.*

class GameRecycleViewActivity : AppCompatActivity() {
    private var gameViewModel: GameViewModel? = null
    private var takenData: Intent? = null
    private var name: String? = null
    private var numberOfKids: String? = null
    private var kidsAge: String? = null
    private var place: String? = null
    private var typeOfGame: String? = null
    private var category: String? = null

    companion object {
        const val DEFAULT_ALL_RANGES = "Wszystkie przedziały"
        const val DEFAULT_ALL_OPTIONS = "Wszystkie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_recycle_view)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        takenData = intent
        dataFromIntent

        val recyclerView = findViewById<RecyclerView>(R.id.search_engine_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val adapter = GameAdapter()
        recyclerView.adapter = adapter

        val dataSource = GameDatabase.getInstance(this).gameDao
        val viewModelFactory = GameViewModelFactory(dataSource)

        gameViewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)

        gameViewModel!!.allGames.observe(this, { games ->
            var games = games
            if (name!!.trim { it <= ' ' }.isNotEmpty()) {
                games = filterByName(games)
            }
            if (numberOfKids != DEFAULT_ALL_RANGES) {
                games = filterByNumberOfKids(games)
            }
            if (kidsAge != DEFAULT_ALL_RANGES) {
                games = filterByKidsAge(games)
            }
            if (typeOfGame != DEFAULT_ALL_OPTIONS) {
                games = filterByTypeOfGame(games)
            }
            if (place != "Każde") {
                games = filterByPlace(games)
            }
            if (category != DEFAULT_ALL_OPTIONS) {
                games = filterByCategory(games)
            }
            if (games.isEmpty()) {
                Toast.makeText(this@GameRecycleViewActivity, "Nic nie znaleziono :(", Toast.LENGTH_SHORT).show()
            }
            adapter.setGames(games)
            title = "Znaleziono " + games.size + " wyników"
        })

        adapter.setOnItemClickListener { game ->
            val intent = Intent(this@GameRecycleViewActivity, PropertiesOfGameActivity::class.java)
            intent.putExtra(SearchEngineFragment.EXTRA_NAME, game.name)
            intent.putExtra(SearchEngineFragment.EXTRA_DESCRIPTION, game.description)
            intent.putExtra(SearchEngineFragment.EXTRA_REQUIREMENTS, game.requirements)
            intent.putExtra(SearchEngineFragment.EXTRA_NUMBER_OF_KIDS, game.numberOfKids)
            intent.putExtra(SearchEngineFragment.EXTRA_KIDS_AGE, game.kidsAge)
            intent.putExtra(SearchEngineFragment.EXTRA_PLACE, game.place)
            intent.putExtra(SearchEngineFragment.EXTRA_TYPE_OF_GAMES, game.typeOfGame)
            intent.putExtra(SearchEngineFragment.EXTRA_CATEGORY, game.category)
            startActivity(intent)
        }

        KmlApp.isFromRecycleViewActivity = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.about_colors, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about_colors -> {
                showDialogAboutColors()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialogAboutColors() {
        val aboutDialog = Dialog(this)
        aboutDialog.setContentView(R.layout.dialog_about_colors)
        aboutDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        aboutDialog.show()
    }

    private fun filterByName(games: List<Game>): List<Game> {
        val filteredGames: MutableList<Game> = ArrayList()
        for (i in games.indices) {
            if (games[i].name.contains(name!!.toUpperCase())) {
                filteredGames.add(games[i])
            }
        }
        return filteredGames
    }

    private fun filterByNumberOfKids(games: List<Game>): List<Game> {
        val filteredGames: MutableList<Game> = ArrayList()
        for (i in games.indices) {
            if (games[i].numberOfKids.contains(numberOfKids!!.replace("+", "<"))) {
                filteredGames.add(games[i])
            }
        }
        return filteredGames
    }

    private fun filterByKidsAge(games: List<Game>): List<Game> {
        val filteredGames: MutableList<Game> = ArrayList()
        for (i in games.indices) {
            if (games[i].kidsAge.contains(kidsAge!!.replace("+", "<"))) {
                filteredGames.add(games[i])
            }
        }
        return filteredGames
    }

    private fun filterByTypeOfGame(games: List<Game>): List<Game> {
        val filteredGames: MutableList<Game> = ArrayList()
        for (i in games.indices) {
            if (games[i].typeOfGame.contains(typeOfGame!!)) {
                filteredGames.add(games[i])
            }
        }
        return filteredGames
    }

    private fun filterByPlace(games: List<Game>): List<Game> {
        val filteredGames: MutableList<Game> = ArrayList()
        for (i in games.indices) {
            if (games[i].place.contains(place!!)) {
                filteredGames.add(games[i])
            }
        }
        return filteredGames
    }

    private fun filterByCategory(games: List<Game>): List<Game> {
        val filteredGames: MutableList<Game> = ArrayList()
        for (i in games.indices) {
            if (games[i].category.contains(category!!)) {
                filteredGames.add(games[i])
            }
        }
        return filteredGames
    }

    private val dataFromIntent: Unit
        get() {
            name = takenData!!.getStringExtra(SearchEngineFragment.EXTRA_NAME)
            numberOfKids = takenData!!.getStringExtra(SearchEngineFragment.EXTRA_NUMBER_OF_KIDS)
            kidsAge = takenData!!.getStringExtra(SearchEngineFragment.EXTRA_KIDS_AGE)
            place = takenData!!.getStringExtra(SearchEngineFragment.EXTRA_PLACE)
            typeOfGame = takenData!!.getStringExtra(SearchEngineFragment.EXTRA_TYPE_OF_GAMES)
            category = takenData!!.getStringExtra(SearchEngineFragment.EXTRA_CATEGORY)
        }
}