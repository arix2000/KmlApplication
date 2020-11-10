package com.kml.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kml.R
import com.kml.data.models.Game
import com.kml.databinding.ActivityPropertiesGameBinding

class GamePropertiesActivity : AppCompatActivity() {

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
            "Zabawy" -> resources.getColor(R.color.colorZabaw)
            "Drużynowe" -> resources.getColor(R.color.colorDruzynowe)
            "Lina" -> resources.getColor(R.color.colorLina)
            "Chusta" -> resources.getColor(R.color.colorChusta)
            "Tańce" -> resources.getColor(R.color.colorTance)
            else -> 0
        }
    }
}