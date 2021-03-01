package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kml.Constants.Signal
import com.kml.adapters.TimeAdapter
import com.kml.data.models.TimeToVolunteers
import com.kml.data.models.Volunteer
import com.kml.databinding.ActivityTimeManagementBinding
import com.kml.extensions.log
import com.kml.viewModels.TimeManagementViewModel
import com.kml.views.activities.SummaryVolunteerActivity.Companion.EXTRA_CHECKED_VOLUNTEERS
import java.util.*

class TimeManagementActivity : AppCompatActivity() {

    lateinit var timeAdapter: TimeAdapter
    lateinit var viewModel: TimeManagementViewModel
    lateinit var binding: ActivityTimeManagementBinding

    companion object {
        const val EXTRA_CLICKED_TIME = "com.kml.views.activities.EXTRA_CLICKED_TIME"
        const val EXTRA_ALL_TIMES = "com.kml.views.activities.EXTRA_ALL_TIMES"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(TimeManagementViewModel::class.java)

        binding.timeManagementRecyclerView.run {
            timeAdapter = TimeAdapter({ onItemArrowClicked(it) }) { removeTime(timeAdapter.times.indexOf(it)) }
            this.adapter = timeAdapter
            layoutManager = LinearLayoutManager(this@TimeManagementActivity) //TODO i don't know..
        }

        binding.timeManagementAddButton.setOnClickListener {
            timeAdapter.addTime(TimeToVolunteers(timeAdapter.times.size, "", "", listOf()))
        }

        binding.timeManagementNextButton.setOnClickListener {
            goToSummary()
        }
    }

    private fun onItemArrowClicked(it: TimeToVolunteers) {
        val validationResult = viewModel.itemValidation(it.hours, it.minutes)
        if (validationResult != Signal.VALIDATION_SUCCESSFUL) {
            Toast.makeText(this, validationResult, Toast.LENGTH_SHORT).show()
            return
        }

        log("id: "+it.id+" "+it.hours+"h "+it.minutes+"min")
        viewModel.lastTime = it
        val intent = createIntentWith(it, timeAdapter.times)
        openActivityWith(intent)
    }

    private fun createIntentWith(clickedTime: TimeToVolunteers, times: MutableList<TimeToVolunteers>): Intent {
        val intent = Intent(Intent(this, SelectVolunteersActivity::class.java))
        intent.putExtra(EXTRA_CLICKED_TIME, clickedTime)
        intent.putParcelableArrayListExtra(EXTRA_ALL_TIMES, times as ArrayList)
        return intent
    }

    private fun openActivityWith(intent: Intent) {
        startActivityForResult(intent, 1)
    }

    private fun removeTime(position: Int): Boolean {
        timeAdapter.times.removeAt(position)
        timeAdapter.notifyItemRemoved(position)
        recreateIdsOn(position)
        return true
    }

    private fun recreateIdsOn(position: Int) {
        for (i in position until timeAdapter.times.size)
            timeAdapter.times[i].id = i
    }

    private fun goToSummary() {
        val intent = Intent(this,SummaryVolunteerActivity::class.java)
        intent.putParcelableArrayListExtra(EXTRA_CHECKED_VOLUNTEERS, timeAdapter.times as ArrayList)
        //FIXME godziny są źle przypisane
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val volunteers = data?.getParcelableArrayListExtra<Volunteer>(EXTRA_CHECKED_VOLUNTEERS)
                    ?: listOf()

            viewModel.lastTime.volunteers = volunteers
            timeAdapter.updateTime(viewModel.lastTime)
        }
    }
}