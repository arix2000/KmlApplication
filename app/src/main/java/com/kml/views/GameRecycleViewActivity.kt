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
    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_recycle_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = GameAdapter()
        initRecycleView(adapter)
        initViewModel()

        gameViewModel.allGames.observe(this, { games ->

            val filteredGames = gameViewModel.filterGames(games)

            if (filteredGames.isEmpty()) {
                Toast.makeText(this@GameRecycleViewActivity, "Nic nie znaleziono :(", Toast.LENGTH_SHORT).show()
            }
            adapter.setGames(filteredGames)
            title = "Znaleziono " + filteredGames.size + " wyników"
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

    private fun initRecycleView(adapter: GameAdapter) {
        val recyclerView = findViewById<RecyclerView>(R.id.search_engine_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun initViewModel() {
        val dataSource = GameDatabase.getInstance(this).gameDao
        val viewModelFactory = GameViewModelFactory(dataSource, intent)
        gameViewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
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
        aboutDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        aboutDialog.show()
    }

}