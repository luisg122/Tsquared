package com.example.tsquared;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView mainRv;
    private ArrayList<AnswerModel> mArrayList;
    private QuestionViewModel question;
    private AnswerAdapter adapter;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    public  AlertDialog alertDialog;
    public  ImageView cancel;
    public  TextView  submit;

    private Menu menu;

    private boolean appBarExpanded = true;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapsingtoolbar);
        setUpToolBar();
        setUpRecyclerView();
        loadQuestionData();
        loadNormalList();

        adapter = new AnswerAdapter(mArrayList, this);
        mainRv.setAdapter(adapter);
    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.answerButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "Replace with Your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                openDialog();

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

    private void openDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View view2 = layoutInflaterAndroid.inflate(R.layout.answer_window, null);

        builder.setView(view2);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        Window window = alertDialog.getWindow();
        assert window != null;
        // Defining width, height for dialog window
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        submit   = (TextView) alertDialog.findViewById(R.id.submitAnswer);
        cancel   = (ImageView) alertDialog.findViewById(R.id.closeAnswerWindow);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    private QuestionViewModel loadQuestionData(){
        question = new QuestionViewModel("What is the idea behind scrum and how is it efficiently applied?");
        return question;
    }

    private ArrayList<AnswerModel> loadNormalList(){
        mArrayList = new ArrayList<>();
        mArrayList.add(new AnswerModel("Brian Doe", "Answered 12/6/2019", "Scrum is an agile process framework for managing complex knowledge work in Software Development", R.drawable.brian));
        mArrayList.add(new AnswerModel("Brian Doe", "Answered 12/6/2019", "Scrum is an agile process framework for managing complex knowledge work in Software Development", R.drawable.brian));
        mArrayList.add(new AnswerModel("Brian Doe", "Answered 12/6/2019", "Scrum is an agile process framework for managing complex knowledge work in Software Development", R.drawable.brian));
        mArrayList.add(new AnswerModel("Brian Doe", "Answered 12/6/2019", "Scrum is an agile process framework for managing complex knowledge work in Software Development", R.drawable.brian));
        mArrayList.add(new AnswerModel("Brian Doe", "Answered 12/6/2019", "Scrum is an agile process framework for managing complex knowledge work in Software Development", R.drawable.brian));
        mArrayList.add(new AnswerModel("Brian Doe", "Answered 12/6/2019", "Scrum is an agile process framework for managing complex knowledge work in Software Development", R.drawable.brian));
        mArrayList.add(new AnswerModel("Brian Doe", "Answered 12/6/2019", "Scrum is an agile process framework for managing complex knowledge work in Software Development", R.drawable.brian));
        mArrayList.add(new AnswerModel("Brian Doe", "Answered 12/6/2019", "Scrum is an agile process framework for managing complex knowledge work in Software Development", R.drawable.brian));

        return mArrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView() {
        mainRv = findViewById(R.id.answersRV);
        mainRv.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mainRv.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new
                DividerItemDecoration(mainRv.getContext(),
                DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(Objects.requireNonNull(this).getBaseContext(),
                R.drawable.line_divider)));
        mainRv.addItemDecoration(divider);
    }
}