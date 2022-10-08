package com.example.tsquared.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.example.tsquared.ViewPager.CustomViewPager;
import com.example.tsquared.Fragments.DiscoverNewsFragment;
import com.example.tsquared.Fragments.QuestionsFragment;
import com.example.tsquared.R;
import com.example.tsquared.Adapters.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

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
    private Handler mDrawerActionHandler;
    private AlertDialog alertDialog;
    private QuestionsFragment questionsFragment;
    private DiscoverNewsFragment discoverNewsFragment;

    public static ExtendedFloatingActionButton fab;
    private Runnable runnable;

    LoadNewQuestionListener loadNewQuestionListener;

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

        // restore fragment state after changing theme
        if(savedInstanceState != null){
            questionsFragment = (QuestionsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "questionsFragment");
            discoverNewsFragment = new DiscoverNewsFragment();
        }

        else {
            questionsFragment = new QuestionsFragment();
            discoverNewsFragment = new DiscoverNewsFragment();
        }

        setContentView(R.layout.drawer_activity);
        setUpViews();

        if(DarkSharedPref.loadNightModeState(this)){
            switchToggle.setChecked(true);
        }
        setUpDarkModeSwitch();
        setupFloatingButtonAction();
        // setProfileName(navigationView);
        viewProfileListener();

        viewPagerInit();
        setUpDrawer();
        initializeHandler();
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
                // loadNameAndCollege();
                Intent questionWindow = new Intent(getApplicationContext(), PostQuestionWindow.class);
                questionWindow.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                // questionWindow.putExtra("Full Name", fullName);
                // questionWindow.putExtra("College", college);
                activityResultLauncher.launch(questionWindow);

                // Slide activity upwards
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();

                        if(loadNewQuestionListener != null)
                            loadNewQuestionListener.insertNewQuestion(data);
                    }
                }
            });

    public void setListener(LoadNewQuestionListener loadNewQuestionListener) {
        this.loadNewQuestionListener = loadNewQuestionListener;
    }

    private void initializeHandler(){
        mDrawerActionHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        View view = menuItem.getActionView();
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
        navigationView.getMenu().getItem(0).setChecked(true);
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
        adapter.addFragment(questionsFragment, "Ask");
        adapter.addFragment(discoverNewsFragment, "Discover");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            openDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void openDialog(){
        // Change from AlertDialog to Dialog for more compact features
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View view2 = layoutInflaterAndroid.inflate(R.layout.alert_dialog, null);
        TextView textView = (TextView) view2.findViewById(R.id.Quitprompt);

        textView.setText(R.string.quit_option_prompt);

        builder.setCustomTitle(view2);
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();

        Window window = alertDialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button quitButton = (Button) alertDialog.findViewById(R.id.continueButton);
        Button continueButton = (Button) alertDialog.findViewById(R.id.quitButton);

        assert quitButton != null;
        quitButton.setText(R.string.option_yes);

        assert continueButton != null;
        continueButton.setText(R.string.option_no);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
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
    }


    private void viewProfileListener(){
        View headerView = navigationView.getHeaderView(0);
        Button viewProfile = (Button) headerView.findViewById(R.id.checkOutProfile);
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler(Looper.getMainLooper());

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(DrawerActivity.this, Profile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }, 250);
            }
        });
    }

    // when logging out, change the values of user credentials, so as to avoid keeping
    // user logged in
    @SuppressLint("NonConstantResourceId")
    public void selectDrawerItem(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.bookmarks:
                Intent bookmarks = new Intent(DrawerActivity.this, UserBookMarks.class);
                startActivity(bookmarks);
                break;

            case R.id.interests:
                Intent interests = new Intent(DrawerActivity.this, UserInterests.class);
                startActivity(interests);
                break;
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
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onPause(){
        super.onPause();
        fab.hide();
    }

    @Override
    public void finish(){
        super.finish();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public interface LoadNewQuestionListener{
        void insertNewQuestion(Intent questionData);
    }

    // This callback is called only when there is a saved instance that is previously saved by using
    // onSaveInstanceState(). We restore some state in onCreate(), while we can optionally restore
    // other state here, possibly usable after onStart() has completed.
    // The savedInstanceState Bundle is same as the one used in onCreate().
    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    // saving state of fragments when dark mode is applied
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        getSupportFragmentManager().putFragment(savedInstanceState, "questionsFragment", questionsFragment);
    }
}