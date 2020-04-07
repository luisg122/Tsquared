package com.example.tsquared.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.tsquared.R;

public class DomainCategoryActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.domain_category);
        setUpToolBar();
    }

    private void setUpToolBar(){
        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("category");
        toolbar = findViewById(R.id.categoryToolbar);
        toolbar.setTitle(categoryName);
        //toolbar.setTitleTextColor();




        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
