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
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.navigation.NavigationView
import com.kml.Constants.Keys.IS_FROM_NOTIFICATION_BUNDLE_KEY
import com.kml.Constants.Tags.MEETINGS_TAG
import com.kml.Constants.Tags.REMAINDER_WORKER_UNIQUE_NAME
import com.kml.Constants.Tags.WORKS_HISTORY_TYPE
import com.kml.Constants.Tags.WORKS_TAG
import com.kml.KmlApp
import com.kml.R
import com.kml.databinding.ActivityMainBinding
import com.kml.extensions.*
import com.kml.utilities.RemainderWorker
import com.kml.viewModels.MainViewModel
import com.kml.views.BaseActivity
import com.kml.views.fragments.AboutAppFragment
import com.kml.views.fragments.mainFeatures.*
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        const val CONTROL_PANEL_ITEM_ID = 3
    }

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    var isBackButtonVisible = false

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
            if (savedInstanceState == null)
                setDrawerFragment(ProfileFragment(), R.id.nav_profile)
            if (KmlApp.isFromRecycleViewActivity) {
                setDrawerFragment(GameSearchEngineFragment(), R.id.nav_search_engine)
                KmlApp.isFromRecycleViewActivity = false
            }
            if (KmlApp.isFromControlPanel) {
                setDrawerFragment(ControlPanelFragment(), R.id.nav_control_panel)
                KmlApp.isFromControlPanel = false
            }
            if (KmlApp.adminIds.contains(KmlApp.loginId))
                navView.menu.getItem(CONTROL_PANEL_ITEM_ID).isVisible = true
            handleWhenFromNotification()
            scheduleRemainderNotificationWork()

        }
    }

    private fun setDrawerFragment(fragment: Fragment, @IdRes navItemMenuRes: Int) {
        setFragment(fragment)
        binding.navView.setCheckedItem(navItemMenuRes)
    }

    private fun handleWhenFromNotification() {
        intent?.getBooleanExtra(IS_FROM_NOTIFICATION_BUNDLE_KEY, false)?.let {
            if (it) {
                setFragment(WorkAddingFragment())
                binding.navView.setCheckedItem(R.id.nav_work_adding)
            }
        }
    }

    private fun scheduleRemainderNotificationWork() {
        val calendar = Calendar.getInstance()
        if (calendar.isNotLastDayInMonth()) {
            val workRequest = OneTimeWorkRequestBuilder<RemainderWorker>()
                .setInitialDelay(calendar.getDaysUntilEndOfThisMonth().toLong(), TimeUnit.DAYS)
                .build()
            WorkManager.getInstance(this)
                .enqueueUniqueWork(REMAINDER_WORKER_UNIQUE_NAME, ExistingWorkPolicy.KEEP, workRequest)
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
        } else
            when(currentFragment) {
                is ControlPanelFragment,
                is GameSearchEngineFragment,
                is WorkAddingFragment -> setDrawerFragment(ProfileFragment(), R.id.nav_profile)
                is WorksHistoryFragment -> {
                    if (WorksHistoryFragment.shouldReturnToHome)
                        setDrawerFragment(ProfileFragment(), R.id.nav_profile)
                    else {
                        super.onBackPressed()
                        currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                    }
                }
                else -> {
                    super.onBackPressed()
                    currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                }
            }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> setFragment(ProfileFragment())
            R.id.nav_work_adding -> setFragment(WorkAddingFragment())
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

    fun showAllMeetingsModeMenu(onClick: (MenuItem) -> Unit) {
        binding.mainToolbar.menu.findItem(R.id.show_all_mode).run {
            isVisible = true
            setOnMenuItemClickListener {
                onClick(it)
                true
            }
        }
    }

    fun hideAllMeetingsModeMenu() {
        binding.mainToolbar.menu.findItem(R.id.show_all_mode).isVisible = false
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
        isBackButtonVisible = true
    }

    fun hideBackButton() {
        drawerToggle.isDrawerIndicatorEnabled = true
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        drawerToggle.syncState()
        binding.mainToolbar.menu.findItem(R.id.about_app).isVisible = true
        isBackButtonVisible = false
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