package com.example.tsquared.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserProfileInformation extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_profile_information);
        setUpViews();
        setUpToolBar();
    }

    private void setUpViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void setUpToolBar(){
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitle("Profile");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }
}
