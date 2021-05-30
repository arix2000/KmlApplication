package com.kml.views.fragments.mainFeatures

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.kml.Constants
import com.kml.Constants.Strings.EMPTY_STRING
import com.kml.Constants.Tags.GET_ALL_TAG
import com.kml.Constants.Tags.SHOULD_SHOW_BACK_BUTTON
import com.kml.Constants.Tags.WORKS_HISTORY_TYPE
import com.kml.R
import com.kml.adapters.WorkAdapter
import com.kml.databinding.FragmentAllHistoryBinding
import com.kml.extensions.*
import com.kml.models.dto.Work
import com.kml.viewModels.WorksHistoryViewModel
import com.kml.views.BaseFragment
import com.kml.views.dialogs.ExtendedWorkDialog
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class WorksHistoryFragment : BaseFragment() {
    private lateinit var adapter: WorkAdapter
    private var _binding: FragmentAllHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WorksHistoryViewModel by viewModel()
    private lateinit var historyType: String
    private var shouldShowAll = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        attachProgressBar(binding.allHistoryProgressBar)
        shouldShowBackButton = arguments?.getBoolean(SHOULD_SHOW_BACK_BUTTON) ?: false
        shouldShowAll = arguments?.getBoolean(GET_ALL_TAG) ?: false
        setupUi()
    }

    private fun fetchWorks() {
        viewModel.fetchDataBy(historyType, shouldShowAll)
                .subscribeBy(
                        onSuccess = { setWorksToAdapter(it, viewModel.isFromFile()) },
                        onError = { logError(it); hideProgressBar() }
                )
    }

    private fun setupUi() {
        adapter = WorkAdapter { extendInDialog(it) }
        binding.worksHistoryRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@WorksHistoryFragment.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                        requireContext().hideSoftKeyboard(this@run)
                }
            })
        }
        showProgressBar()

        arguments?.getString(WORKS_HISTORY_TYPE)?.let { type ->
            historyType = type
            fetchWorks()
        }

        binding.searchExpandableView.searchButton.setOnClickListener {
            if (viewModel.isSearchExpanded)
                collapseSearch()
            else expandSearch()
        }

        binding.searchExpandableView.searchEditText.doAfterTextChanged { text ->
            if(!text.isNullOrBlank()) {
                val isFilteredEmpty = adapter.filterWorksBy(text.toString()).isEmpty()
                if (isFilteredEmpty)
                    binding.noResultsOnSearch.visible()
                else
                    binding.noResultsOnSearch.gone()
            }
        }

        setOnBackPressedListener {
            if (viewModel.isSearchExpanded) {
                collapseSearch()
                false
            } else true
        }
    }

    private fun expandSearch() {
        binding.searchExpandableView.apply {
            val newWidth = binding.root.width - root.marginEnd - root.marginStart
            animateResizing(this.root, newWidth)
            searchButton.setImageResource(R.drawable.ic_close)
        }
        viewModel.isSearchExpanded = true
    }

    private fun collapseSearch() {
        binding.searchExpandableView.apply {
            val newWidth = searchButton.width
            animateResizing(this.root, newWidth)
            searchButton.setImageResource(R.drawable.ic_search)
            searchEditText.setText(EMPTY_STRING)
            requireContext().hideSoftKeyboard(this.root)
        }
        viewModel.isSearchExpanded = false
    }

    private fun animateResizing(view: View, newWidth: Int) {
        ValueAnimator.ofInt(view.width, newWidth).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                view.layoutParams.width = animation.animatedValue as Int
                view.requestLayout()
            }
            start()
        }
    }

    private fun setWorksToAdapter(works: List<Work>, isFromFile: Boolean) {
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.updateWorks(works)
            if (isFromFile) {
                showSnackBar(R.string.load_previous_data)
            }
            reactOnNoItems()
            hideProgressBar()
        }, 200)
    }

    private fun extendInDialog(work: Work) {
        requireContext().hideSoftKeyboard(binding.root)
        val dialog = ExtendedWorkDialog(work, historyType, shouldShowAll)
        dialog.show(parentFragmentManager, "ExtendedWork")
    }

    private fun reactOnNoItems() {
        with(binding) {
            if (adapter.itemCount == 0) {
                noResultsOnHistory.visible()
                noResultsOnHistoryClickable.visible()
                setOnTextViewClickListener(noResultsOnHistoryClickable)
            } else {
                noResultsOnHistory.gone()
                noResultsOnHistoryClickable.gone()
            }
        }
    }

    private fun setOnTextViewClickListener(noResultsHistoryClickable: TextView) {
        noResultsHistoryClickable.setOnClickListener {
            val navigationView: NavigationView = requireActivity().findViewById(R.id.nav_view)
            navigationView.setCheckedItem(R.id.nav_work_adding)
            setFragment(WorkAddingFragment())
        }
    }

    override fun onResume() {
        super.onResume()
        var titleResId = -1

        when (historyType) {
            Constants.Tags.WORKS_TAG -> {
                titleResId = if (shouldShowAll) R.string.all_last_works
                else R.string.your_last_works
            }
            Constants.Tags.MEETINGS_TAG -> {
                titleResId = if (shouldShowAll) R.string.all_last_meetings
                else R.string.your_last_meetings
            }
        }
        requireActivity().setTitle(titleResId)
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