package com.kml.views.activities

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.kml.R
import com.kml.databinding.ActivityPropertiesGameBinding
import com.kml.models.Game
import com.kml.views.BaseActivity

class GamePropertiesActivity : BaseActivity() {

    private lateinit var binding: ActivityPropertiesGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_properties_game)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val takenData = intent
        val game: Game = takenData.getParcelableExtra(GameRecycleViewActivity.EXTRA_GAME)
                ?: Game("", "", "", "", "", "", "", "")
        setFieldsWith(game)
    }

    private fun setFieldsWith(game: Game) {
        val getColor = setColorForBackground(game.category)
        binding.propertiesContainer.setBackgroundColor(getColor)
        binding.game = Game(game.name, game.description,
                game.requirements.replace(";", ","),
                game.numberOfKids.replace(";", ","),
                game.kidsAge.replace(";", ","),
                game.place.replace(";", ","),
                game.typeOfGame.replace(";", ","), game.category)
    }

    private fun setColorForBackground(category: String): Int {

        return when (category) {
            "Zabawy" -> getColorFrom(R.color.gamesColor)
            "Drużynowe" -> getColorFrom(R.color.teamsColor)
            "Lina" -> getColorFrom(R.color.RopeColor)
            "Chusta" -> getColorFrom(R.color.scarfColor)
            "Tańce" -> getColorFrom(R.color.danceColor)
            else -> 0
        }
    }

    private fun getColorFrom(resId: Int): Int {
        return ContextCompat.getColor(this, resId)
    }
}