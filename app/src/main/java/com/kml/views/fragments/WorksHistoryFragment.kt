package com.kml.views.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.kml.R
import com.kml.adapters.WorkAdapter
import com.kml.data.utilities.FileFactory
import com.kml.data.models.Work
import com.kml.viewModelFactories.WorksHistoryViewModelFactory
import com.kml.viewModels.WorksHistoryViewModel
import com.kml.views.dialogs.ExtendedWorkDialog

class WorksHistoryFragment : Fragment() {
    private lateinit var root: View
    private lateinit var adapter: WorkAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var fileFactory: FileFactory
    private lateinit var viewModel: WorksHistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        root = inflater.inflate(R.layout.fragment_works_history, container, false)
        fileFactory = FileFactory(root.context)
        progressBar = root.findViewById(R.id.works_history_progress_bar)

        adapter = WorkAdapter {extendInDialog(it)}
        adapter.progressBar = progressBar
        val recyclerView: RecyclerView = root.findViewById(R.id.works_history_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.adapter = adapter


        val viewModelFactory = WorksHistoryViewModelFactory(fileFactory)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WorksHistoryViewModel::class.java)

        viewModel.works.observe(viewLifecycleOwner) { setWorksToAdapter(it, viewModel.isFromFile()) }

        return root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setTitle(R.string.your_last_works)
    }

    private fun setWorksToAdapter(works: List<Work>, isFromFile: Boolean) {
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.works = works
            if (isFromFile) {
                Toast.makeText(root.context, R.string.load_previous_data, Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.GONE
            reactOnNoItems()
        }, 200)
    }

    private fun extendInDialog(work: Work) {
        val dialog = ExtendedWorkDialog(work)
        dialog.show(parentFragmentManager, "ExtendedWork")
    }

    private fun reactOnNoItems() {
        val noResultsHistory = root.findViewById<TextView>(R.id.no_results_on_history)
        val noResultsHistoryClickable = root.findViewById<TextView>(R.id.no_results_on_history_clickable)
        if (adapter.itemCount == 0) {
            noResultsHistory.visibility = View.VISIBLE
            noResultsHistoryClickable.visibility = View.VISIBLE
            setOnItemClickListener(noResultsHistoryClickable)
        }
    }

    private fun setOnItemClickListener(noResultsHistoryClickable: TextView) {
        noResultsHistoryClickable.setOnClickListener {
            progressBar.visibility = View.GONE
            val navigationView: NavigationView = requireActivity().findViewById(R.id.nav_view)
            navigationView.setCheckedItem(R.id.nav_timer)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WorkTimerFragment()).commit()
        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().setTitle(R.string.app_name)
    }
}