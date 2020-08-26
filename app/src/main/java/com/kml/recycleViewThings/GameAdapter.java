package com.kml.recycleViewThings;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.kml.R;
import com.kml.internalRoomDatabase.Game;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.NoteHolder>
{
    private List<Game> games = new ArrayList<>();
    private OnItemClickListener listener;
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position)
    {
        Game currentGame = games.get(position);
        holder.textViewName.setText(currentGame.getName());
        holder.textViewDescription.setText(currentGame.getDescription());
        holder.textViewNumberOfKids.setText(currentGame.getNumberOfKids().replaceAll(";",","));
        setColorsByCategories(holder, currentGame);

    }

    private void setColorsByCategories(NoteHolder holder, Game currentGame)
    {
        String colorGreen = "#DDb0f7a8";
        String colorBlue = "#DDaca8f7";
        String colorPink = "#DDf7a8f6";
        String colorRed = "#DDf7a8ac";
        String colorYellow = "#DDf7f7a8";

        switch (currentGame.getCategory())
        {
            case "Zabawy": holder.itemBackground.setCardBackgroundColor(Color.parseColor(colorGreen)); break;
            case "Drużynowe": holder.itemBackground.setCardBackgroundColor(Color.parseColor(colorBlue)); break;
            case "Lina": holder.itemBackground.setCardBackgroundColor(Color.parseColor(colorPink)); break;
            case "Chusta": holder.itemBackground.setCardBackgroundColor(Color.parseColor(colorYellow)); break;
            case "Tańce": holder.itemBackground.setCardBackgroundColor(Color.parseColor(colorRed)); break;
        }
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public void setGames(List<Game> games)
    {
        this.games = games;
        notifyDataSetChanged();
    }
    class NoteHolder extends RecyclerView.ViewHolder
    {
        private CardView itemBackground;
        private TextView textViewName;
        private TextView textViewDescription;
        private TextView textViewNumberOfKids;
        public NoteHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.row_name);
            textViewDescription = itemView.findViewById(R.id.row_description);
            textViewNumberOfKids = itemView.findViewById(R.id.row_number_of_kids);
            itemBackground = itemView.findViewById(R.id.item_background);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    {
                        listener.OnItemClick(games.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener
    {
        void OnItemClick(Game game);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;

    }
}

