package com.kml.controlPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kml.R;
import com.kml.controlPanel.recycleViewThings.AddToChosenActivity;

public class ControlPanelFragment extends Fragment
{
    View root;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_control_panel, container, false);
        progressBar = root.findViewById(R.id.control_panel_progress_bar);

        Button btnAddToChosen = root.findViewById(R.id.add_work_to_chosen_btn);
        btnAddToChosen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(root.getContext(), AddToChosenActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onStop()
    {
        super.onStop();
        progressBar.setVisibility(View.GONE);
    }
}
