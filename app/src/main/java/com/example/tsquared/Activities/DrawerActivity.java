package com.example.tsquared.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.bumptech.glide.Glide;
import com.example.tsquared.Fragments.IdeasFragment;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.example.tsquared.ViewPager.CustomViewPager;
import com.example.tsquared.Fragments.DiscoverFragment;
import com.example.tsquared.Utils.PreferenceUtils;
import com.example.tsquared.Fragments.QuestionsFragment;
import com.example.tsquared.R;
import com.example.tsquared.Adapters.ViewPagerAdapter;
import com.facebook.share.Share;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class DrawerActivity extends AppCompatActivity {
    NavigationView navigationView;
    TextView profileName;
    String fullName, college;
    static String email;
    public static String firstNameofUser;
    public DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private CustomViewPager viewPager;
    private SwitchCompat switchToggle;
    public static ExtendedFloatingActionButton fab;
    private Runnable runnable;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(DarkSharedPref.loadNightModeState(this)){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);
        setUpViews();

        if(DarkSharedPref.loadNightModeState(this)){
            switchToggle.setChecked(true);
        }
        setUpDarkModeSwitch();
        setupFloatingButtonAction();
        setProfileName(navigationView);
        viewPagerInit();
        setUpDrawer();
        setupDrawerContent(navigationView);
    }

    private void setUpDarkModeSwitch(){
        final Vibrator vibe = (Vibrator) DrawerActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        switchToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DarkSharedPref.setNightModeState(true, getApplicationContext());
                    DarkSharedPref.isDark = true;
                }
                else {
                    DarkSharedPref.setNightModeState(false, getApplicationContext());
                    DarkSharedPref.isDark = false;
                }
                vibe.vibrate(80);
                DrawerActivity.this.recreate();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void setUpViews(){
        navigationView  = findViewById(R.id.navView);
        toolbar         = findViewById(R.id.searchToolBar);
        drawer          = findViewById(R.id.drawer_layout);
        viewPager       = findViewById(R.id.mainViewPager);
        fab             = findViewById(R.id.FAB);
        switchToggle    = findViewById(R.id.darkThemeSwitch);
    }

    private void setupFloatingButtonAction() {
        fab.setClickable(true);
        fab.setFocusable(true);
        fab.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                //loadNameAndCollege();
                Intent questionWindow = new Intent(getApplicationContext(), PostQuestionWindow.class);
                questionWindow.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //questionWindow.putExtra("Full Name", fullName);
                //questionWindow.putExtra("College", college);
                startActivity(questionWindow);
                // Slide activity upwards
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.profilePictureMenu);
        View view = menuItem.getActionView();

        CircleImageView profileImage = view.findViewById(R.id.toolbar_image_profile);
        Glide.with(this)
                .load("https://seventhqueen.com/themes/kleo/wp-content/uploads/rtMedia/users/44269/2020/07/dummy-profile.png")
                .into(profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent questionWindow = new Intent(getApplicationContext(), Profile.class);
                questionWindow.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                questionWindow.putExtra("Full Name", fullName);
                startActivity(questionWindow);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //Toast.makeText(DrawerActivity.this, "Profile", Toast.LENGTH_SHORT).show();
            }
        });

        menuItem = menu.findItem(R.id.search);
        view = menuItem.getActionView();
        FloatingActionButton search = view.findViewById(R.id.fabSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchWindow = new Intent(getApplicationContext(), SearchActivity.class);
                searchWindow.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(searchWindow);
                // Slide activity upwards
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpDrawer() {
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                //invalidateOptionsMenu();
                // make sure this function is available when user is not in the 'discover' tab
                if(viewPager.getCurrentItem() == 0) fab.hide();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                //invalidateOptionsMenu();
                // make sure this function is available when user is not in the 'discover' tab
                if(viewPager.getCurrentItem() == 0) fab.show();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        };

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void viewPagerInit() {
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        viewPager.setCurrentItem(getIntent().getIntExtra("page", 0));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new QuestionsFragment(), "Ask");
        adapter.addFragment(new DiscoverFragment(), "Discover");
        adapter.addFragment(new IdeasFragment(), "Ideas");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:fab.show();
                        break;

                    default:fab.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupDrawerContent(@NonNull NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        drawer.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(toggle.onOptionsItemSelected(item)){
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

                DarkSharedPref.setNightModeState(false, getApplicationContext());
                DarkSharedPref.isDark = false;
                Intent intent = new Intent(DrawerActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        // when user starts app, maintain the state (either Dark or Light)
        DarkSharedPref.isDark = DarkSharedPref.loadNightModeState(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(viewPager.getCurrentItem() == 0) fab.show();
    }

    @Override
    public void onPause(){
        super.onPause();
        fab.hide();
    }
}