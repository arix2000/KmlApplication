package com.kml.views.fragments.mainFeatures

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kml.Constants.Tags.GET_ALL_TAG
import com.kml.Constants.Tags.MEETINGS_TAG
import com.kml.Constants.Tags.SHOULD_SHOW_BACK_BUTTON
import com.kml.Constants.Tags.WORKS_HISTORY_TYPE
import com.kml.Constants.Tags.WORKS_TAG
import com.kml.databinding.FragmentControlPanelBinding
import com.kml.extensions.setFragment
import com.kml.extensions.setFragmentWithData
import com.kml.views.BaseFragment
import com.kml.views.activities.SelectVolunteersActivity
import com.kml.views.fragments.VolunteersBrowserFragment

class ControlPanelFragment : BaseFragment() {

    private var _binding: FragmentControlPanelBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentControlPanelBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            addWorkToChosenBtn.setOnClickListener {
                val intent = Intent(requireContext(), SelectVolunteersActivity::class.java)
                startActivity(intent)
            }
            showAllWorks.setOnClickListener {
                setFragmentWithData(WorksHistoryFragment(), createBundleFrom(WORKS_TAG))
            }
            showAllMeetings.setOnClickListener {
                setFragmentWithData(WorksHistoryFragment(), createBundleFrom(MEETINGS_TAG))
            }
            volunteersBrowser.setOnClickListener {
                setFragment(VolunteersBrowserFragment())
            }
        }
    }

    private fun createBundleFrom(type: String): Bundle {
        return Bundle().apply {
            putString(WORKS_HISTORY_TYPE, type)
            putBoolean(SHOULD_SHOW_BACK_BUTTON, true)
            putBoolean(GET_ALL_TAG, true)
        }
    }
}