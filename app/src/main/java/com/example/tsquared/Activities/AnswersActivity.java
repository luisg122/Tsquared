package com.example.tsquared.Activities;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.Adapters.AnswerAdapter;
import com.example.tsquared.ExpandableTextView;
import com.example.tsquared.Models.AnswerModel;
import com.example.tsquared.Models.AnswerWithImages;
import com.example.tsquared.R;
import com.example.tsquared.ResizeText;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class AnswersActivity extends AppCompatActivity implements AnswerAdapter.OnCommentsClickListener {
    private RecyclerView mainRv;
    private ArrayList<Object> mArrayList;
    private AnswerAdapter adapter;
    private Toolbar toolbar;
    private TextView collapsedText;
    private ExtendedFloatingActionButton fab;
    private TextView loadWindowQuestion;
    private boolean hasScrolled;
    private int currentPos;

    private SwipeRefreshLayout swipeContainer;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private SwitchCompat anonymous;
    private String toWhatQuestion;
    private String toWhatQuestion1;
    private String answerTextString;
    private EditText answerText;
    private String repliedByEmail;
    private TextView promptCreateQuestion;
    private ImageView promptCreateImage;
    //private ViewStub stub;
    private ShimmerFrameLayout shimmerFrameLayout;
    private View view;

    private boolean isAnon;
    public AlertDialog alertDialog;
    public ImageView cancel;
    public TextView submit;

    RequestParams params;
    AsyncHttpClient client;
    String URL  = "http://207.237.59.117:8080/TSquared/platform?todo=showAnswers";
    String URL1 = "http://207.237.59.117:8080/TSquared/platform?todo=postAnswer";
    String ANONYMOUS = "Anonymous";
    boolean appBarExpanded = true;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answers_activity_layout);
        setViews();
        getQuestion();
        // setUpSwipeContainer();
        // setLayout();
        setUpToolBar();
        initFloatingActionButton();
        setUpRecyclerView();
    }

    private void setViews() {
        // stub = (ViewStub) findViewById(R.id.layout_stub);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer3);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        fab = (ExtendedFloatingActionButton) findViewById(R.id.answerButton);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mainRv = (RecyclerView) findViewById(R.id.answersRV);
        collapsedText = (TextView) findViewById(R.id.collapsedText);
    }

    private void getQuestion(){
        toWhatQuestion = getIntent().getStringExtra("question");
    }

    /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setLayout() {
        stub.setLayoutResource(R.layout.activity_detail);
        stub.inflate();
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container1);
        setUpRecyclerView();
        loadQuestionData();
        //loadListOfAnswers();
        //setUpSwipeListener();
    }*/

    /*private void setUpSwipeListener() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //loadListOfAnswers();
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
    }*/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView() {
        dummyData();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        adapter = new AnswerAdapter(mArrayList, getApplicationContext(), this, getIntent());
        mainRv.setLayoutManager(layoutManager);
        mainRv.setHasFixedSize(false);
        mainRv.setItemViewCacheSize(20);
        mainRv.setAdapter(adapter);
        // design to expand and shrink the extended fab button

        currentPos  = 0;
        hasScrolled = false;
        mainRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentPos += dy;
                if(currentPos == 0 && !hasScrolled){
                    collapsedText.setVisibility(INVISIBLE);
                }

                if (dy > 0) {
                    // Scrolled Downwards
                    hasScrolled = true;
                    fab.shrink();
                    collapsedText.setVisibility(VISIBLE);
                }

                else if (dy < 0) {
                    // Scrolled Upwards
                    hasScrolled = true;
                    fab.extend();
                    collapsedText.setVisibility(VISIBLE);
                }
            }
        });
    }

    private void dummyData(){
        mArrayList  = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            AnswerModel answerItem = new AnswerModel("John Doe", "polynomial is an expression consisting of variables and coefficients, that involves only the operations of addition, " +
                    "subtraction, multiplication, and non-negative integer exponentiation of variables."
                    , "September 09 2020");
            mArrayList.add(answerItem);

            String[] imageUrls = {
                    "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg",
                    "https://www.reuters.com/resizer/vsRFGKTi9vWULwxZlnZkhbTmaLs=/1200x628/smart/filters:quality(80)/cloudfront-us-east-2.images.arcpublishing.com/reuters/HKYMFSORGZJRHMWZ6YYOZO4MTY.jpg",
                    "https://insights.som.yale.edu/sites/default/files/styles/rectangle_xs/public/insights/background/What%20the%20Plunge%20in%20the%20Stock%20Market%20Means%20for%20Individual%20Investors.jpg?h=d0d46503&itok=vl15_CSn0"
            };

            AnswerWithImages answerItem1 = new AnswerWithImages("John Doe", "polynomial is an expression consisting of variables and coefficients, that involves only the operations of addition, " +
                    "subtraction, multiplication, and non-negative integer exponentiation of variables. However, there's a bit more to this simplistic definition, in which I " +
                    "suggest you'd further build your very own definition of what polynomials are. polynomial is an expression consisting of variables and coefficients, that involves only the operations of addition, " +
                    "subtraction, multiplication, and non-negative integer exponentiation of variables. However, there's a bit more to this simplistic definition, in which I " +
                    "suggest you'd further build your very own definition of what polynomials are. polynomial is an expression consisting of variables and coefficients, that involves only the operations of addition."
                    , "September 09 2020", imageUrls, 3);

            mArrayList.add(answerItem1);
        }
    }

    public void initFloatingActionButton(){
        // Expanded ToolBar Floating Action Button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnswersActivity.this, AnswerWindow.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
            }
        });
    }

    private void setUpToolBar() {
        //setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /*private void openDialog() {
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

        answerText = (EditText) alertDialog.findViewById(R.id.answerToQuestion);
        submit = (TextView) alertDialog.findViewById(R.id.submitAnswer);
        cancel = (ImageView) alertDialog.findViewById(R.id.closeAnswerWindow);
        anonymous = (SwitchCompat) alertDialog.findViewById(R.id.answerAnonymous);

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
                if (answerTextString.trim().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(submit, "Cannot submit an empty answer", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Action", null);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#94e5ff"));
                    snackbar.show();
                } else if (!answerTextString.trim().isEmpty()) {
                    takeAnswerToPost();
                    alertDialog.dismiss();
                }
            }
        });

        anonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "You are now answering anonymously", Toast.LENGTH_LONG).show();
                    isAnon = true;
                    Log.d("TAG", "onCheckedChanged: " + isAnon);
                } else if (!isChecked) {
                    isAnon = false;
                    Log.d("TAG", "onCheckedChanged: " + isAnon);
                }
            }
        });
    }*/

    /*private void takeAnswerToPost() {
        repliedByEmail = DrawerActivity.getEmail();
        Log.e("Passing data to post ", repliedByEmail + " " + answerTextString + " to " + toWhatQuestion);
        params = new RequestParams();
        params.put("repliedBy", repliedByEmail);
        params.put("text", answerTextString);
        params.put("toWhatQuestion", toWhatQuestion);
        if (isAnon) params.put("isAnonymous", 1);
        else if (!isAnon) params.put("isAnonymous", 0);

        client = new AsyncHttpClient();
        client.post(URL1, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
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
    }*/

    private void loadWindowQuestion(View view) {
        Intent intent = getIntent();
        String question = intent.getStringExtra("question");
        toWhatQuestion = question;
        loadWindowQuestion = view.findViewById(R.id.questionAnswerPage2);
        loadWindowQuestion.setText(question);
    }

    /*private void loadListOfAnswers() {
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
                for (int i = 0; i < response.length(); i++) {
                    try {
                        //Log.e("ELEMENT: ", response.getString("Question1"));
                        JSONObject object = response.getJSONObject(i);
                        AnswerModel answer = AnswerModel.fromJson(object);
                        Drawable image = ContextCompat.getDrawable(Objects.requireNonNull(getApplication()), R.drawable.blank_profile);
                        answer.setProfileImage(image);
                        answerList.add(answer);
                    } catch (JSONException e) {
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
    }*/

    @Override
    public void onResume() {
        super.onResume();
        fab.show();
        //shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        fab.hide();
        //shimmerFrameLayout.stopShimmer();
    }

    @Override
    public void finish(){
        super.finish();
        fab.hide();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void OnCommentsClick(int position) {
        Intent intent = new Intent(AnswersActivity.this, AnswerCommentsSection.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
    }

    @Override
    public void upVote(int position, View view) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);
    }

    @Override
    public void downVote(int position, View view) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);
    }

    @Override
    public void expandText(int position, View view, ExpandableTextView expandableTextView, TextView textView, ImageView imageView) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);

        Object v = (Object) mArrayList.get(position);
        if(v instanceof AnswerModel){
            AnswerModel answerItem = (AnswerModel) v;

            // text is collapsed and not expanded
            if(!answerItem.isTextExpanded()){
                expandableTextView.expand();
                answerItem.setTextExpanded(true);
            }

            // text is expanded and not collapsed
            else if(answerItem.isTextExpanded()){
                expandableTextView.collapse();
                answerItem.setTextExpanded(false);
            }

            adapter.notifyItemChanged(position, answerItem);
        }

        else if(v instanceof AnswerWithImages) {
            AnswerWithImages answerItem = (AnswerWithImages) v;

            // text is collapsed and not expanded
            if(!answerItem.isTextExpanded()){
                expandableTextView.expand();
                answerItem.setTextExpanded(true);
            }

            // text is expanded and not collapsed
            else if(answerItem.isTextExpanded()){
                expandableTextView.collapse();
                answerItem.setTextExpanded(false);
            }

            adapter.notifyItemChanged(position, answerItem);
        }
    }

    @Override
    public void textExpanded(int position, View view, RelativeLayout relativeLayout, TextView textView, ImageView imageView) {

    }

    @Override
    public void textCollapsed(int position, View view, RelativeLayout relativeLayout, TextView textView, ImageView imageView) {

    }

    @Override
    public void textExceedMaxLines(int position, View view, RelativeLayout relativeLayout, TextView textView, ImageView imageView) {
        ExpandableTextView expandableTextView = (ExpandableTextView) view;
        Layout layout = expandableTextView.getLayout();
        if (layout != null) {
            int lines = layout.getLineCount();
            if (lines > 0) {
                int ellipsisCount = layout.getEllipsisCount(lines - 1);
                if (ellipsisCount > 0) {
                    relativeLayout.setVisibility(VISIBLE);
                } else {
                    relativeLayout.setVisibility(INVISIBLE);
                }
            }
        }
    }
}



    /*private void detectScrollChange(){
        mainRv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    Log.i(TAG, "Scroll DOWN");
                    collapsedText.setVisibility(View.VISIBLE);
                    fab.shrink();
                }
                if (scrollY < oldScrollY) {
                    Log.i(TAG, "Scroll UP");
                    collapsedText.setVisibility(View.VISIBLE);
                    fab.extend();
                }

                if (scrollY == 0) {
                    Log.i(TAG, "TOP SCROLL");
                    collapsedText.setVisibility(View.INVISIBLE);
                }

                if (scrollY == ( v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight() )) {
                    Log.i(TAG, "BOTTOM SCROLL");
                }
            }
        });
    }*/