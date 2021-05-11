package com.kml.views.fragments.volunteerBrowser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kml.Constants.Tags.WORKS_TAG
import com.kml.adapters.BrowserVolunteerWorksAdapter
import com.kml.databinding.FragmentBrowserVolunteerWorksBinding
import com.kml.extensions.*
import com.kml.models.User
import com.kml.models.Work
import com.kml.viewModels.BrowserVolunteerWorksViewModel
import com.kml.viewModels.BrowserVolunteerWorksViewModel.Companion.LAST_YEARS_POSITION
import com.kml.viewModels.BrowserVolunteerWorksViewModel.Companion.SHOW_ALL_ITEM_POSITION
import com.kml.views.BaseFragment
import com.kml.views.dialogs.ExtendedWorkDialog
import com.kml.views.fragments.volunteerBrowser.VolunteersBrowserDetailsFragment.Companion.USER_KEY
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrowserVolunteerWorksFragment : BaseFragment() {

    private val viewModel: BrowserVolunteerWorksViewModel by viewModel()
    private var _binding: FragmentBrowserVolunteerWorksBinding? = null
    private val binding get() = _binding!!
    private lateinit var worksAdapter: BrowserVolunteerWorksAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentBrowserVolunteerWorksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        shouldShowBackButton = true
        attachProgressBar(binding.progressBar)
        worksAdapter = BrowserVolunteerWorksAdapter { extendInDialog(it) }
        val user = arguments?.getParcelable(USER_KEY) ?: User.EMPTY
        fetchWorks(user)
        setupUi()
    }

    private fun fetchWorks(user: User) {
        showProgressBar()
        viewModel.fetchWorks(user)
                .subscribeBy(
                        onSuccess = {
                            worksAdapter.updateWorks(it, true)
                            setupSpinners()
                            hideProgressBar()
                        },
                        onError = { logError(it); hideProgressBar() }
                )
    }

    private fun extendInDialog(work: Work) {
        ExtendedWorkDialog(work, WORKS_TAG, true)
                .show(parentFragmentManager, "ExtendedWork")
    }

    private fun setupUi() {
        with(binding) {
            recyclerView.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = worksAdapter
            }
        }
    }

    private fun setupSpinners() {
        with(binding) {
            with(yearSpinner) {
                adapter = createDefaultSpinnerAdapter(viewModel.getYearList())
                setOnItemSelectedListener({}) { _, position ->
                    val selectedYear = adapter.getItem(position).toString()
                    with(monthSpinner) {
                        adapter = createDefaultSpinnerAdapter(
                                viewModel.getMonthList(viewModel.isCurrentYear(selectedYear.toInt()))
                        )
                        setSelection(adapter.count.dec())
                    }
                }
                setSelection(LAST_YEARS_POSITION)
            }

            monthSpinner.setOnItemSelectedListener({}) { _, position ->
                if (position != SHOW_ALL_ITEM_POSITION)
                    worksAdapter.filterWorksBy(position.toString(), yearSpinner.selectedItem.toString())
                else
                    worksAdapter.showAllWorks()

                if (worksAdapter.isWorksEmpty())
                    noResultsView.visible()
                else
                    noResultsView.gone()
            }
        }
    }
}