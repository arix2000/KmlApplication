package com.kml.worksHistory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.kml.R;
import com.kml.controlPanel.recycleViewThings.Volunteer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import kotlinx.coroutines.scheduling.WorkQueueKt;


public class WorksHistoryFragment extends Fragment
{
    public static final String WORK_HISTORY_TAG="WORK_HISTORY_TAG";

    View root;
    WorkAdapter adapter;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_works_history, container, false);

        progressBar = root.findViewById(R.id.works_history_progress_bar);

        initRecycleView();
        fillRecycleView();

        return root;
    }

    private void initRecycleView()
    {
        adapter = new WorkAdapter();
        adapter.setProgressBar(progressBar);
        RecyclerView recyclerView = root.findViewById(R.id.works_history_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void fillRecycleView()
    {
        //We need to wait for close of navigation drawer and show progress bar until recycle view is not loaded
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String result = getResultFromExternalDb();
                final List<Work> works = createListFromJson(result);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        adapter.setWorks(works);
                    }
                },100);
            }
        }).start();

    }

    private String getResultFromExternalDb()
    {
        DbGetWorksHistory dbGetWorksHistory = new DbGetWorksHistory();
        dbGetWorksHistory.start();

        return dbGetWorksHistory.getResult();
    }

    private List<Work> createListFromJson(String jsonString)
    {

        List<Work> works = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            works = fillArrayFromJson(jsonArray);
        } catch (JSONException e) {
            Log.d("RESULT_FROM_JSON", "onException: " + e.getMessage());
        }
        return works;
    }

    private List<Work> fillArrayFromJson(JSONArray jsonArray) throws JSONException
    {
        JSONObject jsonObject;
        List<Work> works = new ArrayList<>();
        for (int i = 0; i <= jsonArray.length() - 1; i++) {
            jsonObject = jsonArray.getJSONObject(i);
            works.add(new Work(jsonObject.getString("nazwaZadania"),
                    jsonObject.getString("opisZadania"), jsonObject.getString("data")));
        }
        return works;
    }
}