package com.example.tsquared;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

public class DrawerActivity extends AppCompatActivity {

    FloatingSearchView mSearchView;
    NavigationView navigationView;
    TextView profileName;
    String fullName, college;
    static String email;
    static String firstNameofUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);
        setProfileName();

        CustomViewPager viewPager = findViewById(R.id.mainViewPager);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        viewPager.setCurrentItem(getIntent().getIntExtra("page", 0));
        tabLayout.setupWithViewPager(viewPager);

        mSearchView = findViewById(R.id.searchView);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setupDrawerContent(navigationView);

        mSearchView.attachNavigationDrawerToMenuButton(drawer);
    }

    // getting data from either two activities 'UserRegister' or 'LoginActivity'and inserting it into
    // DrawerActivity
    private void setProfileName() {
        Intent intent = getIntent();

        if(intent.hasExtra("map")){
            HashMap<String, String> hashMap;
            hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");
            String firstName = hashMap.get("First Name");
            String lastName  = hashMap.get("Last Name");
            college          = hashMap.get("College");
            email            = hashMap.get("Email");
            firstNameofUser  = firstName;

            assert firstName != null;
            assert lastName  != null;
            fullName = firstName + " " + lastName;

            navigationView = findViewById(R.id.navView);
            View headerView = navigationView.getHeaderView(0);
            profileName = (TextView) headerView.findViewById(R.id.profileName);
            profileName.setText(fullName);
        }

        else if(intent.hasExtra("map1")){
            HashMap<String, String> hashMap;
            hashMap = (HashMap<String, String>)intent.getSerializableExtra("map1");
            String firstName = hashMap.get("First Name");
            String lastName  = hashMap.get("Last Name");
            college          = hashMap.get("College");
            email            = hashMap.get("Email");
            firstNameofUser  = firstName;

            assert firstName != null;
            assert lastName  != null;
            fullName = firstName + " " + lastName;

            navigationView = findViewById(R.id.navView);
            View headerView = navigationView.getHeaderView(0);
            profileName = (TextView) headerView.findViewById(R.id.profileName);
            profileName.setText(fullName);
        }
        else if (!(intent.hasExtra("map") || intent.hasExtra("map1"))){
            String firstName = PreferenceUtils.getFirstName(this);
            String lastName  = PreferenceUtils.getLastName(this);
            college          = PreferenceUtils.getCollege(this);
            email            = PreferenceUtils.getEmail(this);
            firstNameofUser  = firstName;

            fullName = firstName + " " + lastName;
            navigationView = findViewById(R.id.navView);
            View headerView = navigationView.getHeaderView(0);
            profileName = (TextView) headerView.findViewById(R.id.profileName);
            profileName.setText(fullName);
        }
    }

    public static String getEmail() {
        return email;
    }

    public static String getFirstName() {
        return firstNameofUser;
    }

    public String getFullName(){
        return fullName;
    }

    public String getCollege(){
        return college;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new QuestionsFragment(), "Questions");
        adapter.addFragment(new PeopleFragment(), "People");
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(@NonNull NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        DrawerLayout drawer = findViewById(R.id.drawer_layout);
                        selectDrawerItem(menuItem);
                        drawer.closeDrawers();
                        return true;
                    }
                });
    }
    // when logging out, change the values of user credentials, so as to avoid keeping
    // user logged in
    public void selectDrawerItem(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.logout:
                PreferenceUtils.savePassword(null, this);
                PreferenceUtils.saveEmail(null, this);
                PreferenceUtils.saveLastName(null, this);
                PreferenceUtils.saveFirstName(null, this);
                PreferenceUtils.saveCollege(null, this);
                Intent intent = new Intent(DrawerActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        int id = item.getItemId();
        switch (id){
            case R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}