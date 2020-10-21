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
import com.kml.data.models.GameFilterInfo
import com.kml.viewModels.GameViewModel
import com.kml.viewModels.GameViewModelFactory
import java.lang.NullPointerException

class GameRecycleViewActivity : AppCompatActivity() {
    private lateinit var gameViewModel: GameViewModel

    companion object {
        const val EXTRA_GAME = "com.kml.views.EXTRA_GAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_recycle_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = GameAdapter()
        initRecycleView(adapter)
        initViewModel()

        gameViewModel.games.observe(this, { games ->
            val filteredGames = gameViewModel.filterGames(games)

            if (filteredGames.isEmpty()) {
                Toast.makeText(this@GameRecycleViewActivity, "Nic nie znaleziono :(", Toast.LENGTH_SHORT).show()
            }
            adapter.setGames(filteredGames)
            title = "Znaleziono " + filteredGames.size + " wyników"
        })

        adapter.setOnItemClickListener { game ->
            val intent = Intent(this@GameRecycleViewActivity, PropertiesOfGameActivity::class.java)
            intent.putExtra(EXTRA_GAME, game)
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
        val filterInfo: GameFilterInfo = intent.getParcelableExtra(SearchEngineFragment.EXTRA_GAME_FILTER_INFO)
                ?: throw NullPointerException("filterInfo is null")

        val dataSource = GameDatabase.getInstance(this).gameDao
        val viewModelFactory = GameViewModelFactory(dataSource, filterInfo)
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