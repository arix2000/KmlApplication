package com.kml.controlPanel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.kml.R;

import java.util.List;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.VolunteerHolder>
{
    List<Volunteer> volunteers;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public VolunteerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_user, parent, false);
        return new VolunteerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VolunteerHolder holder, int position)
    {
        Volunteer volunteer = volunteers.get(position);
        String fullName = volunteer.getFirstName() + " " + volunteer.getLastName();
        holder.name.setText(fullName);
        holder.checkBox.setChecked(volunteer.isChecked());
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

    public class VolunteerHolder extends RecyclerView.ViewHolder
    {
        private CheckBox checkBox;
        private TextView name;

        public VolunteerHolder(@NonNull View itemView)
        {
            super(itemView);
            checkBox = itemView.findViewById(R.id.row_user_checkbox);
            name = itemView.findViewById(R.id.row_user_name);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(volunteers.get(position));
                        setCheckboxState(checkBox);
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
