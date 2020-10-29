package com.kml.holders;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kml.R;

public class GameHolder extends RecyclerView.ViewHolder {
    public CardView itemBackground;
    public TextView textViewName;
    public TextView textViewDescription;
    public TextView textViewNumberOfKids;

    public GameHolder(View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.row_user_name);
        textViewDescription = itemView.findViewById(R.id.row_description);
        textViewNumberOfKids = itemView.findViewById(R.id.row_number_of_kids);
        itemBackground = itemView.findViewById(R.id.item_background);
    }
}
