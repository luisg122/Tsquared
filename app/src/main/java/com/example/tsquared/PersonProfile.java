package com.example.tsquared;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class PersonProfile extends AppCompatActivity {

    private TextView name;
    private TextView college;
    private TextView desc;
    private TextView userQuestions;
    private TextView userAnswers;
    private TextView email;

    private Toolbar toolbar;
    private String firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        setViews();
        loadViews();
        setUpToolBar();
    }

    private void setViews(){
        name    = findViewById(R.id.profilePersonName);
        college = findViewById(R.id.profilePersonCollege);
        desc    = findViewById(R.id.profileDescription);
        email   = findViewById(R.id.profileEmail);
        userQuestions = findViewById(R.id.profilePersonQuestions);
        userAnswers   = findViewById(R.id.profilePersonAnswers);
    }

    private void loadViews(){
        Intent intent = getIntent();
        String profileName = intent.getStringExtra("name");
        // Extract the First Name of the user
        firstName = profileName.substring(0, profileName.indexOf(' '));

        // User is studying/ has studied at what institution
        String profileCollege   = intent.getStringExtra("college");
        String studyingCollege  = "Studying at " + profileCollege;

        String profileDesc = intent.getStringExtra("desc");
        String profileQuestions = firstName + "'s Questions";
        String profileAnswers   = firstName + "'s Answers";

        String profileEmail = intent.getStringExtra("email");

        name.setText(profileName);
        college.setText(studyingCollege);
        desc.setText(profileDesc);
        userQuestions.setText(profileQuestions);
        userAnswers.setText(profileAnswers);
        email.setText(profileEmail);
    }

    private void setUpToolBar(){
        toolbar = findViewById(R.id.personProfileToolbar);
        toolbar.setTitle(firstName);
        //toolbar.setTitleTextColor();
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}