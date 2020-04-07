package com.example.tsquared.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tsquared.R;

public class SignUp_Screen5 extends AppCompatActivity implements View.OnClickListener {
    private Button finishButton;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup5);
        setButtonViewAndListener();
        setUpToolBar();
    }

    private void setUpToolBar() {
        toolbar = findViewById(R.id.toolbarSignUp);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("Profile");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setButtonViewAndListener() {
        finishButton = findViewById(R.id.signUpButton5);
        finishButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
