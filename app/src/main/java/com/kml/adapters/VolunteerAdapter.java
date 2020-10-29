package com.kml.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.kml.R;
import com.kml.data.models.Volunteer;
import com.kml.holders.VolunteerHolder;

import java.util.List;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerHolder>
{
    List<Volunteer> volunteers;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public VolunteerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new VolunteerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VolunteerHolder holder, int position)
    {
        Volunteer volunteer = volunteers.get(position);
        String fullName = volunteer.getFirstName() + " " + volunteer.getLastName();
        holder.name.setText(fullName);
        holder.checkBox.setChecked(volunteer.isChecked());
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.OnItemClick(volunteers.get(position));
                    setCheckboxState(holder.checkBox);
                }
            }

            private void setCheckboxState(CheckBox checkBox)
            {
                if (checkBox.isChecked())
                    checkBox.setChecked(false);
                else if (!checkBox.isChecked())
                    checkBox.setChecked(true);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return volunteers.size();
    }

    public void setVolunteers(List<Volunteer> volunteers)
    {
        this.volunteers = volunteers;
        notifyDataSetChanged();
    }

    public List<Volunteer> getVolunteers()
    {
        return volunteers;
    }


    public interface OnItemClickListener
    {
        void OnItemClick(Volunteer volunteer);
    }

    public void setOnItemClickListener(VolunteerAdapter.OnItemClickListener listener)
    {
        this.listener = listener;
    }
}
