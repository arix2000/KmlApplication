package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kml.R
import com.kml.adapters.TimeAdapter
import com.kml.data.models.TimeToVolunteers
import com.kml.data.models.Volunteer
import com.kml.data.utilities.Signal
import com.kml.viewModels.TimeManagementViewModel
import java.util.*

class TimeManagementActivity : AppCompatActivity() {

    lateinit var recycleView: RecyclerView
    lateinit var forwardButton: FloatingActionButton
    lateinit var adapter: TimeAdapter
    lateinit var viewModel: TimeManagementViewModel

    companion object {
        const val EXTRA_CLICKED_TIME = "com.kml.views.activities.EXTRA_CLICKED_TIME"
        const val EXTRA_ALL_TIMES = "com.kml.views.activities.EXTRA_ALL_TIMES"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_management)

        viewModel = ViewModelProvider(this).get(TimeManagementViewModel::class.java)

        recycleView = findViewById(R.id.time_management_recycler_view)
        adapter = TimeAdapter({ onItemArrowClicked(it) }) { removeTime(adapter.times.indexOf(it)) }
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(this)

        forwardButton = findViewById(R.id.time_management_floating_button)
        forwardButton.setOnClickListener {
            adapter.addTime(TimeToVolunteers(adapter.times.size, "", "", listOf()))
        }
    }

    private fun onItemArrowClicked(it: TimeToVolunteers) {
        val validationResult = viewModel.itemValidation(it.hours, it.minutes)
        if (validationResult != Signal.VALIDATION_SUCCESSFUL) {
            Toast.makeText(this, validationResult, Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.lastTime = it
        val intent = createIntentWith(it, adapter.times)
        openActivityWith(intent)
    }

    private fun createIntentWith(clickedTime: TimeToVolunteers, times: MutableList<TimeToVolunteers>): Intent {
        val intent = Intent(Intent(this, SelectVolunteersActivity::class.java))
        intent.putExtra(EXTRA_CLICKED_TIME, clickedTime)
        intent.putParcelableArrayListExtra(EXTRA_ALL_TIMES, times as ArrayList<TimeToVolunteers>)
        return intent
    }

    private fun openActivityWith(intent: Intent) {
        startActivityForResult(intent, 1)
    }

    private fun removeTime(position: Int): Boolean {
        adapter.times.removeAt(position)
        adapter.notifyItemRemoved(position)
        recreateIdsOn(position)
        return true
    }

    private fun recreateIdsOn(position: Int) {
        for (i in position until adapter.times.size)
            adapter.times[i].id = i
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val volunteers = data?.getParcelableArrayListExtra<Volunteer>(SelectVolunteersActivity.EXTRA_CHECKED_VOLUNTEERS)
                    ?: listOf()

            viewModel.lastTime.volunteers = volunteers
            adapter.updateTime(viewModel.lastTime)
        }
    }
}