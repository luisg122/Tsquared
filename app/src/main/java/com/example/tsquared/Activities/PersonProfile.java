package com.example.tsquared.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tsquared.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import static java.util.Objects.requireNonNull;


public class PersonProfile extends AppCompatActivity implements View.OnClickListener{

    private TextView name;
    private TextView college;
    private TextView desc;
    private TextView email;
    private TextView follow;
    private TextView userQuestions;
    private TextView userAnswers;

    private ImageView questions;
    private ImageView answers;
    private ImageView following;
    private ImageView followers;

    private Toolbar toolbar;
    private String  firstName;

    static RequestParams params;
    static AsyncHttpClient client;
    static String URL = "http://207.237.59.117:8080/TSquared/platform?todo=addToFollowing";

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
        follow  = findViewById(R.id.tv_follow);
        userQuestions = findViewById(R.id.profilePersonQuestions);
        userAnswers   = findViewById(R.id.profilePersonAnswers);

        questions = findViewById(R.id.questionsArrow);
        answers   = findViewById(R.id.answersArrow);
        following = findViewById(R.id.followingArrow);
        followers = findViewById(R.id.followersArrow);
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

        follow.setOnClickListener(this);
        questions.setOnClickListener(this);
        answers.setOnClickListener(this);
        following.setOnClickListener(this);
        followers.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.tv_follow:
                addToFollowing(email.getText().toString());
                break;
            case R.id.questionsArrow:
                Bundle args = new Bundle();
                args.putString("email", email.getText().toString());
                UserQuestionsFragment newFragment = new UserQuestionsFragment();
                newFragment.setArguments(args);
                newFragment.show(getSupportFragmentManager(), "Dialog");
                break;
            case R.id.answersArrow:
                Bundle args1 = new Bundle();
                args1.putString("email", email.getText().toString());
                UserAnswersFragment newFragment1 = new UserAnswersFragment();
                newFragment1.setArguments(args1);
                newFragment1.show(getSupportFragmentManager(), "Dialog");
                break;
            case R.id.followingArrow:
                Bundle args2 = new Bundle();
                args2.putString("email", email.getText().toString());
                UserFollowingFragment newFragment2 = new UserFollowingFragment();
                newFragment2.setArguments(args2);
                newFragment2.show(getSupportFragmentManager(), "Dialog");
                break;
            case R.id.followersArrow:
                Bundle args3 = new Bundle();
                args3.putString("email", email.getText().toString());
                UserFollowersFragment newFragment3 = new UserFollowersFragment();
                newFragment3.setArguments(args3);
                newFragment3.show(getSupportFragmentManager(), "Dialog");
                break;
        }*/
    }

    /*public static void addToFollowing(String profileEmail){
        params = new RequestParams();
        params.put("currentUserEmail", DrawerActivity.getEmail());
        params.put("userToFollow", profileEmail);
        Log.e("DATA to Add Following", DrawerActivity.getEmail() +" follows "+ profileEmail);
        client = new AsyncHttpClient();

        client.get(URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Log.e("Adding Following", DrawerActivity.getEmail() +" follows another user");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "Failed to add user to follow");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }*/
}