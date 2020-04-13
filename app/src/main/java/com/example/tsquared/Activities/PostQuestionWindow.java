package com.example.tsquared.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.tsquared.Adapters.ViewPagerAdapter;
import com.example.tsquared.Fragments.PostQuestionFragment;
import com.example.tsquared.R;
import com.example.tsquared.ViewPager.CustomViewPager;
import com.google.android.material.tabs.TabLayout;

public class PostQuestionWindow extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        setUpToolBar();
        viewPagerInit();
    }

    private void setUpToolBar() {
        toolbar = findViewById(R.id.searchToolBar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitle("New question");
        setSupportActionBar(toolbar);
    }

    private void viewPagerInit() {
        CustomViewPager viewPager = findViewById(R.id.mainViewPager);
        setupViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setCurrentItem(getIntent().getIntExtra("page", 0));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PostQuestionFragment(), "Ask Question");
        adapter.addFragment(new Fragment(), "Share to Groups");
        viewPager.setAdapter(adapter);
    }
}
