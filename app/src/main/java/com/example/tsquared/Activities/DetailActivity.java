package com.example.tsquared.Activities;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tsquared.Adapters.AnswerAdapter;
import com.example.tsquared.Models.AnswerModel;
import com.example.tsquared.R;
import com.facebook.shimmer.ShimmerFrameLayout;
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

public class DetailActivity extends AppCompatActivity {
    private RecyclerView mainRv;
    private ArrayList<AnswerModel> mArrayList;
    private AnswerAdapter adapter;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeContainer;
    private TextView loadQuestion;
    private TextView loadWindowQuestion;
    private SwitchCompat anonymous;
    private String toWhatQuestion;
    private String toWhatQuestion1;
    private String answerTextString;
    private EditText answerText;
    private String    repliedByEmail;
    private TextView  promptCreateQuestion;
    private ImageView promptCreateImage;
    private ViewStub  stub;
    private ShimmerFrameLayout shimmerFrameLayout;
    private View view;

    private boolean isAnon;
    public  AlertDialog alertDialog;
    public  ImageView cancel;
    public  TextView  submit;

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
        setViews();
        setUpSwipeContainer();
        setLayout();
        setUpToolBar();
    }

    private void setViews(){
        stub = (ViewStub) findViewById(R.id.layout_stub);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer3);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        fab = (FloatingActionButton) findViewById(R.id.answerButton);
        collapsingToolbar = findViewById(R.id.collapsingToolBar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        loadQuestion    = findViewById(R.id.questionAnswerPage1);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setLayout(){
        stub.setLayoutResource(R.layout.activity_detail);
        stub.inflate();
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container1);
        setUpRecyclerView();
        loadQuestionData();
        loadListOfAnswers();
        setUpSwipeListener();
    }

    private void setUpSwipeListener(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadListOfAnswers();
            }
        });
    }

    private void setUpSwipeContainer() {
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "Replace with Your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                openDialog();
            }
        });
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.TextAppearance_MyApp_Title_Collapsed);
        collapsingToolbar.setExpandedTitleColor(R.style.TextAppearance_MyApp_Title_Expanded);
        collapsingToolbar.setTitle("");
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
        Intent intent   = getIntent();
        String question = intent.getStringExtra("question");
        toWhatQuestion1 = question;
        loadQuestion.setText(question);
    }

    private void loadWindowQuestion(View view){
        Intent intent      = getIntent();
        String question    = intent.getStringExtra("question");
        toWhatQuestion     = question;
        loadWindowQuestion = view.findViewById(R.id.questionAnswerPage2);
        loadWindowQuestion.setText(question);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView() {
        mArrayList = new ArrayList<>();
        mainRv = (RecyclerView) findViewById(R.id.answersRV);
        mainRv.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new AnswerAdapter(mArrayList, getApplicationContext());
        mainRv.setLayoutManager(layoutManager);
    }

    private void loadListOfAnswers(){
        params = new RequestParams();
        params.put("q", toWhatQuestion1);
        client = new AsyncHttpClient();
        client.get(URL, params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //super.onSuccess(statusCode, headers, response);
                Log.e("STRING DATA: ", response.toString());
                ArrayList<AnswerModel> answerList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++){
                    try {
                        //Log.e("ELEMENT: ", response.getString("Question1"));
                        JSONObject object  = response.getJSONObject(i);
                        AnswerModel answer = AnswerModel.fromJson(object);
                        Drawable image     = ContextCompat.getDrawable(Objects.requireNonNull(getApplication()), R.drawable.blank_profile);
                        answer.setProfileImage(image);
                        answerList.add(answer);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                adapter.swapData(answerList);
                mainRv.setAdapter(adapter);
                swipeContainer.setRefreshing(false);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "EMAIL: " + "dummy");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause(){
        super.onPause();
        shimmerFrameLayout.stopShimmer();
    }
}
        /*else if(responseNum.equals("0 Answers")) {
            stub.setLayoutResource(R.layout.no_answers_yet);
            stub.inflate();
            String name = DrawerActivity.getFirstName();
            String promptUser    = "Hi " + name + "! Be the first to answer this question";
            promptCreateQuestion = findViewById(R.id.promptCreateFirstAnswer);
            promptCreateImage    = findViewById(R.id.createFirstAnswer);
            shimmerFrameLayout   = findViewById(R.id.shimmer_view_container1);
            promptCreateQuestion.setText(promptUser);
            setUpRecyclerView();
            loadQuestionData();
            loadListOfAnswers();
            setUpSwipeListener1();
        }*/