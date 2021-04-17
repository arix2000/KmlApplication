package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kml.Constants.Strings.EMPTY_STRING
import com.kml.R
import com.kml.adapters.VolunteerAdapter
import com.kml.data.app.KmlApp
import com.kml.databinding.ActivitySelectVolunteersBinding
import com.kml.extensions.gone
import com.kml.extensions.logError
import com.kml.extensions.showSnackBar
import com.kml.extensions.visible
import com.kml.models.Volunteer
import com.kml.viewModels.VolunteersViewModel
import io.reactivex.rxjava3.kotlin.subscribeBy

class SelectVolunteersActivity : AppCompatActivity() {

    private lateinit var volunteerAdapter: VolunteerAdapter
    private lateinit var viewModel: VolunteersViewModel
    private lateinit var binding: ActivitySelectVolunteersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectVolunteersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Zaznacz wolontariuszy:"
        KmlApp.isFromControlPanel = true

        viewModel = ViewModelProvider(this).get(VolunteersViewModel::class.java)

        createRecycleView()

        viewModel.fetchVolunteers()
                .doOnSubscribe { binding.selectVolunteersProgressBar.visible() }
                .subscribeBy(
                        onSuccess = {
                            volunteerAdapter.updateVolunteers(it)
                            binding.selectVolunteersProgressBar.gone()
                        },
                        onError = { logError(it);binding.selectVolunteersProgressBar.gone() }
                )

        initClickableTextViews()
        initSearchEditText()

        binding.controlPanelFloatingButton.setOnClickListener {
            sendIntentWithCheckedList()
        }
    }

    private fun createRecycleView() {
        binding.controlPanelRecycleView.run {
            volunteerAdapter = VolunteerAdapter { handleOnClick(it) }
            this.layoutManager = LinearLayoutManager(this@SelectVolunteersActivity)
            this.adapter = volunteerAdapter
        }
    }

    private fun handleOnClick(volunteer: Volunteer) {
        volunteer.isChecked = !volunteer.isChecked
    }

    private fun initClickableTextViews() {
        binding.controlPanelSelectAll.setOnClickListener {
            viewModel.selectAllVolunteers()
            volunteerAdapter.updateVolunteers(viewModel.volunteers)
        }

        binding.controlPanelDeselect.setOnClickListener {
            viewModel.deselectAllVolunteers()
            volunteerAdapter.updateVolunteers(viewModel.volunteers)
        }
    }

    private fun initSearchEditText() {
        binding.controlPanelSearchByFirstName.doAfterTextChanged {
            volunteerAdapter.updateVolunteers(viewModel.filterArrayByName(it.toString()))
        }
    }

    private fun sendIntentWithCheckedList() {
        val intent = Intent(this, SummaryVolunteerActivity::class.java)
        val checkedVolunteers = viewModel.volunteers.filter { it.isChecked } as ArrayList
        if (checkedVolunteers.isNotEmpty()) {
            intent.putParcelableArrayListExtra(EXTRA_CHECKED_VOLUNTEERS, checkedVolunteers)
            startActivity(intent)
        } else showSnackBar(R.string.volunteers_are_not_chosen)
    }

    override fun onBackPressed() {
        if (binding.controlPanelSearchByFirstName.text.isEmpty())
            super.onBackPressed()
        else
            binding.controlPanelSearchByFirstName.setText(EMPTY_STRING)
    }

    companion object {
        const val EXTRA_CHECKED_VOLUNTEERS = "com.kml.views.activities.EXTRA_CHECKED_VOLUNTEERS"
    }
}