package com.kml.views.fragments.volunteerBrowser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kml.adapters.VolunteerBrowserAdapter
import com.kml.databinding.FragmentVolunteersBrowserBinding
import com.kml.extensions.logError
import com.kml.extensions.setFragmentWithData
import com.kml.models.dto.Volunteer
import com.kml.viewModels.VolunteersBrowserViewModel
import com.kml.views.BaseFragment
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.koin.androidx.viewmodel.ext.android.viewModel

class VolunteersBrowserFragment : BaseFragment() {
    private val viewModel: VolunteersBrowserViewModel by viewModel()
    private var _binding: FragmentVolunteersBrowserBinding? = null
    private val binding get() = _binding!!
    private lateinit var volunteerAdapter: VolunteerBrowserAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentVolunteersBrowserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        shouldShowBackButton = true
        attachProgressBar(binding.browserProgressBar)
        setupUi()
        fetchData()
    }

    private fun fetchData() {
        viewModel.fetchVolunteers()
                .subscribeBy(
                        onSuccess = {
                            volunteerAdapter.updateData(it)
                            hideProgressBar()
                            binding.volunteersBrowserRecyclerView.scrollToPosition(viewModel.scrollState)
                        },
                        onError = { logError(it); hideProgressBar() }
                )
    }

    private fun setupUi() {
        showProgressBar()
        volunteerAdapter = VolunteerBrowserAdapter { onItemClicked(it) }
        binding.volunteersBrowserRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = volunteerAdapter
        }
    }

    private fun onItemClicked(volunteer: Volunteer) {
        viewModel.scrollState = (binding.volunteersBrowserRecyclerView.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()
        setFragmentWithData(VolunteersBrowserDetailsFragment(), Bundle().apply { putInt(VOLUNTEER_ID_KEY, volunteer.id) })
    }

    companion object {
        const val VOLUNTEER_ID_KEY = "VOLUNTEER_ID_KEY"
    }
}