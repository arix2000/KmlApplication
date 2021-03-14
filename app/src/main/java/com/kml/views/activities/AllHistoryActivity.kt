package com.kml.views.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.kml.Constants.Extras.IS_FROM_FILE_EXTRA
import com.kml.Constants.Extras.WORKS_EXTRA
import com.kml.R
import com.kml.adapters.WorkAdapter
import com.kml.data.app.KmlApp
import com.kml.data.models.Work
import com.kml.views.dialogs.ExtendedWorkDialog
import com.kml.views.fragments.mainFeatures.WorkTimerFragment

class AllHistoryActivity : AppCompatActivity() {
    private lateinit var adapter: WorkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        KmlApp.isFromWorksHistory = true

        adapter = WorkAdapter { extendInDialog(it) }
        val recyclerView: RecyclerView = findViewById(R.id.works_history_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val works = intent.getParcelableArrayListExtra<Work>(WORKS_EXTRA) ?: listOf()
        val isFromFile = intent.getBooleanExtra(IS_FROM_FILE_EXTRA, true)
        setWorksToAdapter(works ,isFromFile)
    }

    override fun onResume() {
        super.onResume()
        setTitle(R.string.your_last_works)
    }

    private fun setWorksToAdapter(works: List<Work>, isFromFile: Boolean) {
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.works = works
            if (isFromFile) {
                Toast.makeText(this, R.string.load_previous_data, Toast.LENGTH_SHORT).show()
            }
            reactOnNoItems()
        }, 200)
    }

    private fun extendInDialog(work: Work) {
        val dialog = ExtendedWorkDialog(work)
        dialog.show(supportFragmentManager, "ExtendedWork")
    }

    private fun reactOnNoItems() {
        val noResultsHistory = findViewById<TextView>(R.id.no_results_on_history)
        val noResultsHistoryClickable = findViewById<TextView>(R.id.no_results_on_history_clickable)
        if (adapter.itemCount == 0) {
            noResultsHistory.visibility = View.VISIBLE
            noResultsHistoryClickable.visibility = View.VISIBLE
            setOnItemClickListener(noResultsHistoryClickable)
        }
    }

    private fun setOnItemClickListener(noResultsHistoryClickable: TextView) {
        noResultsHistoryClickable.setOnClickListener {
            val navigationView: NavigationView = findViewById(R.id.nav_view)
            navigationView.setCheckedItem(R.id.nav_timer)
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WorkTimerFragment()).commit()
        }
    }

    override fun onPause() {
        super.onPause()
        setTitle(R.string.app_name)
    }
}