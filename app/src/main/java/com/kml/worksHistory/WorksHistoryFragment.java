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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.kml.R;
import com.kml.aGlobalUses.FileFactory;
import com.kml.aGlobalUses.externalDbOperations.DbGetWorksHistory;
import com.kml.workTimer.WorkTimerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WorksHistoryFragment extends Fragment
{
    public static final String WORK_HISTORY_TAG = "WORK_HISTORY_TAG";

    View root;
    WorkAdapter adapter;
    ProgressBar progressBar;
    FileFactory fileFactory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_works_history, container, false);
        fileFactory = new FileFactory(root.getContext());

        progressBar = root.findViewById(R.id.works_history_progress_bar);

        initRecycleView();
        fillRecycleView();
        initOnItemClickListener();

        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle("Twoje ostatnie zadania:");
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
            private List<Work> works;
            private String result;
            private boolean isFromFile;

            @Override
            public void run()
            {
                result = getResultFromExternalDb();
                if (result.trim().isEmpty()) // no internet connection
                {
                    result = fileFactory.readFromFile(FileFactory.HISTORY_KEEP_DATA_TXT);
                    isFromFile = true;
                }
                works = createListFromJson(result);
                setWorksToAdapter(works, isFromFile);
                fileFactory.saveStateToFile(result, FileFactory.HISTORY_KEEP_DATA_TXT);
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

    private void setWorksToAdapter(final List<Work> works, final boolean isFromFile)
    {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                adapter.setWorks(works);
                if (isFromFile) {
                    Toast.makeText(root.getContext(), "Brak połączenia z internetem! Wczytano ostatnie dane.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
                reactOnNoItems();
            }
        }, 200);

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

    private void reactOnNoItems()
    {
        TextView noResultsHistory = root.findViewById(R.id.no_results_on_history);
        TextView noResultsHistoryClickable = root.findViewById(R.id.no_results_on_history_clickable);

        if(adapter.getItemCount() == 0)
        {
            noResultsHistory.setVisibility(View.VISIBLE);
            noResultsHistoryClickable.setVisibility(View.VISIBLE);
            setOnItemClickListener(noResultsHistoryClickable);
        }

    }

    private void setOnItemClickListener(TextView noResultsHistoryClickable)
    {
        noResultsHistoryClickable.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.GONE);
                NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                navigationView.setCheckedItem(R.id.nav_timer);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorkTimerFragment()).commit();
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        getActivity().setTitle("Klub Młodych Liderów");
    }
}