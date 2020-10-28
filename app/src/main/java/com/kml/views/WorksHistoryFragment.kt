package com.kml.views

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.kml.data.app.FileFactory
import com.kml.data.models.Work
import com.kml.viewModels.WorksHistoryViewModel
import com.kml.viewModels.WorksHistoryViewModelFactory

class WorksHistoryFragment : Fragment() {
    private lateinit var root: View
    lateinit var adapter: WorkAdapter
    lateinit var progressBar: ProgressBar
    private lateinit var fileFactory: FileFactory
    lateinit var viewModel: WorksHistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        root = inflater.inflate(R.layout.fragment_works_history, container, false)
        fileFactory = FileFactory(root.context)
        progressBar = root.findViewById(R.id.works_history_progress_bar)
        initRecycleView()


        val viewModelFactory = WorksHistoryViewModelFactory(fileFactory)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WorksHistoryViewModel::class.java)

        viewModel.works.observe(viewLifecycleOwner) { setWorksToAdapter(it, viewModel.isFromFile())}


        adapter.setOnItemClickListener { work: Work -> extendInDialog(work) }
        return root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setTitle(R.string.your_last_works)
    }

    private fun initRecycleView() {
        adapter = WorkAdapter()
        adapter.setProgressBar(progressBar)
        val recyclerView: RecyclerView = root.findViewById(R.id.works_history_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.adapter = adapter
    }

    //fragment
    private fun setWorksToAdapter(works: List<Work>?, isFromFile: Boolean) {
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.setWorks(works)
            if (isFromFile) {
                Toast.makeText(root.context, R.string.load_previous_data, Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.GONE
            reactOnNoItems()
        }, 200)
    }

    //fragment
    private fun extendInDialog(work: Work) {
        val dialog = Dialog(root.context)
        dialog.setContentView(R.layout.dialog_work_history_extended)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val workName = dialog.findViewById<TextView>(R.id.dialog_history_work_name)
        val workDescription = dialog.findViewById<TextView>(R.id.dialog_history_work_description)
        val workDate = dialog.findViewById<TextView>(R.id.dialog_history_work_date)
        val executionTime = dialog.findViewById<TextView>(R.id.dialog_history_execution_time)
        dialog.show()
        workName.text = work.workName
        workDescription.text = work.workDescription
        workDate.text = work.workDate
        executionTime.text = work.executionTime
    }

    //fragment
    private fun reactOnNoItems() {
        val noResultsHistory = root.findViewById<TextView>(R.id.no_results_on_history)
        val noResultsHistoryClickable = root.findViewById<TextView>(R.id.no_results_on_history_clickable)
        if (adapter.itemCount == 0) {
            noResultsHistory.visibility = View.VISIBLE
            noResultsHistoryClickable.visibility = View.VISIBLE
            setOnItemClickListener(noResultsHistoryClickable)
        }
    }

    //fragment
    private fun setOnItemClickListener(noResultsHistoryClickable: TextView) {
        noResultsHistoryClickable.setOnClickListener {
            progressBar.visibility = View.GONE
            val navigationView: NavigationView = requireActivity().findViewById(R.id.nav_view)
            navigationView.setCheckedItem(R.id.nav_timer)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WorkTimerFragment()).commit()
        }
    }

    //fragment
    override fun onPause() {
        super.onPause()
        requireActivity().setTitle(R.string.app_name)
    }
}