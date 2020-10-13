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


import java.util.ArrayList;
import java.util.List;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.WorkHolder>
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
    }

    @Override
    public int getItemCount()
    {
        return works.size();
    }

    public class WorkHolder extends RecyclerView.ViewHolder
    {
        private TextView workHistoryName;
        private TextView workHistoryDescription;
        private TextView workHistoryDate;

        public WorkHolder(@NonNull View itemView)
        {
            super(itemView);
            workHistoryName = itemView.findViewById(R.id.history_work_name);
            workHistoryDescription = itemView.findViewById(R.id.history_work_description);
            workHistoryDate = itemView.findViewById(R.id.history_work_date);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int position = getAdapterPosition();
                    if(listener!=null && position != RecyclerView.NO_POSITION)
                    {
                        listener.OnItemClick(works.get(position));
                    }
                }
            });
        }
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
