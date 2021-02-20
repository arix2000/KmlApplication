package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kml.Constants.Strings.EMPTY_STRING
import com.kml.adapters.VolunteerAdapter
import com.kml.data.app.KmlApp
import com.kml.data.models.TimeToVolunteers
import com.kml.data.models.Volunteer
import com.kml.databinding.ActivitySelectVolunteersBinding
import com.kml.viewModels.VolunteersViewModel

class SelectVolunteersActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CHECKED_VOLUNTEERS = "com.kml.controlPanel.EXTRA_CHECKED_VOLUNTEERS"
    }

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

        mergeWithParentList()

        createRecycleView()

        volunteerAdapter.updateVolunteers(viewModel.volunteers)

        initClickableTextViews()
        initSearchEditText()

        binding.controlPanelFloatingButton.setOnClickListener {
            if (binding.controlPanelSearchByFirstName.text.isEmpty())
                sendIntentWithCheckedList()
            else
                binding.controlPanelSearchByFirstName.setText(EMPTY_STRING)
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

    private fun mergeWithParentList() {
        val time = intent.getParcelableExtra(TimeManagementActivity.EXTRA_CLICKED_TIME)
                ?: TimeToVolunteers(-1, "", "", listOf())
        val allTimes = intent.getParcelableArrayListExtra<TimeToVolunteers>(TimeManagementActivity.EXTRA_ALL_TIMES)
                ?: listOf()

        viewModel.chooseEnabledWith(allTimes)
        viewModel.chooseCheckedWith(time)
        viewModel.previousCheckedVolunteers = viewModel.volunteers.filter { it.isChecked }
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
        binding.controlPanelSearchByFirstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                volunteerAdapter.updateVolunteers(viewModel.filterArrayByName(editable.toString()))
            }
        })
    }

    private fun sendIntentWithCheckedList() {
        val checkedVolunteers = viewModel.volunteers.filter { it.isChecked } as ArrayList
        setResult(RESULT_OK, Intent().putParcelableArrayListExtra(EXTRA_CHECKED_VOLUNTEERS, checkedVolunteers))
        finish()
    }

    override fun onBackPressed() {
        if (binding.controlPanelSearchByFirstName.text.isEmpty())
            super.onBackPressed()
        else
            binding.controlPanelSearchByFirstName.setText(EMPTY_STRING)
    }
}