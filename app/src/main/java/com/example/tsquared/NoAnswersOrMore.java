package com.example.tsquared;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoAnswersOrMore extends AppCompatActivity {

    private TextView loadQuestion;
    private String toWhatQuestion1;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapsingtoolbar1);
        setUpToolBar();
        loadQuestionData();
    }

    private void loadQuestionData(){
        Intent intent = getIntent();
        String question = intent.getStringExtra("question");
        toWhatQuestion1 = question;
        loadQuestion = findViewById(R.id.questionAnswerPage1);
        loadQuestion.setText(question);
    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.answerButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "Replace with Your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //openDialog();

            }
        });

        collapsingToolbar = findViewById(R.id.collapsingToolBar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Collapsed);
        collapsingToolbar.setExpandedTitleColor(R.style.TextAppearance_MyApp_Title_Expanded);
        collapsingToolbar.setTitle("");
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    collapsingToolbar.setTitle(" ");
                    toolbar.setTitle("");
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    // When Toolbar collapses
                    collapsingToolbar.setTitle(" ");
                    toolbar.setTitle("");
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
                    isShow = true;
                    //showOption(R.id.action_info);
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    toolbar.setTitle("");
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
                    isShow = false;
                    //hideOption(R.id.action_info);
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
}