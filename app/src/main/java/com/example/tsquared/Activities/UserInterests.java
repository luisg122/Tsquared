package com.example.tsquared.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.tsquared.Adapters.InterestsAdapter;
import com.example.tsquared.Models.InterestsModel;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.example.tsquared.Utils.PreferenceUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserInterests extends AppCompatActivity implements InterestsAdapter.OnCheckClickListener {
    public DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Handler mDrawerActionHandler;
    private TextView keepTrackNum;

    private ArrayList<InterestsModel> mArrayList;
    private RecyclerView mainRv;
    private InterestsAdapter adapter;
    private ArrayList<Integer> numberOfCheckItems;

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
        setUpInterestsRecyclerView();
    }

    private void setUpViews(){
        navigationView  = findViewById(R.id.navView);
        toolbar = findViewById(R.id.searchToolBar);
        drawer  = findViewById(R.id.drawer_layout);
        mainRv  = findViewById(R.id.recyclerview);
        keepTrackNum = findViewById(R.id.keepTrackNum);

        numberOfCheckItems = new ArrayList<>();
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
        }
    }

    private void setUpInterestsRecyclerView() {
        dummyInterests();
        mainRv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);
        mainRv.setLayoutManager(layoutManager);

        adapter = new InterestsAdapter(mArrayList, getApplicationContext(), this);
        mainRv.setAdapter(adapter);
        mainRv.setNestedScrollingEnabled(false);
    }

    private void dummyInterests(){
        mArrayList = new ArrayList<>();
        mArrayList.add(new InterestsModel("Business"));
        mArrayList.add(new InterestsModel("Mathematics"));
        mArrayList.add(new InterestsModel("Computer Science"));
        mArrayList.add(new InterestsModel("Programming"));
        mArrayList.add(new InterestsModel("Literature"));
        mArrayList.add(new InterestsModel("Chemistry"));
        mArrayList.add(new InterestsModel("Biology"));
        mArrayList.add(new InterestsModel("Geography"));
        mArrayList.add(new InterestsModel("Arts"));
        mArrayList.add(new InterestsModel("The Economy"));
        mArrayList.add(new InterestsModel("Physics"));
        mArrayList.add(new InterestsModel("Engineering"));
        mArrayList.add(new InterestsModel("Money"));
        mArrayList.add(new InterestsModel("Fashion"));
        mArrayList.add(new InterestsModel("Law"));
        mArrayList.add(new InterestsModel("Investing"));
        mArrayList.add(new InterestsModel("Stock Markets"));
        mArrayList.add(new InterestsModel("Philosophy"));
        mArrayList.add(new InterestsModel("Meditation"));
        mArrayList.add(new InterestsModel("Protests"));
        mArrayList.add(new InterestsModel("Civil Rights"));
        mArrayList.add(new InterestsModel("Politics"));
        mArrayList.add(new InterestsModel("Education"));
        mArrayList.add(new InterestsModel("College"));
        mArrayList.add(new InterestsModel("Sex"));
        mArrayList.add(new InterestsModel("High School"));
        mArrayList.add(new InterestsModel("Relationships"));
        mArrayList.add(new InterestsModel("Dating"));
        mArrayList.add(new InterestsModel("LGBTQ"));
        mArrayList.add(new InterestsModel("Music"));
        mArrayList.add(new InterestsModel("Medicine"));
        mArrayList.add(new InterestsModel("Writing"));
        mArrayList.add(new InterestsModel("Books"));
        mArrayList.add(new InterestsModel("History"));
        mArrayList.add(new InterestsModel("Start-ups"));
        mArrayList.add(new InterestsModel("Health"));
        mArrayList.add(new InterestsModel("Psychology"));
        mArrayList.add(new InterestsModel("Cooking"));
        mArrayList.add(new InterestsModel("Food"));
        mArrayList.add(new InterestsModel("Religion"));
        mArrayList.add(new InterestsModel("Military"));
        mArrayList.add(new InterestsModel("Ethics"));
        mArrayList.add(new InterestsModel("Sports"));
        mArrayList.add(new InterestsModel("Mental Health"));
        mArrayList.add(new InterestsModel("Family"));
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(2).setChecked(true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCheckedClick(int position, CompoundButton buttonView, boolean isChecked) {
        InterestsModel interests = mArrayList.get(position);

        // save initial button state (meaning was it check or unchecked before)
        boolean selected = false;
        if(interests.isSelected()) selected = true;

        // update button state
        interests.setSelected(isChecked);

        // if check button has not been selected before but it is checked NOW, then add to arrayList
        if(!selected && isChecked) {
            numberOfCheckItems.add(1);
            showSnackBar(buttonView, interests);
        }

        // if check button has been selected before but it is 'unchecked' NOW, then remove from the arrayList
        if(selected && !isChecked) numberOfCheckItems.remove(numberOfCheckItems.size() - 1);

        keepTrackNum.setText(numberOfCheckItems.size() + "/5");
    }

    private void showSnackBar(CompoundButton buttonView, InterestsModel interests){
        String interestName = interests.getSubject();

        Snackbar snackbar = Snackbar.make(buttonView, "Now Following " + interestName, Snackbar.LENGTH_SHORT);
        snackbar.setAction("Action", null);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor((Integer) getResources().getColor(R.color.mainColor));
        snackbar.show();
    }
}
