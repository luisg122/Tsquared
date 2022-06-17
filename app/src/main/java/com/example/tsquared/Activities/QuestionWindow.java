package com.example.tsquared.Activities;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.tsquared.Adapters.QuestionItemAdapter;
import com.example.tsquared.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class QuestionWindow extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private Button post;
    private ImageView cancel;
    private RequestQueue mRequestQueue;

    private TextView postCollege;
    private EditText topic;
    private EditText topicPost;

    private String postName;
    private String topicPostString;
    private boolean isAnon;

    private AlertDialog alertDialog;
    RequestParams params, params1;
    AsyncHttpClient client, client1;
    String URL1 = "http://207.237.59.117:8080/TSquared/platform?todo=postQuestion";
    QuestionItemAdapter adapter;
    RecyclerView        mainRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_window);
        setViews();
        loadFullNameAndCollege();
        setListeners();
    }

    private void setViews() {
        topicPost   = (EditText)  findViewById(R.id.topicPost);
    }

    private void setListeners(){
        cancel.setOnClickListener(this);
    }

    private void loadFullNameAndCollege(){
        postName = getIntent().getStringExtra("Full Name");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            /*case R.id.submitPostButton:
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
                break;*/
            /*case R.id.cancelSubmitButton:
                QuestionWindow.this.finish();
                break;*/
        }
    }

    // not good programming practice to override the back-button
    @Override
    public void onBackPressed() {
        return;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean getInfo() {
        topicPostString = Objects.requireNonNull(topicPost.getText().toString());

        if(topicPostString.trim().isEmpty()){
            return false;
        }
        return true;
    }

    private void takeDataToPost(){

        params = new RequestParams();
        params.put("postedBy", postName);
        params.put("content", topicPostString);

        if(isAnon) params.put("isAnonymous", 1);
        else params.put("isAnonymous", 0);

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