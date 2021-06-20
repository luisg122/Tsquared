package com.example.tsquared.Activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.tsquared.Adapters.CommentsAdapter;
import com.example.tsquared.Models.CommentsModel;
import com.example.tsquared.R;
import com.example.tsquared.Utils.BlurBehind;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AnswerCommentsSection extends AppCompatActivity {
    private ArrayList<CommentsModel> comments;
    private CommentsAdapter adapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        blurBackground();

        setUpViews();
        setUpToolbar();
        setUpToolbarListener();
        setUpRecyclerView();
    }

    private void blurBackground() {
        BlurBehind.getInstance()
                .withAlpha(30)
                .withFilterColor(Color.parseColor("#282828")) //or Color.RED
                .setBackground(this);
    }

    private void setUpViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.commentsList);
    }

    private void setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitle("Comments");
        setSupportActionBar(toolbar);
    }

    private void setUpToolbarListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView(){
        dummyDataSetUp();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        adapter = new CommentsAdapter(comments, getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration divider = new
                DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(Objects.requireNonNull(this).getBaseContext(),
                R.drawable.line_divider)));

        recyclerView.addItemDecoration(divider);

    }

    private void dummyDataSetUp(){
        comments = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            comments.add(new CommentsModel("John Smith", "07/11/2021", "I agree with this answer, however I feel like it's too elementary"));
        }
    }
}
