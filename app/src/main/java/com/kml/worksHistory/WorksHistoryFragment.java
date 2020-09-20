package com.kml.worksHistory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kml.R;


public class WorksHistoryFragment extends Fragment
{
    View root;
    WorkAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_works_history, container, false);

        initRecycleView();
        fillRecycleView();







        return root;
    }

    private void initRecycleView()
    {
        adapter = new WorkAdapter();
        RecyclerView recyclerView = root.findViewById(R.id.works_history_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void fillRecycleView()
    {

    }
}