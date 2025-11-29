package com.example.tsquared.Activities;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.Layout;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tsquared.Adapters.AnswerAdapter;
import com.example.tsquared.ExpandableTextView;
import com.example.tsquared.Fragments.MoreOptionsAnswers;
import com.example.tsquared.Fragments.MoreOptionsQuestions;
import com.example.tsquared.Fragments.TopicsBottomSheet;
import com.example.tsquared.Models.AnswerModel;
import com.example.tsquared.Models.AnswerWithImages;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class AnswersActivity extends AppCompatActivity implements AnswerAdapter.OnCommentsClickListener {
    private int saveClickCounter;
    private Handler handler;

    private RecyclerView mainRv;
    private ArrayList<Object> mArrayList;
    private AnswerAdapter adapter;
    private Toolbar toolbar;
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
        initializeHandler();
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
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
    }

    private void getQuestion(){
        toWhatQuestion = getIntent().getStringExtra("question");
    }

    /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setLayout() {
        stub.setLayoutResource(R.layout.activity_detail);
        stub.inflate();
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

        adapter = new AnswerAdapter(mArrayList, getApplicationContext(), this);
        mainRv.setLayoutManager(layoutManager);
        mainRv.setHasFixedSize(false);
        mainRv.setItemViewCacheSize(20);
        mainRv.setAdapter(adapter);

        mainRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && fab != null) {
                    // Scrolled Downwards
                    fab.shrink();
                } else if (dy < 0 && fab != null) {
                    // Scrolled Upwards
                    fab.extend();
                }
            }
        });
    }

    private void dummyData(){
        mArrayList  = new ArrayList<>();

        mArrayList.add(getIntent());
        for(int i = 0; i < 20; i++){
            AnswerModel answerItem = new AnswerModel("John Doe", "polynomial is an expression consisting of variables and coefficients, that involves only the operations of addition, " +
                    "subtraction, multiplication, and non-negative integer exponentiation of variables."
                    , "Sept 09 2020", 15);
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
                    , "Sept 09 2020", imageUrls, 13);

            mArrayList.add(answerItem1);
        }
    }

    public void initFloatingActionButton(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnswersActivity.this, AnswerWindow.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activityResultLauncher.launch(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        assert data != null;
                        String answer = data.getStringExtra("Answer");
                        AnswerModel answerItem = new AnswerModel("Luis Gualpa", answer, "Oct 30 2021", 2);
                        mArrayList.add(1, answerItem);
                        adapter.notifyItemInserted(1);

                        // do database stuff here, need the email address to associate question with user
                        // where to store the user email however
                    }
                }
            });

    private void setUpToolBar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle(R.string.question);
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
    }*s/

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
    }

    @Override
    public void onPause() {
        super.onPause();
        fab.hide();
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



    private void setButtonColor(View view, Boolean toChangeColor, boolean like){
        final ImageView button = (ImageView) view;

        Integer upvoteColor   = (Integer) getResources().getColor(R.color.green);
        Integer downVoteColor = (Integer) getResources().getColor(R.color.crimsonRed);

        Integer darkModeDefaultColor  = (Integer) getResources().getColor(R.color.white);
        Integer lightModeDefaultColor = (Integer) getResources().getColor(R.color.black);

        Integer originalColor = DarkSharedPref.isDark ? darkModeDefaultColor : lightModeDefaultColor;

        Integer changedColor  = like ? upvoteColor : downVoteColor;


        ValueAnimator buttonColorAnim = null;
        if(toChangeColor) {
            // create a color value animator
            buttonColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), originalColor, changedColor);

            buttonColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    button.setColorFilter((Integer) animator.getAnimatedValue());
                }
            });

        }

        else {
            // create a color value animator
            buttonColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), changedColor, originalColor);

            buttonColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    button.setColorFilter((Integer) animator.getAnimatedValue());
                }
            });

        }
        buttonColorAnim.setStartDelay(50);
        buttonColorAnim.start();
    }

    @Override
    public void upVote(int position, View view, ImageView downVote) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);

        Object v = (Object) mArrayList.get(position);
        if(v instanceof AnswerModel){
            AnswerModel answerItem = (AnswerModel) v;

            // Answer has not been upVoted
            if(!answerItem.isUpVoted()){
                if(answerItem.isDownVoted()){
                    answerItem.incrementNumberOfVotes();
                    answerItem.setDownVoted(false);
                    setButtonColor(downVote, false, false);
                }

                answerItem.incrementNumberOfVotes();
                answerItem.setUpVoted(true);
                setButtonColor(view, true, true);
            }

            // Answer has been upVoted previously
            else if(answerItem.isUpVoted()){
                answerItem.decrementNumberOfVotes();
                answerItem.setUpVoted(false);
                setButtonColor(view, false, true);
            }

            adapter.notifyItemChanged(position, answerItem);
        }

        else if(v instanceof AnswerWithImages) {
            AnswerWithImages answerItem = (AnswerWithImages) v;

            // Answer has not been upVoted
            if(!answerItem.isUpVoted()){
                if(answerItem.isDownVoted()){
                    answerItem.incrementNumberOfVotes();
                    setButtonColor(downVote, false, false);
                    answerItem.setDownVoted(false);
                }

                answerItem.incrementNumberOfVotes();
                answerItem.setUpVoted(true);
                setButtonColor(view, true, true);
            }

            // Answer has been upVoted previously
            else if(answerItem.isUpVoted()){
                answerItem.decrementNumberOfVotes();
                answerItem.setUpVoted(false);
                setButtonColor(view, false, true);
            }

            adapter.notifyItemChanged(position, answerItem);
        }
    }

    @Override
    public void downVote(int position, View view, ImageView upVote) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);

        Object v = (Object) mArrayList.get(position);
        if(v instanceof AnswerModel){
            AnswerModel answerItem = (AnswerModel) v;

            // Answer has not been downVoted
            if(!answerItem.isDownVoted()){
                if(answerItem.isUpVoted()){
                    answerItem.decrementNumberOfVotes();
                    answerItem.setUpVoted(false);
                    setButtonColor(upVote, false, true);
                }

                answerItem.decrementNumberOfVotes();
                answerItem.setDownVoted(true);
                setButtonColor(view, true, false);
            }

            // Answer has been downVoted previously
            else if(answerItem.isDownVoted()){
                answerItem.incrementNumberOfVotes();
                answerItem.setDownVoted(false);
                setButtonColor(view, false, false);
            }

            adapter.notifyItemChanged(position, answerItem);
        }

        else if(v instanceof AnswerWithImages) {
            AnswerWithImages answerItem = (AnswerWithImages) v;

            // Answer has not been downVoted
            if(!answerItem.isDownVoted()) {
                if(answerItem.isUpVoted()){
                    answerItem.decrementNumberOfVotes();
                    answerItem.setUpVoted(false);
                    setButtonColor(upVote, false, true);
                }

                answerItem.decrementNumberOfVotes();
                answerItem.setDownVoted(true);
                setButtonColor(view, true, false);
            }

            // Answer has been downVoted previously
            else if(answerItem.isDownVoted()){
                answerItem.incrementNumberOfVotes();
                answerItem.setDownVoted(false);
                setButtonColor(view, false, false);
            }

            adapter.notifyItemChanged(position, answerItem);
        }
    }

    void expandTouchArea(View delegate, int extraPadding){
        final View parent = (View) delegate.getParent();
        parent.post( new Runnable() {
            // Post in the parent's message queue to make sure the parent
            // lays out its children before we call getHitRect()
            public void run() {
                final Rect rect = new Rect();
                delegate.getHitRect(rect);
                rect.top -= extraPadding;
                rect.left -= extraPadding;
                rect.right += extraPadding;
                rect.bottom += extraPadding;
                parent.setTouchDelegate( new TouchDelegate(rect, delegate));
            }
        });
    }

    @Override
    public void follow(int position, View view) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);

        // expand touch area of view
        expandTouchArea(view, 70);

        Object v = mArrayList.get(position);
        if(v instanceof AnswerModel){
            AnswerModel answerItem = (AnswerModel) v;

            if(!answerItem.isFollowing()){
                answerItem.setFollowing(true);
            }

            else if(answerItem.isFollowing){
                answerItem.setFollowing(false);
            }

            adapter.notifyItemChanged(position, answerItem);
        }

        else if(v instanceof  AnswerWithImages){
            AnswerWithImages answerItem = (AnswerWithImages) v;

            if(!answerItem.isFollowing()){
                answerItem.setFollowing(true);
            }

            else if(answerItem.isFollowing){
                answerItem.setFollowing(false);
            }

            adapter.notifyItemChanged(position, answerItem);
        }
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
        Object v = (Object) mArrayList.get(position);

        boolean textAlreadyExpanded = false;
        if(v instanceof AnswerModel){
            AnswerModel answerItem = (AnswerModel) v;

            textAlreadyExpanded = answerItem.isTextExpanded();
        }

        else if(v instanceof AnswerWithImages){
            AnswerWithImages answerItem = (AnswerWithImages) v;

            textAlreadyExpanded = answerItem.isTextExpanded();
        }

        ExpandableTextView expandableTextView = (ExpandableTextView) view;
        Layout layout = expandableTextView.getLayout();
        if (layout != null) {
            int lines = layout.getLineCount();

            int ellipsisCount = layout.getEllipsisCount(lines - 1);

            if ((ellipsisCount > 0 || lines > 5) && !textAlreadyExpanded) {
                relativeLayout.setVisibility(VISIBLE);
            } else {
                relativeLayout.setVisibility(INVISIBLE);
            }
        }
    }

    @Override
    public void imageOneClick(int position, int imagePos) {
        Intent intent = new Intent(AnswersActivity.this, AnswerImagesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void imageTwoClick(int position, int imagePos) {
        Intent intent = new Intent(AnswersActivity.this, AnswerImagesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void moreImageClick(int position, int imagePos) {
        Intent intent = new Intent(AnswersActivity.this, AnswerImagesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void moreOptions(int position) {
        // At this position, we have our question
        if(position == 0){
            if(saveClickCounter++ == 0){
                MoreOptionsQuestions bottomSheet = new MoreOptionsQuestions();
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        saveClickCounter = 0;
                    }
                }, 1000);
            }

            return;
        }

        // At other positions, we have our answers to our question
        if(saveClickCounter++ == 0){
            MoreOptionsAnswers bottomSheet = new MoreOptionsAnswers();
            bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveClickCounter = 0;
                }
            }, 1000);
        }
    }
}