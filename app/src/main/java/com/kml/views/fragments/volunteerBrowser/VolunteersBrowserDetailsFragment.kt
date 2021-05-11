package com.kml.views.fragments.volunteerBrowser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kml.Constants
import com.kml.Constants.Signal.EMPTY_ID
import com.kml.databinding.FragmentVolunteersBrowserDetailsBinding
import com.kml.extensions.invisible
import com.kml.extensions.logError
import com.kml.extensions.setFragmentWithData
import com.kml.extensions.visible
import com.kml.models.User
import com.kml.viewModels.VolunteersBrowserDetailsViewModel
import com.kml.views.BaseFragment
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.koin.androidx.viewmodel.ext.android.viewModel

class VolunteersBrowserDetailsFragment : BaseFragment() {

    private val viewModel: VolunteersBrowserDetailsViewModel by viewModel()
    private var _binding: FragmentVolunteersBrowserDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentVolunteersBrowserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        shouldShowBackButton = true
        attachProgressBar(binding.detailsProgressBar)
        showProgressBar()
        val id = arguments?.getInt(VolunteersBrowserFragment.VOLUNTEER_ID_KEY)
            ?: Constants.Numbers.INVALID_ID
        viewModel.fetchVolunteerData(id)
            .subscribeBy(
                onSuccess = { binding.volunteer = it; hideProgressBar() },
                onError = { logError(it); hideProgressBar() }
            )

        binding.volunteerWorksButton.setOnClickListener {
            val profile = viewModel.profile
            val bundle = Bundle().apply {
                putParcelable(USER_KEY, User(EMPTY_ID, profile.firstName, profile.lastName))
            }
            setFragmentWithData(BrowserVolunteerWorksFragment(), bundle)
        }
    }

    override fun hideProgressBar() {
        super.hideProgressBar()
        binding.volunteerDataContainer.visible()
    }

    override fun showProgressBar() {
        super.showProgressBar()
        binding.volunteerDataContainer.invisible()
    }

    companion object {
        const val USER_KEY = "USER_KEY"
    }
}