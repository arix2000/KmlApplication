package com.kml.holders

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kml.R

class GameHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @JvmField
    var itemBackground: CardView = itemView.findViewById(R.id.item_background)
    @JvmField
    var textViewName: TextView = itemView.findViewById(R.id.row_user_name)
    @JvmField
    var textViewDescription: TextView = itemView.findViewById(R.id.row_description)
    @JvmField
    var textViewNumberOfKids: TextView = itemView.findViewById(R.id.row_number_of_kids)

}