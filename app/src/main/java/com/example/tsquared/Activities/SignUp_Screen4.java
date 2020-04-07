package com.example.tsquared.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tsquared.R;

public class SignUp_Screen4 extends AppCompatActivity implements View.OnClickListener{
    private Button nextButton;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup4);
        setButtonViewAndListener();
        setUpToolBar();

    }

    private void setUpToolBar() {
        toolbar = findViewById(R.id.toolbarSignUp);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("Birthday");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setButtonViewAndListener() {
        nextButton = findViewById(R.id.signUpButton4);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent register = new Intent(SignUp_Screen4.this, SignUp_Screen5.class);
        register.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(register);
        // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        // finish();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
