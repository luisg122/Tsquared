package com.example.tsquared.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;

import com.example.tsquared.Adapters.UserArticlesAdapter;
import com.example.tsquared.Fragments.MoreOptionsArticles;
import com.example.tsquared.Models.UserArticleModel;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserBookMarks extends AppCompatActivity implements UserArticlesAdapter.OnMoreNewsListener {
    public DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Handler mDrawerActionHandler;
    private int saveClickCounter;

    private RecyclerView articlesRV;
    private UserArticlesAdapter articlesAdapter;
    private ArrayList<UserArticleModel> mArrayList;

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
        setContentView(R.layout.user_bookmarks_drawer);
        setUpViews();
        setUpDrawer();
        initializeHandler();
        setupDrawerContent(navigationView);
        setUpRecyclerView();
    }

    private void setUpViews(){
        navigationView  = findViewById(R.id.navView);
        toolbar         = findViewById(R.id.searchToolBar);
        drawer          = findViewById(R.id.drawer_layout);
        articlesRV      = findViewById(R.id.recyclerview);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpDrawer() {
        setSupportActionBar(toolbar);
        navigationView.getMenu().getItem(1).setChecked(true);
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
                Intent home = new Intent(UserBookMarks.this, DrawerActivity.class);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(home);
                finish();
                break;

            case R.id.interests:
                Intent interests = new Intent(UserBookMarks.this, UserInterests.class);
                startActivity(interests);
                break;
        }
    }

    private void setUpRecyclerView(){
        dummyDataSetUp();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        articlesAdapter = new UserArticlesAdapter(mArrayList, this);
        articlesRV.setLayoutManager(layoutManager);

        articlesRV.setAdapter(articlesAdapter);
    }

    private void dummyDataSetUp(){
        mArrayList = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            mArrayList.add(new UserArticleModel("https://www.cnn.com/2022/01/20/politics/biden-russia-putin-ukraine-incursion/index.html", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Washington Post"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    @Override
    public void onMoreNewsClick(int position) {
        UserArticleModel moreNewsModel = (UserArticleModel) mArrayList.get(position);
        Intent intent = new Intent(getApplicationContext(), NewsWebView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("articleURL", moreNewsModel.getURL());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
    }

    @Override
    public void onMoreIconClick(int position) {
        if(saveClickCounter++ == 0){
            MoreOptionsArticles bottomSheet = new MoreOptionsArticles();
            bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

            mDrawerActionHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveClickCounter = 0;
                }
            }, 1000);
        }
    }
}
