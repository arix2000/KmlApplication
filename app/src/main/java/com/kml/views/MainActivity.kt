package com.kml.views

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.kml.R
import com.kml.data.app.KmlApp
import com.kml.data.services.TimerService

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        @JvmField
        var isFirstClick = true
        const val CONTROL_PANEL_ITEM_ID = 3
    }

    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawer.addDrawerListener(toggle)

        toggle.syncState()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ProfileFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_profile)
        }
        if (TimerService.isServiceRunning) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WorkTimerFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_timer)
        }
        if (KmlApp.isFromRecycleViewActivity) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, GameSearchEngineFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_search_engine)
            KmlApp.isFromRecycleViewActivity = false
        }
        if (KmlApp.isFromControlPanel) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ControlPanelFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_control_panel)
            KmlApp.isFromControlPanel = false
        }
        if (KmlApp.adminIds.contains(KmlApp.loginId)) {
            navigationView.getMenu().getItem(CONTROL_PANEL_ITEM_ID).isVisible = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.about_app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about_app -> {
                showDialogAboutApp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialogAboutApp() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_about_app)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ProfileFragment()).commit()
            R.id.nav_timer -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WorkTimerFragment()).commit()
            R.id.nav_search_engine -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, GameSearchEngineFragment()).commit()
            R.id.nav_control_panel -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ControlPanelFragment()).commit()
            R.id.nav_works_history -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WorksHistoryFragment()).commit()
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    // this method will be invoked when we click on image view in header of navigation drawer
    fun openKmlWebsite(view: View) {
        val uri = Uri.parse("https://www.klubmlodychliderow.pl/")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}