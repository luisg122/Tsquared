package com.example.tsquared.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

public class Settings extends AppCompatActivity {
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
        setContentView(R.layout.settings);
        setViews();
        setUpToolBar();

    }
    public void setViews(){
        toolbar = findViewById(R.id.toolbar);
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

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }
}
