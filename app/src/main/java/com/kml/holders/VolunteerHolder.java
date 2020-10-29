package com.kml.holders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kml.R;

public class VolunteerHolder extends RecyclerView.ViewHolder
{
    public CheckBox checkBox;
    public TextView name;

    public VolunteerHolder(@NonNull View itemView)
    {
        super(itemView);
        checkBox = itemView.findViewById(R.id.row_user_checkbox);
        name = itemView.findViewById(R.id.row_user_name);
    }
}
