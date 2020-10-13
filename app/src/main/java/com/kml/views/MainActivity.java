package com.kml.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.kml.R;
import com.kml.data.app.KmlApp;
import com.kml.data.services.TimerService;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    // TODO moze by tak stad wpisywać dane na profil? #FUTURE
    private DrawerLayout drawer;
    public static boolean isFirstClick = true;
    NavigationView navigationView;
    private final int CONTROL_PANEL_ITEM_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_profile);
        }

        if (TimerService.isServiceRunning) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorkTimerFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_timer);
        }

        if(KmlApp.isFromRecycleViewActivity)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchEngineFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_search_engine);
            KmlApp.isFromRecycleViewActivity=false;
        }

        if(KmlApp.isFromControlPanel)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ControlPanelFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_control_panel);
            KmlApp.isFromControlPanel=false;
        }

        if(KmlApp.loginId == 9 || KmlApp.loginId == 14 || KmlApp.loginId == 18 || KmlApp.loginId == 13)
        {
            navigationView.getMenu().getItem(CONTROL_PANEL_ITEM_ID).setVisible(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.about_app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.about_app:
                showDialogAboutApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialogAboutApp()
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_about_app);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {

        switch (item.getItemId()) {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;

            case R.id.nav_timer:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorkTimerFragment()).commit();
                break;

            case R.id.nav_search_engine:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchEngineFragment()).commit();
                break;

            case R.id.nav_control_panel:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ControlPanelFragment()).commit();
                break;

            case R.id.nav_works_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorksHistoryFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    // this method will be invoked when we click on image view in header of navigation drawer
    public void openKmlWebsite(View view)
    {
        Uri uri = Uri.parse("https://www.klubmlodychliderow.pl/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
