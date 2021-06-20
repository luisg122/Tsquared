package com.example.tsquared.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;

import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.example.tsquared.Utils.PreferenceUtils;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class UserInterests extends AppCompatActivity {
    public DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Handler mDrawerActionHandler;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_interests_drawer);

        setUpViews();
        setUpDrawer();
        initializeHandler();
        setupDrawerContent(navigationView);
    }

    private void setUpViews(){
        navigationView  = findViewById(R.id.navView);
        toolbar         = findViewById(R.id.searchToolBar);
        drawer          = findViewById(R.id.drawer_layout);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpDrawer() {
        setSupportActionBar(toolbar);
        navigationView.getMenu().getItem(2).setChecked(true);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        };

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initializeHandler(){
        mDrawerActionHandler = new Handler(Looper.getMainLooper());
    }

    private void setupDrawerContent(@NonNull NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
                        mDrawerActionHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                selectDrawerItem(menuItem);
                            }
                        }, 250);

                        drawer.closeDrawers();
                        return true;
                    }
                });
    }

    // when logging out, change the values of user credentials, so as to avoid keeping
    // user logged in
    @SuppressLint("NonConstantResourceId")
    public void selectDrawerItem(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.home:
                Intent home = new Intent(UserInterests.this, DrawerActivity.class);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(home);
                finish();
                break;

            case R.id.bookmarks:
                Intent bookmarks = new Intent(UserInterests.this, UserBookMarks.class);
                startActivity(bookmarks);
                break;

            case R.id.logout:
                PreferenceUtils.savePassword(null, this);
                PreferenceUtils.saveEmail(null, this);
                PreferenceUtils.saveLastName(null, this);
                PreferenceUtils.saveFirstName(null, this);
                PreferenceUtils.saveCollege(null, this);

                DarkSharedPref.setNightModeState(false, getApplicationContext());
                DarkSharedPref.isDark = false;

                Intent logout = new Intent(UserInterests.this, LoginActivity.class);
                startActivity(logout);
                ActivityCompat.finishAffinity(UserInterests.this);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(2).setChecked(true);
    }
}
