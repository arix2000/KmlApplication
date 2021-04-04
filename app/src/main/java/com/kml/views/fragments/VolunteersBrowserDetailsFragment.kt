package com.kml.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kml.Constants.Numbers.INVALID_ID
import com.kml.databinding.FragmentVolunteersBrowserDetailsBinding
import com.kml.extensions.invisible
import com.kml.extensions.logError
import com.kml.extensions.visible
import com.kml.viewModels.VolunteersBrowserDetailsViewModel
import com.kml.views.BaseFragment
import com.kml.views.fragments.VolunteersBrowserFragment.Companion.VOLUNTEER_ID_KEY
import io.reactivex.rxjava3.kotlin.subscribeBy

class VolunteersBrowserDetailsFragment : BaseFragment() {

    private lateinit var viewModel: VolunteersBrowserDetailsViewModel
    private var _binding: FragmentVolunteersBrowserDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentVolunteersBrowserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VolunteersBrowserDetailsViewModel::class.java)
        shouldShowBackButton = true
        attachProgressBar(binding.detailsProgressBar)

        showProgressBar()
        val id = arguments?.getInt(VOLUNTEER_ID_KEY) ?: INVALID_ID
        viewModel.fetchVolunteerData(id)
                .subscribeBy(
                        onSuccess = { binding.volunteer = it; hideProgressBar() },
                        onError = { logError(it); hideProgressBar() }
                )
    }

    override fun hideProgressBar() {
        super.hideProgressBar()
        binding.volunteerDataContainer.visible()
    }

    override fun showProgressBar() {
        super.showProgressBar()
        binding.volunteerDataContainer.invisible()
    }
}