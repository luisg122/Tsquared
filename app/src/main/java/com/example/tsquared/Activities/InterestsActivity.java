package com.example.tsquared.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class InterestsActivity extends AppCompatActivity {
    private ArrayList<InterestsModel> mArrayList;
    private ArrayList<String> obtainedInterests;
    private RecyclerView mainRv;
    private InterestsAdapter adapter;
    private Toolbar toolbar;
    private TextView keepTrackNum;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interests_toolbar);
        setUpToolBar();
        setUpViews();
        setUpInterestsRecyclerView();
    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpViews(){
        keepTrackNum = (TextView) findViewById(R.id.keepTrackNum);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.finish_sign_up_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.finishButton);
        View view = menuItem.getActionView();
        Button finishSelecting = view.findViewById(R.id.finishSelecting);
        finishSelecting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNumSubjectsSelected();
                Intent intent = new Intent(InterestsActivity.this, DrawerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                ActivityCompat.finishAffinity(InterestsActivity.this);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setUpInterestsRecyclerView() {
        mArrayList = new ArrayList<>();
        mainRv     = findViewById(R.id.interestRV);
        mainRv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);
        mainRv.setLayoutManager(layoutManager);

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

        adapter = new InterestsAdapter(mArrayList, getApplicationContext());
        mainRv.setAdapter(adapter);
        mainRv.setNestedScrollingEnabled(false);
    }

    @SuppressLint("SetTextI18n")
    public void checkNumSubjectsSelected(){
        obtainedInterests = new ArrayList<>();
        InterestsModel interestsModel;
        int keepCount = 0;
        for(int i = 0; i < mArrayList.size(); i++){
            interestsModel = mArrayList.get(i);
            if(interestsModel.isSelected()){
                obtainedInterests.add(interestsModel.getSubject());
            }
        }

        for(int i = 0; i < obtainedInterests.size(); i++){
            Log.d("Subject" + "(" + (keepCount++) + "): ", obtainedInterests.get(i));
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}