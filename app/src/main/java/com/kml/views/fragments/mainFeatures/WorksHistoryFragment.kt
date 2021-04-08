package com.kml.views.fragments.mainFeatures

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.kml.Constants.Tags.GET_ALL_TAG
import com.kml.Constants.Tags.MEETINGS_TAG
import com.kml.Constants.Tags.SHOULD_SHOW_BACK_BUTTON
import com.kml.Constants.Tags.WORKS_HISTORY_TYPE
import com.kml.Constants.Tags.WORKS_TAG
import com.kml.R
import com.kml.adapters.WorkAdapter
import com.kml.data.utilities.FileFactory
import com.kml.databinding.FragmentAllHistoryBinding
import com.kml.extensions.logError
import com.kml.extensions.setFragment
import com.kml.extensions.showSnackBar
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
    private lateinit var historyType: String //WORK_TAG or MEETINGS_TAG
    private var shouldShowAll = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        attachProgressBar(binding.allHistoryProgressBar)
        shouldShowBackButton = arguments?.getBoolean(SHOULD_SHOW_BACK_BUTTON) ?: false
        shouldShowAll = arguments?.getBoolean(GET_ALL_TAG) ?: false

        val viewModelFactory = WorksHistoryViewModelFactory(FileFactory(requireContext()))
        viewModel = ViewModelProvider(this, viewModelFactory).get(WorksHistoryViewModel::class.java)

        adapter = WorkAdapter { extendInDialog(it) }
        binding.worksHistoryRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@WorksHistoryFragment.adapter
        }
        showProgressBar()

        arguments?.getString(WORKS_HISTORY_TYPE)?.let { type ->
            historyType = type
            fetchWorks()
        }
    }

    private fun fetchWorks() {
        viewModel.fetchDataBy(historyType, shouldShowAll)
                .subscribeBy(
                        onSuccess = { setWorksToAdapter(it, viewModel.isFromFile()) },
                        onError = { logError(it); hideProgressBar() }
                )
    }

    override fun onResume() {
        super.onResume()
        var titleResId = -1

        when (historyType) {
            WORKS_TAG -> {
                titleResId = if (shouldShowAll) R.string.all_last_works
                else R.string.your_last_works
            }
            MEETINGS_TAG -> {
                titleResId = if (shouldShowAll) R.string.all_last_meetings
                else R.string.your_last_meetings
            }
        }
        requireActivity().setTitle(titleResId)
    }

    private fun setWorksToAdapter(works: List<Work>, isFromFile: Boolean) {
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.works = works
            if (isFromFile) {
                showSnackBar(R.string.load_previous_data)
            }
            reactOnNoItems()
            hideProgressBar()
        }, 200)
    }

    private fun extendInDialog(work: Work) {
        val dialog = ExtendedWorkDialog(work, historyType, true)
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