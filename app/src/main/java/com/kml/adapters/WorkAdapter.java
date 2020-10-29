package com.kml.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kml.R;
import com.kml.data.models.Work;
import com.kml.holders.WorkHolder;


import java.util.ArrayList;
import java.util.List;

public class WorkAdapter extends RecyclerView.Adapter<WorkHolder>
{
    List<Work> works = new ArrayList<>();
    private WorkAdapter.OnItemClickListener listener;
    ProgressBar progressBar;

    @NonNull
    @Override
    public WorkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item_work_history, parent, false);
        return new WorkHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkHolder holder, int position)
    {
        Work work = works.get(position);
        holder.workHistoryName.setText(work.getWorkName());
        holder.workHistoryDescription.setText(work.getWorkDescription());
        holder.workHistoryDate.setText(work.getWorkDate());
        holder.itemView.setOnClickListener(v -> {
            if(listener!=null && position != RecyclerView.NO_POSITION)
            {
                listener.OnItemClick(works.get(position));
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return works.size();
    }

    public void setWorks(List<Work> works)
    {
        this.works = works;
        notifyDataSetChanged();
    }

    public void setProgressBar(ProgressBar progressBar)
    {
        this.progressBar = progressBar;
    }

    public interface OnItemClickListener
    {
        void OnItemClick(Work work);
    }

    public void setOnItemClickListener(WorkAdapter.OnItemClickListener listener)
    {
        this.listener = listener;
    }
}
