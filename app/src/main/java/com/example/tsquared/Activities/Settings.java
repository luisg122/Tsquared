package com.example.tsquared.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.tsquared.Activities.login.LoginActivity;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.example.tsquared.Utils.PreferenceUtils;

public class Settings extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ScrollView scrollView;

    // Account Section Buttons
    private RelativeLayout profile_infoTab;
    private RelativeLayout account_settingTab;
    private RelativeLayout notifications_tab;
    private RelativeLayout signOutTab;

    // Support Section Buttons
    private RelativeLayout rateTab;
    private RelativeLayout suggestionTab;
    private RelativeLayout helpTab;

    // Legal Section Buttons
    private RelativeLayout termsAndServicesTab;
    private RelativeLayout privacyPolicyTab;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setUpViews();
        disableScrollViewTouchInterception();
        setUpToolBar();
        setUpListeners();
    }

    public void setUpViews(){
        toolbar = findViewById(R.id.toolbar);
        scrollView = (ScrollView) findViewById(R.id.settingsScrollView);

        // Account Section Buttons
        profile_infoTab = (RelativeLayout) findViewById(R.id.profile_infoTab);
        account_settingTab = (RelativeLayout) findViewById(R.id.account_settingTab);
        notifications_tab = (RelativeLayout) findViewById(R.id.notifications_tab);
        signOutTab = (RelativeLayout) findViewById(R.id.signOutTab);

        // Support Section Buttons
        rateTab = (RelativeLayout) findViewById(R.id.rateTab);
        suggestionTab = (RelativeLayout) findViewById(R.id.suggestionTab);
        helpTab = (RelativeLayout) findViewById(R.id.helpTab);

        // Legal Section Buttons
        termsAndServicesTab = (RelativeLayout) findViewById(R.id.termsAndServicesTab);
        privacyPolicyTab = (RelativeLayout) findViewById(R.id.privacyPolicyTab);
    }

    public void disableScrollViewTouchInterception(){
        scrollView.requestDisallowInterceptTouchEvent(true);
    }

    public void setUpToolBar(){
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setUpListeners(){
        // Account Section Buttons
        profile_infoTab.setOnClickListener(this);
        account_settingTab.setOnClickListener(this);
        notifications_tab.setOnClickListener(this);
        signOutTab.setOnClickListener(this);

        // Support Section Buttons
        rateTab.setOnClickListener(this);
        suggestionTab.setOnClickListener(this);
        helpTab.setOnClickListener(this);

        // Legal Section Buttons
        termsAndServicesTab.setOnClickListener(this);
        privacyPolicyTab.setOnClickListener(this);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Intent intent;
        int id = view.getId();

        if (id == R.id.profile_infoTab) {
            intent = new Intent(Settings.this, UserProfileInformation.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
        } else if (id == R.id.account_settingTab) {
            intent = new Intent(Settings.this, UserAccount.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
        } else if (id == R.id.signOutTab) {
            PreferenceUtils.savePassword(null, this);
            PreferenceUtils.saveEmail(null, this);
            PreferenceUtils.saveLastName(null, this);
            PreferenceUtils.saveFirstName(null, this);
            PreferenceUtils.saveCollege(null, this);

            DarkSharedPref.setNightModeState(false, getApplicationContext());
            DarkSharedPref.isDark = false;

            intent = new Intent(Settings.this, LoginActivity.class);
            startActivity(intent);
            ActivityCompat.finishAffinity(Settings.this);
        } else if(id == R.id.termsAndServicesTab) {
            intent = new Intent(Settings.this, GeneralWebView.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("url", "https://www.notion.so/Terms-Of-Service-f5b72aba42cb47e5b0d31431994ac40f");
            intent.putExtra("title", "Terms of Service");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
        } else if (id == R.id.privacyPolicyTab) {
            intent = new Intent(Settings.this, GeneralWebView.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("url", "https://www.notion.so/Privacy-Policy-8c2eedb2fec5422e95a5aa68ee1476fe");
            intent.putExtra("title", "Privacy Policy");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
        }
    }
}
