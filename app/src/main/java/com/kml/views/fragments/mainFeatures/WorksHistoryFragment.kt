package com.kml.views.fragments.mainFeatures

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.kml.Constants.Tag.WORKS_HISTORY_TAG
import com.kml.R
import com.kml.adapters.WorkAdapter
import com.kml.data.utilities.FileFactory
import com.kml.databinding.FragmentAllHistoryBinding
import com.kml.extensions.logError
import com.kml.extensions.setFragment
import com.kml.models.Work
import com.kml.viewModelFactories.WorksHistoryViewModelFactory
import com.kml.viewModels.WorksHistoryViewModel
import com.kml.views.BaseFragment
import com.kml.views.dialogs.ExtendedWorkDialog
import io.reactivex.rxjava3.kotlin.subscribeBy

class WorksHistoryFragment : BaseFragment() {
    private lateinit var adapter: WorkAdapter
    private var _binding: FragmentAllHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WorksHistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        attachProgressBar(binding.allHistoryProgressBar)

        val viewModelFactory = WorksHistoryViewModelFactory(FileFactory(requireContext()))
        viewModel = ViewModelProvider(this, viewModelFactory).get(WorksHistoryViewModel::class.java)

        adapter = WorkAdapter { extendInDialog(it) }
        binding.worksHistoryRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@WorksHistoryFragment.adapter
        }
        showProgressBar()

        arguments?.getString(WORKS_HISTORY_TAG)?.let { tag ->
            fetchWorks(tag)
        }
    }

    private fun fetchWorks(tag: String) {
        viewModel.fetchDataBy(tag).subscribeBy(
                onSuccess = { setWorksToAdapter(it, viewModel.isFromFile())},
                onError = { logError(it); hideProgressBar() }
        )
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
            hideProgressBar()
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
            setFragment(TimerFragment())
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