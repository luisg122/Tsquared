package com.example.tsquared.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.tsquared.Adapters.MoreNewsAdapter;
import com.example.tsquared.Models.MoreNewsModel;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

import java.util.ArrayList;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MoreNewsActivity extends AppCompatActivity implements MoreNewsAdapter.OnMoreNewsListener{
    private Toolbar toolbar;
    private RecyclerView rv;
    private MoreNewsAdapter moreNewsAdapter;
    private ArrayList<MoreNewsModel> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_more_layout);
        setUpViews();
        setUpToolbar();
        setUpRecyclerView();
    }

    private void setUpViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void setUpToolbar(){
        toolbar.setTitle("News");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpRecyclerView(){
        dummyDataSetUp();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        moreNewsAdapter = new MoreNewsAdapter(mArrayList, getApplicationContext(), this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(moreNewsAdapter);
    }

    private void dummyDataSetUp(){
        mArrayList = new ArrayList<>();
        mArrayList.add(new MoreNewsModel(" ", " ", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Washington Post"));
        mArrayList.add(new MoreNewsModel(" ", " ", "The Dow Jones trends negative after opening with small gains", "New York Times"));
        mArrayList.add(new MoreNewsModel(" ", " ", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Axios"));
        mArrayList.add(new MoreNewsModel(" ", " ", "The Dow Jones trends negative after opening with small gains", "Washington Post"));
        mArrayList.add(new MoreNewsModel(" ", " ", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "New York Times"));
        mArrayList.add(new MoreNewsModel(" ", " ", "The Dow Jones trends negative after opening with small gains", "Axios"));
        mArrayList.add(new MoreNewsModel(" ", " ", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Washington Post"));
        mArrayList.add(new MoreNewsModel(" ", " ", "The Dow Jones trends negative after opening with small gains", "New York Times"));
        mArrayList.add(new MoreNewsModel(" ", " ", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Axios"));
        mArrayList.add(new MoreNewsModel(" ", " ", "The Dow Jones trends negative after opening with small gains", "Washington Post"));
        mArrayList.add(new MoreNewsModel(" ", " ", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "New York Times"));
        mArrayList.add(new MoreNewsModel(" ", " ", "The Dow Jones trends negative after opening with small gains", "Axios"));
        mArrayList.add(new MoreNewsModel(" ", " ", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Washington Post"));
        mArrayList.add(new MoreNewsModel(" ", " ", "The Dow Jones trends negative after opening with small gains", "New York Times"));
        mArrayList.add(new MoreNewsModel(" ", " ", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Axios"));
        mArrayList.add(new MoreNewsModel(" ", " ", "The Dow Jones trends negative after opening with small gains", "New York Times"));
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onMoreNewsClick(int position) {
        Intent intent = new Intent(getApplicationContext(), NewsWebView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("articleURL", mArrayList.get(position).getURL());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
    }
}