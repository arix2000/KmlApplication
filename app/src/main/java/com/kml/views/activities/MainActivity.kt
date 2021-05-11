package com.kml.views.activities

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.kml.Constants.Tags.MEETINGS_TAG
import com.kml.Constants.Tags.WORKS_HISTORY_TYPE
import com.kml.Constants.Tags.WORKS_TAG
import com.kml.KmlApp
import com.kml.R
import com.kml.databinding.ActivityMainBinding
import com.kml.extensions.setFragment
import com.kml.extensions.setFragmentWithData
import com.kml.viewModels.MainViewModel
import com.kml.views.BaseActivity
import com.kml.views.fragments.AboutAppFragment
import com.kml.views.fragments.mainFeatures.*
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        const val CONTROL_PANEL_ITEM_ID = 3
    }

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        with(binding) {
            setWorksHistoryStyle()
            navView.setNavigationItemSelectedListener(this@MainActivity)
            drawerToggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
            )
            drawerLayout.addDrawerListener(drawerToggle)
            setupOptions()

            drawerToggle.syncState()
            if (savedInstanceState == null) {
                setFragment(ProfileFragment())
                navView.setCheckedItem(R.id.nav_profile)
            }
            if (KmlApp.isFromRecycleViewActivity) {
                setFragment(GameSearchEngineFragment())
                navView.setCheckedItem(R.id.nav_search_engine)
                KmlApp.isFromRecycleViewActivity = false
            }
            if (KmlApp.isFromControlPanel) {
                setFragment(ControlPanelFragment())
                navView.setCheckedItem(R.id.nav_control_panel)
                KmlApp.isFromControlPanel = false
            }
            if (KmlApp.adminIds.contains(KmlApp.loginId)) {
                navView.menu.getItem(CONTROL_PANEL_ITEM_ID).isVisible = true
            }
        }
    }

    private fun setWorksHistoryStyle() {
        val itemTitle = binding.navView.menu.findItem(R.id.works_history_title)
        val spannable = SpannableString(itemTitle.title)
        spannable.setSpan(TextAppearanceSpan(this, R.style.nav_drawer_works_history_title), 0, spannable.length, 0)
        itemTitle.title = spannable
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.about_app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about_app -> {
                showFragmentAboutApp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showFragmentAboutApp() {
        setFragment(AboutAppFragment(), true, true)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> setFragment(ProfileFragment())
            R.id.nav_timer -> setFragment(WorkAddingFragment())
            R.id.nav_search_engine -> setFragment(GameSearchEngineFragment())
            R.id.nav_control_panel -> setFragment(ControlPanelFragment())
            R.id.nav_works_history -> setFragmentWithData(WorksHistoryFragment(), getWorksBundleByTag(WORKS_TAG))
            R.id.nav_meetings_history -> setFragmentWithData(WorksHistoryFragment(), getWorksBundleByTag(MEETINGS_TAG))

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getWorksBundleByTag(tag: String): Bundle {
        return Bundle().also {
            it.putString(WORKS_HISTORY_TYPE, tag)
        }
    }

    fun showBackButton() {
        drawerToggle.isDrawerIndicatorEnabled = false
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        drawerToggle.setToolbarNavigationClickListener {
            onBackPressed()
        }
        binding.drawerLayout.isEnabled = false
        binding.mainToolbar.menu.findItem(R.id.about_app).isVisible = false
    }

    fun hideBackButton() {
        drawerToggle.isDrawerIndicatorEnabled = true
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        drawerToggle.syncState()
        binding.mainToolbar.menu.findItem(R.id.about_app).isVisible = true
    }

    /** this method will be invoked when we click on image view in header of navigation drawer */
    fun openKmlWebsite(view: View) {
        val uri = Uri.parse("https://www.klubmlodychliderow.pl/")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun setupOptions() {
        with(binding) {
            logOut.root.setOnClickListener {
                viewModel.clearLogData()
                startActivity(Intent(this@MainActivity, LoginScreen::class.java))
                finish()
            }
            darkMode.root.setOnClickListener {
                changeAppMode()
                viewModel.saveSwitchDarkMode(!darkMode.stateSwitch.isChecked)
                darkMode.stateSwitch.isChecked = !darkMode.stateSwitch.isChecked
            }
            restoreDarkModeSwitchState()
        }
    }

    private fun changeAppMode() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Configuration.UI_MODE_NIGHT_NO ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun restoreDarkModeSwitchState() {
        val isDarkMode = viewModel.getSwitchDarkModeState()
        if (isDarkMode) {
            binding.darkMode.stateSwitch.isChecked = isDarkMode
        }
    }
}