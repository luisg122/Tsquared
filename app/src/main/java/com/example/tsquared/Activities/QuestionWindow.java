package com.example.tsquared.Activities;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.android.volley.RequestQueue;
import com.example.tsquared.R;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class QuestionWindow extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private Button post;
    private ImageView cancel;
    private SwitchCompat anonymous;
    private RequestQueue mRequestQueue;

    private TextView postName;
    private TextView postCollege;
    private EditText topic;
    private EditText topicPost;

    private String topicString;
    private String topicPostString;
    private String postedByString;
    private String collegeString;
    private boolean isAnon;

    private AlertDialog alertDialog;
    RequestParams params, params1;
    AsyncHttpClient client, client1;
    String URL1 = "http://207.237.59.117:8080/TSquared/platform?todo=postQuestion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_post);
        setViews();
        loadFullNameAndCollege();
        setListeners();
    }

    private void setViews() {
        postName    = (TextView)     findViewById(R.id.postName);
        postCollege = (TextView)     findViewById(R.id.postCollegeName);
        topic       = (EditText)     findViewById(R.id.topic);
        topicPost   = (EditText)     findViewById(R.id.topicPost);
        post        = (Button)       findViewById(R.id.submitPostButton);
        cancel      = (ImageView)    findViewById(R.id.cancelSubmitButton);
        anonymous   = (SwitchCompat) findViewById(R.id.questionAnonymous);
    }

    private void setListeners(){
        post.setOnClickListener(this);
        cancel.setOnClickListener(this);
        anonymous.setOnCheckedChangeListener(this);
    }

    private void loadFullNameAndCollege(){
        String fullName = getIntent().getStringExtra("Full Name");
        String college  = getIntent().getStringExtra("College");
        postName.setText(fullName);
        postCollege.setText(college);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.submitPostButton:
                if(!getInfo()){
                    Snackbar snackbar = Snackbar.make(post, "Cannot leave fields empty", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Action", null);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#94e5ff"));
                    snackbar.show();
                }
                else if (getInfo()){
                    takeDataToPost();
                    QuestionWindow.this.finish();
                }
                break;
            case R.id.cancelSubmitButton:
                QuestionWindow.this.finish();
                break;
            case R.id.questionAnonymous:
                break;

        }
    }

    // not good programming practice to override the back-button
    @Override
    public void onBackPressed() {
        return;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean getInfo() {
        postedByString  = Objects.requireNonNull(postName.getText().toString());
        collegeString   = Objects.requireNonNull(postCollege.getText().toString());
        topicString     = Objects.requireNonNull(topic.getText().toString());
        topicPostString = Objects.requireNonNull(topicPost.getText().toString());

        if(topicString.trim().isEmpty() || topicPostString.trim().isEmpty()){
            return false;
        }
        return true;
    }

    private void takeDataToPost(){

        params = new RequestParams();
        params.put("postedBy", postedByString);
        params.put("content", topicPostString);
        params.put("topic", topicString);

        if(isAnon) params.put("isAnonymous", 1);
        else if(!isAnon) params.put("isAnonymous", 0);

        client = new AsyncHttpClient();

        client.post(URL1, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Toast.makeText(getApplicationContext(), "Submit Success " + response, Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Posting Question by " + "Someone");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "Failed to post the question:");

                //Snackbar.make(post, "Something Went Wrong", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //Toast.makeText(getApplicationContext(), "Something Went Wrong ", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
                //Snackbar.make(post, "Something Went Wrong", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            Toast.makeText(getApplicationContext(), "Your question is being asked anonymously", Toast.LENGTH_LONG).show();
            isAnon = true;
            Log.d("TAG", "onCheckedChanged: " + isAnon);
        }
        else if(!isChecked){
            isAnon = false;
            Log.d("TAG", "onCheckedChanged: " + isAnon);
        }
    }
}