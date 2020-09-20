package com.kml.worksHistory;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;

import com.kml.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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
        initOnItemClickListener();

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
            works.add(new Work(jsonObject.getString("nazwaZadania"), jsonObject.getString("opisZadania"),
                    jsonObject.getString("data"), jsonObject.getString("czasWykonania")));
        }
        return works;
    }

    private void initOnItemClickListener()
    {
        adapter.setOnItemClickListener(new WorkAdapter.OnItemClickListener()
        {
            @Override
            public void OnItemClick(Work work)
            {
                extendInDialog(work);
            }
        });
    }

    private void extendInDialog(Work work)
    {
        Dialog dialog = new Dialog(root.getContext());
        dialog.setContentView(R.layout.dialog_work_history_extended);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView workName = dialog.findViewById(R.id.dialog_history_work_name);
        TextView workDescription = dialog.findViewById(R.id.dialog_history_work_description);
        TextView workDate = dialog.findViewById(R.id.dialog_history_work_date);
        TextView executionTime = dialog.findViewById(R.id.dialog_history_execution_time);
        dialog.show();

        workName.setText(work.getWorkName());
        workDescription.setText(work.getWorkDescription());
        workDate.setText(work.getWorkDate());
        executionTime.setText(work.getExecutionTime());

    }

}