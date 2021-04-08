package com.kml.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kml.R
import com.kml.models.Game
import com.kml.holders.GameHolder
import java.util.*

class GameAdapter(private val clickListener: (Game) -> Unit) : RecyclerView.Adapter<GameHolder>() {

    private var games: List<Game> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_game, parent, false)
        return GameHolder(itemView)
    }

    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        val currentGame = games[position]
        holder.bind(currentGame, clickListener)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    fun setGames(games: List<Game>) {
        this.games = games
        notifyDataSetChanged()
    }

}