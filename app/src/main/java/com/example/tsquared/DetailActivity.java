package com.example.tsquared;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static java.security.AccessController.getContext;

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
    private SwipeRefreshLayout swipeContainer;
    private TextView loadQuestion;
    private TextView loadWindowQuestion;
    private SwitchCompat anonymous;
    private String toWhatQuestion;
    private String toWhatQuestion1;
    private String answerTextString;
    private EditText answerText;
    private String repliedByEmail;

    private Menu menu;

    private  boolean isAnon;

    RequestParams params;
    AsyncHttpClient client;
    String URL  = "http://207.237.59.117:8080/TSquared/platform?todo=showAnswers";
    String URL1 = "http://207.237.59.117:8080/TSquared/platform?todo=postAnswer";
    String ANONYMOUS = "Anonymous";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapsingtoolbar);
        setUpSwipeContainer();
        setUpRecyclerView();
        setUpToolBar();
        loadQuestionData();
        setUpSwipeListener();

        adapter = new AnswerAdapter(mArrayList, getApplicationContext());
        mainRv.setAdapter(adapter);

    }
    private void setUpSwipeContainer() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer3);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
    }

    private void setUpSwipeListener(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadListOfAnswers();
            }
        });
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
        View view = layoutInflaterAndroid.inflate(R.layout.answer_window, null);
        loadWindowQuestion(view);

        builder.setView(view);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        Window window = alertDialog.getWindow();
        assert window != null;
        // Defining width, height for dialog window
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        answerText = (EditText)     alertDialog.findViewById(R.id.answerToQuestion);
        submit     = (TextView)     alertDialog.findViewById(R.id.submitAnswer);
        cancel     = (ImageView)    alertDialog.findViewById(R.id.closeAnswerWindow);
        anonymous  = (SwitchCompat) alertDialog.findViewById(R.id.answerAnonymous);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                answerTextString = Objects.requireNonNull((answerText.getText().toString()));
                if(answerTextString.trim().isEmpty()){
                    Snackbar snackbar = Snackbar.make(submit, "Cannot submit an empty answer", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Action", null);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#94e5ff"));
                    snackbar.show();
                }
                else if (!answerTextString.trim().isEmpty()){
                    takeAnswerToPost();
                    alertDialog.dismiss();
                }
            }
        });
        anonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Toast.makeText(getApplicationContext(), "You are now answering anonymously", Toast.LENGTH_LONG).show();
                    isAnon = true;
                    Log.d("TAG", "onCheckedChanged: " + isAnon);
                }
                else if(!isChecked){
                    isAnon = false;
                    Log.d("TAG", "onCheckedChanged: " + isAnon);
                }
            }
        });

    }
    private void takeAnswerToPost(){
        repliedByEmail = DrawerActivity.getEmail();
        Log.e("Passing data to post ", repliedByEmail + " " + answerTextString + " to " + toWhatQuestion);
        params = new RequestParams();
        params.put("repliedBy", repliedByEmail);
        params.put("text", answerTextString);
        params.put("toWhatQuestion", toWhatQuestion);

        if(isAnon) params.put("isAnonymous", 1);
        else if (!isAnon) params.put("isAnonymous", 0);

        client = new AsyncHttpClient();
        client.post(URL1, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Toast.makeText(getApplicationContext(), "Submit Success " + response, Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Posting Answer by " + repliedByEmail);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "Failed to post the answer:" + repliedByEmail);

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }

    private void loadQuestionData(){
        Intent intent = getIntent();
        String question = intent.getStringExtra("question");
        toWhatQuestion1 = question;
        loadQuestion = findViewById(R.id.questionAnswerPage1);
        loadQuestion.setText(question);
        loadListOfAnswers();
    }

    private void loadWindowQuestion(View view){
        Intent intent = getIntent();
        String question = intent.getStringExtra("question");
        toWhatQuestion = question;
        loadWindowQuestion = view.findViewById(R.id.questionAnswerPage2);
        loadWindowQuestion.setText(question);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView() {
        mainRv = findViewById(R.id.answersRV);
        mainRv.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mainRv.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new
                DividerItemDecoration(mainRv.getContext(),
                DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(Objects.requireNonNull(this).getBaseContext(),
                R.drawable.line_divider)));
        mainRv.addItemDecoration(divider);
    }

    private void loadListOfAnswers(){
        mArrayList = new ArrayList<>();

        params = new RequestParams();
        params.put("q", toWhatQuestion1);
        client = new AsyncHttpClient();
        client.get(URL, params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //super.onSuccess(statusCode, headers, response);
                Log.e("STRING DATA: ", response.toString());
                try {
                    //Log.e("ELEMENT: ", response.getString("Question1"));

                    for (int i = 0; i < response.length(); i++){
                        JSONObject object = response.getJSONObject(i);
                        String name     = object.getString("RepliedBy");
                        String answer   = object.getString("Text");
                        String dateAns  = object.getString("DateReplied");
                        int isAnonymous = object.getInt("isAnonymous");
                        Log.d("DATA ANSWER: ", name + " " + answer + " " + dateAns + " " + isAnonymous);
                        Drawable image     = ContextCompat.getDrawable(Objects.requireNonNull(getApplication()), R.drawable.blank_profile);

                        name = capitalizeFirstCharOfEveryWordInString(name);
                        if(isAnonymous == 1) {
                            AnswerModel data = new AnswerModel(ANONYMOUS, dateAns, answer, image);
                            mArrayList.add(data);
                        }
                        else if (isAnonymous == 0){
                            AnswerModel data = new AnswerModel(name, dateAns, answer, image);
                            mArrayList.add(data);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new AnswerAdapter(mArrayList, getApplicationContext());
                adapter.notifyDataSetChanged();
                mainRv.setAdapter(adapter);
                swipeContainer.setRefreshing(false);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("TAG", "EMAIL: " + "dummy");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }

    private String capitalizeFirstCharOfEveryWordInString(String string){
        char[] ch = string.toCharArray();
        for(int i = 0; i < string.length(); i++) {
            // Find first char of a word
            // Make sure the character does not equal a space
            if (i == 0 && ch[i] != ' ' || ch[i] != ' ' && ch[i - 1] == ' ') {
                // If such character is lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {
                    // simply convert it into upper-case
                    // refer to the ASCII table to understand this line of code
                    ch[i] = (char) (ch[i] - 'a' + 'A');
                }
            }
            else if (ch[i] >= 'A' && ch[i] <= 'Z'){
                ch[i] = (char) (ch[i] + 'a' - 'A');
            }
        }
        return new String(ch);
    }
}