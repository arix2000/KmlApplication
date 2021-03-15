package com.kml.views.fragments.mainFeatures

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import com.kml.R
import com.kml.views.BaseFragment
import com.kml.views.activities.SelectVolunteersActivity

class ControlPanelFragment : BaseFragment() {
    private lateinit var root: View
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        root = inflater.inflate(R.layout.fragment_control_panel, container, false)

        progressBar = root.findViewById(R.id.control_panel_progress_bar)
        val btnAddToChosen = root.findViewById<Button>(R.id.add_work_to_chosen_btn)

        btnAddToChosen.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val intent = Intent(requireContext(), SelectVolunteersActivity::class.java)
            startActivity(intent)
        }
        return root
    }

    override fun onStop() {
        super.onStop()
        progressBar.visibility = View.GONE
    }
}