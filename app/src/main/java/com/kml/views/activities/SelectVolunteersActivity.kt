package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kml.R
import com.kml.adapters.VolunteerAdapter
import com.kml.data.app.KmlApp
import com.kml.data.models.TimeToVolunteers
import com.kml.data.models.Volunteer
import com.kml.viewModels.VolunteersViewModel

class SelectVolunteersActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CHECKED_VOLUNTEERS = "com.kml.controlPanel.EXTRA_CHECKED_VOLUNTEERS"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VolunteerAdapter
    private lateinit var viewModel: VolunteersViewModel


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_volunteers)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "Zaznacz wolontariuszy:"
        KmlApp.isFromControlPanel = true

        viewModel = ViewModelProvider(this).get(VolunteersViewModel::class.java)

        createRecycleView()

        viewModel.volunteers.observe(this) {
            adapter.volunteers = it
        }

        mergeWithParentList()

        initClickableTextViews()
        initSearchEditText()

        val actionButton = findViewById<FloatingActionButton>(R.id.control_panel_floating_button)
        actionButton.setOnClickListener { sendIntentWithCheckedList() }
    }

    private fun createRecycleView() {
        recyclerView = findViewById(R.id.control_panel_recycle_view)
        adapter = VolunteerAdapter { handleOnClick(it) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun handleOnClick(volunteer: Volunteer) {
        volunteer.isChecked = !volunteer.isChecked
    }

    private fun mergeWithParentList() {
        val time = intent.getParcelableExtra<TimeToVolunteers>(TimeManagementActivity.EXTRA_CLICKED_TIME)
                ?: TimeToVolunteers(-1,"","", listOf())
        val allTimes = intent.getParcelableArrayListExtra<TimeToVolunteers>(TimeManagementActivity.EXTRA_ALL_TIMES)
                ?: listOf()

        viewModel.chooseSelectableWith(time, allTimes)
    }

    private fun initClickableTextViews() {
        val selectAllTextView = findViewById<TextView>(R.id.control_panel_select_all)
        selectAllTextView.setOnClickListener {
            viewModel.selectAllVolunteers()
            adapter.volunteers = viewModel.volunteers.value!!
        }

        val deselectAllTextView = findViewById<TextView>(R.id.control_panel_deselect)
        deselectAllTextView.setOnClickListener {
            viewModel.deselectAllVolunteers()
            adapter.volunteers = viewModel.volunteers.value!!
        }
    }

    private fun initSearchEditText() {
        val searchEditText = findViewById<EditText>(R.id.control_panel_search_by_first_name)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                adapter.volunteers = viewModel.filterArrayByName(editable.toString())
            }
        })
    }

    private fun sendIntentWithCheckedList() {
        val checkedVolunteers = viewModel.volunteers.value?.filter { it.isChecked } as ArrayList
        if (checkedVolunteers.isNotEmpty()) {
            val intent = Intent()
            intent.putParcelableArrayListExtra(EXTRA_CHECKED_VOLUNTEERS, checkedVolunteers)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, R.string.choose_one_at_least, Toast.LENGTH_SHORT).show()
        }
    }
}