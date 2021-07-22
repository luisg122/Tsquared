package com.example.tsquared.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsquared.Adapters.InterestsAdapter;
import com.example.tsquared.Models.InterestsModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class InterestsActivity extends AppCompatActivity implements InterestsAdapter.OnCheckClickListener{
    private ArrayList<InterestsModel> mArrayList;
    private ArrayList<String> obtainedInterests;
    private RecyclerView mainRv;
    private InterestsAdapter adapter;
    private Toolbar toolbar;
    private Handler handler;
    private TextView keepTrackNum;
    private int counter = 0;
    private int lastPosition = -1;
    private ArrayList<Integer> numberOfCheckItems;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interests_toolbar);
        setUpViews();
        setUpToolBar();
        setUpInterestsRecyclerView();
    }

    private void setUpViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        keepTrackNum = (TextView) findViewById(R.id.keepTrackNum);
        numberOfCheckItems = new ArrayList<>();
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.finish_sign_up_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.finishButton);
        View view = menuItem.getActionView();

        handler = new Handler(Looper.getMainLooper());
        Button finishSelecting = view.findViewById(R.id.finishSelecting);
        finishSelecting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(InterestsActivity.this, DrawerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        ActivityCompat.finishAffinity(InterestsActivity.this);
                    }
                }, 150);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setUpInterestsRecyclerView() {
        dummyInterests();
        mainRv     = findViewById(R.id.interestRV);
        mainRv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);
        mainRv.setLayoutManager(layoutManager);

        adapter = new InterestsAdapter(mArrayList, getApplicationContext(), this);
        mainRv.setAdapter(adapter);
        mainRv.setNestedScrollingEnabled(false);
    }

    private void dummyInterests(){
        mArrayList = new ArrayList<>();
        mArrayList.add(new InterestsModel("Business"));
        mArrayList.add(new InterestsModel("Mathematics"));
        mArrayList.add(new InterestsModel("Computer Science"));
        mArrayList.add(new InterestsModel("Programming"));
        mArrayList.add(new InterestsModel("Literature"));
        mArrayList.add(new InterestsModel("Chemistry"));
        mArrayList.add(new InterestsModel("Biology"));
        mArrayList.add(new InterestsModel("Geography"));
        mArrayList.add(new InterestsModel("Arts"));
        mArrayList.add(new InterestsModel("The Economy"));
        mArrayList.add(new InterestsModel("Physics"));
        mArrayList.add(new InterestsModel("Engineering"));
        mArrayList.add(new InterestsModel("Money"));
        mArrayList.add(new InterestsModel("Fashion"));
        mArrayList.add(new InterestsModel("Law"));
        mArrayList.add(new InterestsModel("Investing"));
        mArrayList.add(new InterestsModel("Stock Markets"));
        mArrayList.add(new InterestsModel("Philosophy"));
        mArrayList.add(new InterestsModel("Meditation"));
        mArrayList.add(new InterestsModel("Protests"));
        mArrayList.add(new InterestsModel("Civil Rights"));
        mArrayList.add(new InterestsModel("Politics"));
        mArrayList.add(new InterestsModel("Education"));
        mArrayList.add(new InterestsModel("College"));
        mArrayList.add(new InterestsModel("Sex"));
        mArrayList.add(new InterestsModel("High School"));
        mArrayList.add(new InterestsModel("Relationships"));
        mArrayList.add(new InterestsModel("Dating"));
        mArrayList.add(new InterestsModel("LGBTQ"));
        mArrayList.add(new InterestsModel("Music"));
        mArrayList.add(new InterestsModel("Medicine"));
        mArrayList.add(new InterestsModel("Writing"));
        mArrayList.add(new InterestsModel("Books"));
        mArrayList.add(new InterestsModel("History"));
        mArrayList.add(new InterestsModel("Start-ups"));
        mArrayList.add(new InterestsModel("Health"));
        mArrayList.add(new InterestsModel("Psychology"));
        mArrayList.add(new InterestsModel("Cooking"));
        mArrayList.add(new InterestsModel("Food"));
        mArrayList.add(new InterestsModel("Religion"));
        mArrayList.add(new InterestsModel("Military"));
        mArrayList.add(new InterestsModel("Ethics"));
        mArrayList.add(new InterestsModel("Sports"));
        mArrayList.add(new InterestsModel("Mental Health"));
        mArrayList.add(new InterestsModel("Family"));
    }

    @Override
    public void finish(){
        super.finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCheckedClick(int position, CompoundButton buttonView, boolean isChecked) {
        InterestsModel interests = mArrayList.get(position);
        interests.setSelected(isChecked);

        // if check button has not been checked before but it is checked NOW, then add to arrayList
        if(isChecked && !interests.isSelected()) numberOfCheckItems.add(1);

        // if check button has been selected before but it is 'unchecked' NOW, then remove from the arrayList
        if(!isChecked && interests.isSelected()) numberOfCheckItems.remove(numberOfCheckItems.size() - 1);

        keepTrackNum.setText(numberOfCheckItems.size() + "/5");
    }
}