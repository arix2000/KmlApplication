package com.kml.views.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kml.KmlApp
import com.kml.R
import com.kml.adapters.GameAdapter
import com.kml.extensions.showSnackBar
import com.kml.models.Game
import com.kml.models.GameFilterInfo
import com.kml.viewModels.GameViewModel
import com.kml.views.BaseActivity
import com.kml.views.fragments.mainFeatures.GameSearchEngineFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameRecycleViewActivity : BaseActivity() {
    private val gameViewModel: GameViewModel by viewModel()

    companion object {
        const val EXTRA_GAME = "com.kml.views.EXTRA_GAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_recycle_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = GameAdapter { handleItemClick(it) }
        initRecycleView(adapter)
        initViewModel()

        gameViewModel.games.observe(this, { games ->
            val filteredGames = gameViewModel.filterGames(games)

            if (filteredGames.isEmpty()) {
                showSnackBar(R.string.no_results_found)
            }
            adapter.setGames(filteredGames)
            title = "Znaleziono " + filteredGames.size + " wyników"
        })

        KmlApp.isFromRecycleViewActivity = true
    }

    private fun handleItemClick(game: Game) {
        val intent = Intent(this, GamePropertiesActivity::class.java)
        intent.putExtra(EXTRA_GAME, game)
        startActivity(intent)
    }

    private fun initRecycleView(adapter: GameAdapter) {
        val recyclerView = findViewById<RecyclerView>(R.id.search_engine_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun initViewModel() {
        val filterInfo: GameFilterInfo = intent.getParcelableExtra(GameSearchEngineFragment.EXTRA_GAME_FILTER_INFO)
                ?: GameFilterInfo.EMPTY

        gameViewModel.filterInfo = filterInfo
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