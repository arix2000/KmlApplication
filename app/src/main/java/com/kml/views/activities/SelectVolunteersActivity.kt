package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kml.Constants.Strings.EMPTY_STRING
import com.kml.R
import com.kml.adapters.VolunteerAdapter
import com.kml.data.app.KmlApp
import com.kml.databinding.ActivitySelectVolunteersBinding
import com.kml.extensions.*
import com.kml.models.Volunteer
import com.kml.viewModels.VolunteersViewModel
import com.kml.views.BaseActivity
import io.reactivex.rxjava3.kotlin.subscribeBy

class SelectVolunteersActivity : BaseActivity() {

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
        val checkedVolunteers = viewModel.volunteers.filter { it.isChecked && !it.isDisabled } as ArrayList
        if (checkedVolunteers.isNotEmpty()) {
            intent.putParcelableArrayListExtra(EXTRA_CHECKED_VOLUNTEERS, checkedVolunteers)
            intent.putExtra(EXTRA_IS_ALL_CHOSEN, viewModel.volunteers.none { !it.isChecked })
            startActivityForResult(intent, SUMMARY_RESULT)
        } else showSnackBar(R.string.volunteers_are_not_chosen)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == SUMMARY_RESULT) {
            viewModel.setCheckedVolunteersDisabled()
            volunteerAdapter.notifyDataSetChanged()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (binding.controlPanelSearchByFirstName.text.isEmpty())
            super.onBackPressed()
        else
            binding.controlPanelSearchByFirstName.setText(EMPTY_STRING)
    }

    companion object {
        const val EXTRA_CHECKED_VOLUNTEERS = "com.kml.views.activities.EXTRA_CHECKED_VOLUNTEERS"
        const val EXTRA_IS_ALL_CHOSEN = "EXTRA_IS_ALL_CHOSEN"
        const val SUMMARY_RESULT = 1
    }
}