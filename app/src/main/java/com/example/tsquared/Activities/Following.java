package com.example.tsquared.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsquared.Adapters.FollowersAdapter;
import com.example.tsquared.Models.FollowersModel;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

import java.util.ArrayList;

public class Following extends AppCompatActivity implements FollowersAdapter.ProfileClickListener{
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<FollowersModel> mArrayList;
    private FollowersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedBundleState){
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        super.onCreate(savedBundleState);
        setContentView(R.layout.list_following_followers);
        setViews();
        setUpToolBar();
        setUpRecyclerView();
    }

    public void setViews(){
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.followers_list);
    }

    public void setUpToolBar(){
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitle("Following");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setUpRecyclerView(){
        dummyData();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        adapter = new FollowersAdapter(mArrayList, getApplicationContext(), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setAdapter(adapter);
    }

    public void dummyData(){
        mArrayList = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            FollowersModel user = new FollowersModel("Jake Lin", "https://cdn.pixabay.com/photo/2014/07/09/10/04/man-388104_960_720.jpg", "12 followers", true);
            mArrayList.add(user);
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }

    @Override
    public void followButton(int position, View view) {
        FollowersModel followersModel = mArrayList.get(position);

        Button button = (Button) view;

        // if user is not followed, but button is clicked, then follow user
        if(!followersModel.getFollowing()){
            button.setText(R.string.Following);

            // user is now followed
            followersModel.setFollowing(true);
        }

        // if user was followed, but button is clicked, then unfollow user
        else if(followersModel.getFollowing()){
            button.setText(R.string.Follow);

            // user is now unfollowed
            followersModel.setFollowing(false);
        }


        adapter.notifyItemChanged(position, followersModel);
    }
}
