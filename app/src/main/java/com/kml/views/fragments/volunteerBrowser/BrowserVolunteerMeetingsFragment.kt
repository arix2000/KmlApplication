package com.kml.views.fragments.volunteerBrowser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kml.Constants.Tags.MEETINGS_TAG
import com.kml.R
import com.kml.adapters.BrowserVolunteerMeetingsAdapter
import com.kml.databinding.FragmentBrowserVolunteerMeetingsBinding
import com.kml.extensions.*
import com.kml.models.dto.Work
import com.kml.models.model.User
import com.kml.viewModels.BrowserVolunteerLogbookViewModel.Companion.LAST_YEARS_POSITION
import com.kml.viewModels.BrowserVolunteerMeetingsViewModel
import com.kml.views.BaseFragment
import com.kml.views.activities.MainActivity
import com.kml.views.dialogs.ExtendedWorkDialog
import com.kml.views.fragments.volunteerBrowser.VolunteersBrowserDetailsFragment.Companion.USER_KEY
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrowserVolunteerMeetingsFragment : BaseFragment() {

    private var _binding: FragmentBrowserVolunteerMeetingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BrowserVolunteerMeetingsViewModel by viewModel()
    private lateinit var meetingsAdapter: BrowserVolunteerMeetingsAdapter
    private var isAllMeetingsModeEnabled = false
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowserVolunteerMeetingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        shouldShowBackButton = true
        attachProgressBar(binding.progressBar)
        user = arguments?.getParcelable(USER_KEY) ?: User.EMPTY
        setTitle("${user.firstName} ${user.lastName}")
        meetingsAdapter = BrowserVolunteerMeetingsAdapter(user) { extendInDialog(it) }
        fetchData(true)
    }

    override fun onStart() {
        (activity as? MainActivity)?.showAllMeetingsModeMenu {
            if (isAllMeetingsModeEnabled)
                stopAllMeetingsMode()
            else
                startAllMeetingsMode()
        }
        super.onStart()
    }

    private fun startAllMeetingsMode() {
        showProgressBar()
        viewModel.fetchAllMeetings()
            .subscribeBy(
                onSuccess = {
                    meetingsAdapter.updateWorksOnAllMeetingsModeToggle(it)
                    hideProgressBar()
                    isAllMeetingsModeEnabled = true
                },
                onError = {
                    logError(it)
                    hideProgressBar()
                }
            )
    }

    private fun stopAllMeetingsMode() {
        fetchData(false)
    }

    private fun extendInDialog(work: Work) {
        ExtendedWorkDialog(work, MEETINGS_TAG)
            .show(parentFragmentManager, "ExtendedWork")
    }

    private fun fetchData(isInitialSet: Boolean) {
        showProgressBar()
        viewModel.fetchMeetings(user)
            .subscribeBy(
                onSuccess = {
                    if (isInitialSet)
                        meetingsAdapter.updateWorks(it, true)
                    else
                        meetingsAdapter.updateWorksOnAllMeetingsModeToggle(it)
                    hideProgressBar()
                    setupUi()
                    isAllMeetingsModeEnabled = false
                },
                onError = {
                    logError(it)
                    hideProgressBar()
                    setupUi()
                }
            )
    }

    private fun setupUi() {
        with(binding) {
            recyclerView.run {
                setHasFixedSize(true)
                adapter = meetingsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            searchButton.setOnClickListener {
                filterWorks()
            }
            setupSpinners()
        }
    }

    private fun checkResultCount() {
        with(binding) {
            if (meetingsAdapter.isWorksEmpty())
                noResultsView.visible()
            else
                noResultsView.gone()
        }
    }

    private fun FragmentBrowserVolunteerMeetingsBinding.setupSpinners() {
        typeSpinner.run {
            val typeList = resources.getStringArray(R.array.work_types).toList()
            adapter = createDefaultSpinnerAdapter(typeList)
            setSelection(0)
        }
        yearSpinner.run {
            adapter = createDefaultSpinnerAdapter(viewModel.getYearList())
            setOnItemSelectedListener({}) { view: View?, i: Int ->
                monthSpinner.adapter = createMonthArrayAdapter()
            }
            setSelection(LAST_YEARS_POSITION)
        }

        monthSpinner.run {
            adapter = createMonthArrayAdapter()
            setSelection(0)
        }
        filterWorks()
    }

    private fun createMonthArrayAdapter() =
        binding.monthSpinner.createDefaultSpinnerAdapter(
            viewModel.getMonthList(
                viewModel.isCurrentYear(binding.yearSpinner.selectedItem.toString().toInt())
            )
        )

    private fun filterWorks() {
        with(binding) {
            meetingsAdapter.filterWorksBy(
                typeSpinner.selectedItem.toString(),
                resources.getStringArray(R.array.work_types).toList(),
                monthSpinner.selectedItemPosition.toString(),
                yearSpinner.selectedItem.toString()
            )
        }
        checkResultCount()
        setTotalsBy(meetingsAdapter.itemCount, meetingsAdapter.getWorksTimeTotal())
    }

    private fun setTotalsBy(workCount: Int, workTime: String) {
        with(binding) {
            totalWorkCount.text = workCount.toString()
            totalWorkTime.text = workTime
        }
    }

    override fun onStop() {
       (activity as? MainActivity)?.hideAllMeetingsModeMenu()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ALL_TYPES = "Wszystkie"
    }
}