package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kml.R
import com.kml.adapters.TimeAdapter
import com.kml.data.models.TimeToVolunteers
import com.kml.data.models.Volunteer
import com.kml.viewModels.TimeManagementViewModel

class TimeManagementActivity : AppCompatActivity() {

    lateinit var recycleView:RecyclerView
    lateinit var forwardButton:FloatingActionButton
    lateinit var adapter:TimeAdapter
    lateinit var viewModel:TimeManagementViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_management)

        viewModel = ViewModelProvider(this).get(TimeManagementViewModel::class.java)

        recycleView = findViewById(R.id.time_management_recycler_view)
        adapter = TimeAdapter({ openActivityWith(it) }) {removeTime(adapter.times.indexOf(it))}
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(this)

        forwardButton = findViewById(R.id.time_management_floating_button)
        forwardButton.setOnClickListener {
            Log.d("TEST_TEST_TAG", ": ${adapter.times.size}")
            adapter.addTime(TimeToVolunteers(adapter.times.size,"","", listOf()))
        }
    }

    private fun openActivityWith(lastTime: TimeToVolunteers) {
        viewModel.lastTime = lastTime
        startActivityForResult(Intent(this,SelectVolunteersActivity::class.java), 1)
    }

    private fun removeTime(position: Int): Boolean {
        adapter.times.removeAt(position)
        adapter.notifyItemRemoved(position)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val volunteers = data?.getParcelableArrayListExtra<Volunteer>(SelectVolunteersActivity.EXTRA_CHECKED_VOLUNTEERS) ?: listOf()

            viewModel.lastTime.volunteers = volunteers
            adapter.updateTime(viewModel.lastTime)
        }
    }
}