package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kml.R
import com.kml.data.models.Volunteer
import com.kml.databinding.ActivitySummarySelectedBinding
import com.kml.viewModels.SummaryVolunteerViewModel
import com.kml.views.dialogs.MyDatePickerDialog

class SummaryVolunteerActivity : AppCompatActivity() {

    private lateinit var viewModel: SummaryVolunteerViewModel
    lateinit var binding:ActivitySummarySelectedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Dokończ wpisywanie:"
        binding = DataBindingUtil.setContentView(this, R.layout.activity_summary_selected)

        viewModel = ViewModelProvider(this).get(SummaryVolunteerViewModel::class.java)

        writeChosenVolunteers()

        val sendWorkButton = findViewById<Button>(R.id.summary_activity_send_work)
        sendWorkButton.setOnClickListener {
            val hours = binding.summaryActivityHours.text.toString()
            val minutes = binding.summaryActivityMinutes.text.toString()
            val workName = binding.summaryActivityWorkName.text.toString()

            if (hours.trim().isEmpty() || minutes.trim().isEmpty() || workName.trim().isEmpty()) {
                Toast.makeText(this@SummaryVolunteerActivity, R.string.no_empty_fields, Toast.LENGTH_SHORT).show()
            } else {
                addWorkToDatabase(hours.toInt(), minutes.toInt(), workName)
                resetPools()
                backToChose()
            }
        }

        binding.newWorkCreationDate.setOnClickListener {
            val dialog = MyDatePickerDialog()
            dialog.setOnResultListener {
                binding.newWorkCreationDate.text = it
            }
            dialog.show(supportFragmentManager, "DatePicker")
        }
    }

    private fun addWorkToDatabase(hours: Int, minutes: Int, workName: String) {
        resolveResult(viewModel.addWorkToDatabase(hours, minutes, workName))
    }

    private fun resolveResult(result: Boolean) {
        if (result) {
            Toast.makeText(this, R.string.adding_work_confirmation, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.adding_work_error, Toast.LENGTH_SHORT).show()
        }
    }


    private fun backToChose() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun resetPools() {
        binding.summaryActivityHours.setText("")
        binding.summaryActivityMinutes.setText("")
        binding.summaryActivityWorkName.setText("")
    }

    private fun writeChosenVolunteers() {
        val intent = intent
        val chosenVolunteers: List<Volunteer> = intent.getParcelableArrayListExtra(EXTRA_CHECKED_VOLUNTEERS)
                ?: arrayListOf()
        viewModel.chosenVolunteers = chosenVolunteers
        binding.summaryActivityChosenVolunteers.text = viewModel.createReadableFromVolunteers()
    }

    companion object {
        const val EXTRA_CHECKED_VOLUNTEERS = "com.kml.controlPanel.EXTRA_CHECKED_VOLUNTEERS"
    }
}