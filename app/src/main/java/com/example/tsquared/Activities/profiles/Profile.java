package com.example.tsquared.Activities.profiles;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.tsquared.Activities.Followers;
import com.example.tsquared.Activities.Following;
import com.example.tsquared.Activities.Settings;
import com.example.tsquared.Adapters.ViewPagerAdapter;
import com.example.tsquared.Fragments.QuestionsFragment;
import com.example.tsquared.Fragments.UserAnswersFragment;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.example.tsquared.ViewPager.CustomViewPager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

public class Profile extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TextView following;
    private TextView followers;
    private Button button;
    private TextView collapsedTV;
    private ImageView profilePic;
    boolean appBarExpanded = true;


    @Override
    protected void onCreate(Bundle savedBundleState){
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        super.onCreate(savedBundleState);
        setContentView(R.layout.profile_page);
        setViews();
        setUpProfileImage();
        setUpToolBar();
        setButtonClicks();
        viewPagerInit();
        setUpdateProfileListener();
    }

    private void setViews() {
        toolbar      = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        following    = (TextView) findViewById(R.id.followingButton);
        followers    = (TextView) findViewById(R.id.followersButton);
        profilePic   = (ImageView) findViewById(R.id.profileImage);
        // collapsedTV  = (TextView) findViewById(R.id.collapsedText);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolBar);
        button = (Button) findViewById(R.id.button);

    }

    private void setUpProfileImage(){
        Glide.with(this)
                .load("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500")
                .into(profilePic);
    }

    private void setButtonClicks(){
        following.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Profile.this, Following.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
            }
        });
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Followers.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
            }
        });
    }

    private void setUpdateProfileListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
            }
        });
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    collapsingToolbar.setTitle(" ");
                    toolbar.setTitle(" ");
                    // collapsedTV.setVisibility(View.INVISIBLE);
                    scrollRange = appBarLayout.getTotalScrollRange();
                    appBarExpanded = false;
                }

                else if (scrollRange + verticalOffset == 0) {
                    // Collapsed Toolbar
                    collapsingToolbar.setTitle(" ");
                    collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
                    // collapsedTV.setVisibility(View.VISIBLE);
                    isShow = true;
                }

                else if (isShow) {
                    // Expanded Toolbar
                    collapsingToolbar.setTitle(" ");
                    toolbar.setTitle("");
                    // collapsedTV.setVisibility(View.INVISIBLE);
                    isShow = false;
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void viewPagerInit() {
        CustomViewPager viewPager = findViewById(R.id.mainViewPager1);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        viewPager.setCurrentItem(getIntent().getIntExtra("page", 0));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new QuestionsFragment(), "Questions");
        adapter.addFragment(new UserAnswersFragment(), "Answers");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}