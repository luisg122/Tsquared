package com.example.tsquared.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsquared.Adapters.MoreInterests_Adapter;
import com.example.tsquared.Adapters.MoreNewsAdapter;
import com.example.tsquared.Models.MoreInterestsModel;
import com.example.tsquared.Models.MoreNewsModel;
import com.example.tsquared.R;

import java.util.ArrayList;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MoreInterestsActivity extends AppCompatActivity implements MoreInterests_Adapter.OnMoreInterestsListener{
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MoreInterests_Adapter moreInterestsAdapter;
    private ArrayList<MoreInterestsModel> mArrayList;
    private Handler handler;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interests_more_layout);
        setUpViews();
        initializeHandler();
        setUpToolbar();
        setUpRecyclerView();
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
    }

    private void setUpViews(){
        toolbar      = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.interestsRecyclerview);
    }

    private void setUpToolbar(){
        toolbar.setTitle("Your interests");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
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
        moreInterestsAdapter = new MoreInterests_Adapter(mArrayList, getApplication(), this);
        recyclerView.setAdapter(moreInterestsAdapter);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new
                DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        divider.setDrawable((Objects.requireNonNull(ContextCompat.getDrawable(Objects.requireNonNull((this).getApplicationContext()),
                R.drawable.line_divider_black))));

        recyclerView.addItemDecoration(divider);
    }

    private void dummyDataSetUp(){
        mArrayList = new ArrayList<>();
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Computer Science"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Computer Engineering"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Mathematics"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Real Estate"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Politics"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Climate"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Sports"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Business"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Education"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Mental Health"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Law"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Computer Science"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Computer Engineering"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Mathematics"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Real Estate"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Politics"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Climate"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Sports"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Business"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Education"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Mental Health"));
        mArrayList.add(new MoreInterestsModel(R.drawable.blank_profile, "Law"));
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onMoreInterestsClick() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), NewsArticleContainer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 150);
    }
}