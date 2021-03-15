package com.kml.views.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.kml.Constants.Extras.IS_FROM_FILE_EXTRA
import com.kml.Constants.Extras.WORKS_EXTRA
import com.kml.R
import com.kml.adapters.WorkAdapter
import com.kml.data.app.KmlApp
import com.kml.databinding.FragmentAllHistoryBinding
import com.kml.extensions.setFragment
import com.kml.models.Work
import com.kml.views.BaseFragment
import com.kml.views.dialogs.ExtendedWorkDialog
import com.kml.views.fragments.mainFeatures.WorkTimerFragment

class AllHistoryFragment : BaseFragment() {
    private lateinit var adapter: WorkAdapter
    private var _binding: FragmentAllHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        shouldShowBackButton = true
        KmlApp.isFromWorksHistory = true

        adapter = WorkAdapter { extendInDialog(it) }
        binding.worksHistoryRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AllHistoryFragment.adapter
        }

        val works = arguments?.getParcelableArrayList<Work>(WORKS_EXTRA) ?: listOf()
        val isFromFile = arguments?.getBoolean(IS_FROM_FILE_EXTRA) ?: true
        setWorksToAdapter(works ,isFromFile)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().setTitle(R.string.your_last_works)
    }

    private fun setWorksToAdapter(works: List<Work>, isFromFile: Boolean) {
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.works = works
            if (isFromFile) {
                Toast.makeText(requireContext(), R.string.load_previous_data, Toast.LENGTH_SHORT).show()
            }
            reactOnNoItems()
        }, 200)
    }

    private fun extendInDialog(work: Work) {
        val dialog = ExtendedWorkDialog(work)
        dialog.show(parentFragmentManager, "ExtendedWork")
    }

    private fun reactOnNoItems() {
        if (adapter.itemCount == 0) {
            binding.noResultsOnHistory.visibility = View.VISIBLE
            binding.noResultsOnHistoryClickable.visibility = View.VISIBLE
            setOnItemClickListener(binding.noResultsOnHistoryClickable)
        }
    }

    private fun setOnItemClickListener(noResultsHistoryClickable: TextView) {
        noResultsHistoryClickable.setOnClickListener {
            val navigationView: NavigationView = requireActivity().findViewById(R.id.nav_view)
            navigationView.setCheckedItem(R.id.nav_timer)
            setFragment(WorkTimerFragment())
        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().setTitle(R.string.app_name)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}