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
import com.kml.models.User
import com.kml.models.Work
import com.kml.viewModels.BrowserVolunteerMeetingsViewModel
import com.kml.views.BaseFragment
import com.kml.views.dialogs.ExtendedWorkDialog
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrowserVolunteerMeetingsFragment : BaseFragment() {

    private var _binding: FragmentBrowserVolunteerMeetingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BrowserVolunteerMeetingsViewModel by viewModel()
    private lateinit var meetingsAdapter: BrowserVolunteerMeetingsAdapter

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
        meetingsAdapter = BrowserVolunteerMeetingsAdapter { extendInDialog(it) }
        fetchData()
    }

    private fun extendInDialog(work: Work) {
        ExtendedWorkDialog(work, MEETINGS_TAG)
            .show(parentFragmentManager, "ExtendedWork")
    }

    private fun fetchData() {
        showProgressBar()
        val user = arguments?.getParcelable(VolunteersBrowserDetailsFragment.USER_KEY) ?: User.EMPTY
        viewModel.fetchMeetings(user)
            .subscribeBy(
                onSuccess = {
                    meetingsAdapter.updateWorks(it, true)
                    hideProgressBar()
                    setupUi()
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
                adapter = meetingsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            typeSpinner.run {
                val typeList = resources.getStringArray(R.array.work_types).toList()
                adapter = createDefaultSpinnerAdapter(typeList)
                setOnItemSelectedListener({}) { _: View?, position: Int ->
                    meetingsAdapter.filterWorksBy(getItemAtPosition(position).toString(), typeList)
                    checkResultCount()
                }
                setSelection(0)
            }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}