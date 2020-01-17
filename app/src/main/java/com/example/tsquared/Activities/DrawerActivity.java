package com.example.tsquared.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.tsquared.ViewPager.CustomViewPager;
import com.example.tsquared.Fragments.PeopleFragment;
import com.example.tsquared.Utils.PreferenceUtils;
import com.example.tsquared.Fragments.QuestionsFragment;
import com.example.tsquared.R;
import com.example.tsquared.Adapters.ViewPagerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

public class DrawerActivity extends AppCompatActivity {

    FloatingSearchView mSearchView;
    NavigationView navigationView;
    TextView profileName;
    String fullName, college;
    static String email;
    public static String firstNameofUser;
    public DrawerLayout drawer;
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);

        navigationView = findViewById(R.id.navView);
        setProfileName(navigationView);
        viewPagerInit();
        setUpDrawer();
        setupDrawerContent(navigationView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpDrawer() {
        toolbar = findViewById(R.id.searchToolBar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void viewPagerInit() {
        CustomViewPager viewPager = findViewById(R.id.mainViewPager);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        viewPager.setCurrentItem(getIntent().getIntExtra("page", 0));
        tabLayout.setupWithViewPager(viewPager);
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

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
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
            case R.id.search:
                Intent i = new Intent(this, SearchActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    // getting data from either two activities 'UserRegister' or 'LoginActivity'and inserting it into
    // DrawerActivity
    private void setProfileName(NavigationView navigationView) {
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
            View headerView = navigationView.getHeaderView(0);
            profileName = (TextView) headerView.findViewById(R.id.profileName);
            profileName.setText(fullName);
        }
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
}