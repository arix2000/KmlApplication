package com.kml.holders

import android.graphics.Color
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kml.models.Game
import kotlinx.android.synthetic.main.list_item_game.view.*

class GameHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(game: Game, clickListener: (Game) -> Unit)
    {
        setColorsByCategories(itemView.item_background, game.category)
        itemView.game_item_name.text = game.name
        itemView.row_description.text = game.description
        itemView.row_number_of_kids.text = game.numberOfKids.replace(";".toRegex(), ",")
        itemView.setOnClickListener {clickListener(game)}
    }

    private fun setColorsByCategories(cardView: CardView, category: String) {
        val colorGreen = "#DDb0f7a8"
        val colorBlue = "#DDaca8f7"
        val colorPink = "#DDf7a8f6"
        val colorRed = "#DDf7a8ac"
        val colorYellow = "#DDf7f7a8"
        when (category) {
            "Zabawy" -> cardView.setCardBackgroundColor(Color.parseColor(colorGreen))
            "Drużynowe" -> cardView.setCardBackgroundColor(Color.parseColor(colorBlue))
            "Lina" -> cardView.setCardBackgroundColor(Color.parseColor(colorPink))
            "Chusta" -> cardView.setCardBackgroundColor(Color.parseColor(colorYellow))
            "Tańce" -> cardView.setCardBackgroundColor(Color.parseColor(colorRed))
        }
    }
}