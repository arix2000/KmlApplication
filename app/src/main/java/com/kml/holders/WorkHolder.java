package com.kml.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kml.R;

public class WorkHolder extends RecyclerView.ViewHolder
{
    public TextView workHistoryName;
    public TextView workHistoryDescription;
    public TextView workHistoryDate;

    public WorkHolder(@NonNull View itemView)
    {
        super(itemView);
        workHistoryName = itemView.findViewById(R.id.history_work_name);
        workHistoryDescription = itemView.findViewById(R.id.history_work_description);
        workHistoryDate = itemView.findViewById(R.id.history_work_date);
    }
}
