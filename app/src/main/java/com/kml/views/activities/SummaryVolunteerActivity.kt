package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kml.Constants
import com.kml.Constants.Numbers.TIME_HAS_NO_VALUE
import com.kml.Constants.Strings.EMPTY_STRING
import com.kml.R
import com.kml.data.utilities.Validator
import com.kml.databinding.ActivitySummarySelectedBinding
import com.kml.extensions.asSafeString
import com.kml.extensions.hideSoftKeyboard
import com.kml.extensions.showToast
import com.kml.extensions.toIntOr
import com.kml.models.Volunteer
import com.kml.models.WorkToAdd
import com.kml.viewModels.SummaryVolunteerViewModel
import com.kml.views.dialogs.MyDatePickerDialog

class SummaryVolunteerActivity : AppCompatActivity() {

    private lateinit var viewModel: SummaryVolunteerViewModel
    lateinit var binding: ActivitySummarySelectedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.finish_writing)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_summary_selected)

        viewModel = ViewModelProvider(this).get(SummaryVolunteerViewModel::class.java)

        writeChosenVolunteers()
        binding.apply {
            operationCreationDate.text = Constants.Strings.TODAY
            operationCreationDate.setOnClickListener {
                MyDatePickerDialog().run {
                    setOnResultListener { operationCreationDate.text = it }
                    show(supportFragmentManager, "DatePicker")
                }
            }

            summaryActivitySendWork.setOnClickListener {
                this@SummaryVolunteerActivity.hideSoftKeyboard(it)
                val validator = Validator(this@SummaryVolunteerActivity)
                val creationDateString = viewModel.decideAboutDate(operationCreationDate.text.toString())
                val meetingDesc = "$creationDateString  ${summaryActivityWorkDesc.text}"
                val hours = summaryActivityHours.text.toString()
                val minutes = summaryActivityMinutes.text.toString()
                val workName = summaryActivityWorkName.text.toString()

                val workToAdd = WorkToAdd(workName, meetingDesc, hours.toIntOr(TIME_HAS_NO_VALUE), minutes.toIntOr(TIME_HAS_NO_VALUE))

                if (!validator.validateWork(workToAdd)) {
                    return@setOnClickListener
                } else {
                    addWorkToDatabase(WorkToAdd(workName.asSafeString(), meetingDesc.asSafeString(), hours.toInt(), minutes.toInt()))
                    resetPools()
                    backToChoose()
                }
            }
        }
    }

    private fun addWorkToDatabase(work: WorkToAdd) {
        resolveResult(viewModel.addWorkToDatabase(work))
    }

    private fun resolveResult(result: Boolean) {
        if (result) {
            showToast(R.string.adding_work_confirmation)
        } else {
            showToast(R.string.adding_work_error)
        }
    }

    private fun backToChoose() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun resetPools() {
        binding.summaryActivityHours.setText(EMPTY_STRING)
        binding.summaryActivityMinutes.setText(EMPTY_STRING)
        binding.summaryActivityWorkName.setText(EMPTY_STRING)
    }

    private fun writeChosenVolunteers() {
        val intent = intent
        val chosenVolunteers: List<Volunteer> = intent.getParcelableArrayListExtra(SelectVolunteersActivity.EXTRA_CHECKED_VOLUNTEERS)
                ?: arrayListOf()
        viewModel.chosenVolunteers = chosenVolunteers
        binding.summaryActivityChosenVolunteers.text = viewModel.createReadableFromVolunteers()
    }
}