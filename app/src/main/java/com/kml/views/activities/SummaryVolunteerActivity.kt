package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kml.Constants
import com.kml.Constants.Signal.MAXIMUM_PERMITTED_HOURS
import com.kml.Constants.Strings.EMPTY_STRING
import com.kml.R
import com.kml.databinding.ActivitySummarySelectedBinding
import com.kml.models.Volunteer
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
                val creationDateString = viewModel.decideAboutDate(operationCreationDate.text.toString())
                val meetingDesc = "$creationDateString -> ${summaryActivityWorkDesc.text}" //TODO something wrong with adding to chosen
                val hours = summaryActivityHours.text.toString()
                val minutes = summaryActivityMinutes.text.toString()
                val workName = summaryActivityWorkName.text.toString()
//TODO refactor and create validation class!!!
                if (hours.trim().isEmpty() || minutes.trim().isEmpty() || workName.trim().isEmpty()) {
                    Toast.makeText(this@SummaryVolunteerActivity, R.string.no_empty_fields, Toast.LENGTH_SHORT).show()
                } else if(hours.toInt()>MAXIMUM_PERMITTED_HOURS) {
                    Toast.makeText(this@SummaryVolunteerActivity, R.string.too_many_hours, Toast.LENGTH_SHORT).show()
                } else if(minutes.toInt()>60) {
                    Toast.makeText(this@SummaryVolunteerActivity, R.string.too_many_minutes, Toast.LENGTH_SHORT).show()
                }
                else {
                    addWorkToDatabase(hours.toInt(), minutes.toInt(), workName, meetingDesc)
                    resetPools()
                    backToChoose()
                }
            }
        }
    }

    private fun addWorkToDatabase(hours: Int, minutes: Int, workName: String, meetingDesc: String) {
        resolveResult(viewModel.addWorkToDatabase(hours, minutes, workName, meetingDesc))
    }

    private fun resolveResult(result: Boolean) {
        if (result) {
            Toast.makeText(this, R.string.adding_work_confirmation, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.adding_work_error, Toast.LENGTH_SHORT).show()
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