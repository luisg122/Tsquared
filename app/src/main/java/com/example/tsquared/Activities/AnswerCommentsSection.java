package com.example.tsquared.Activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.tsquared.Adapters.CommentsAdapter;
import com.example.tsquared.Fragments.CommentBottomSheet;
import com.example.tsquared.Fragments.ReplyCommentBottomSheet;
import com.example.tsquared.Models.CommentsModel;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.example.tsquared.Utils.BlurBehind;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AnswerCommentsSection extends AppCompatActivity implements CommentsAdapter.OnReplyListener {
    private ArrayList<CommentsModel> comments;
    private CommentsAdapter adapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private Handler handler;
    private ConstraintLayout addComment;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState){
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);

        setUpViews();
        setUpToolbar();
        setUpToolbarListener();
        postCommentListener();
        setUpRecyclerView();
        initializeHandler();
    }

    private void setUpViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.commentsList);
        addComment = (ConstraintLayout) findViewById(R.id.postCommentPrompt);
    }

    private void setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("Comments");
        setSupportActionBar(toolbar);
    }

    private void setUpToolbarListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void postCommentListener(){
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentBottomSheet bottomSheet = null;
                if(bottomSheet == null || !bottomSheet.isVisible()){
                    bottomSheet = new CommentBottomSheet();
                    bottomSheet.show(getSupportFragmentManager(), "ReplyBottomSheet");
                }
            }
        });
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView(){
        dummyDataSetUp();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new
                DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(recyclerView.getContext(),
                R.drawable.line_divider_black)));
        recyclerView.addItemDecoration(divider);

        adapter = new CommentsAdapter(comments, getApplicationContext(), this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    private void dummyDataSetUp(){
        comments = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            comments.add(new CommentsModel("John Smith", "07/11/2021", "I agree with this answer, however I feel like it's too elementary"));
        }
    }

    @Override
    public void onReplyClick(int position) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AnswerCommentsSection.this, ReplyComments.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }, 150);
    }

    @Override
    public void onUpVoteClick(int position, View view) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);

        // first time this will be null
        ValueAnimator buttonColorAnim = null;
        if(buttonColorAnim != null){
            // reverse the color
            buttonColorAnim.reverse();
            // reset for next time click
            buttonColorAnim = null;
            // add your code here to remove from database
        }
        else {
            final ImageView button = (ImageView) view;
            // create a color value animator
            buttonColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), Color.RED, R.color.darkOrange);
            // add a update listener for the animator
            buttonColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    // set the background color
                    button.setColorFilter((Integer) animator.getAnimatedValue());
                    // button.setColorFilter(getResources().getColor(R.color.darkOrange));
                    // button.setBackgroundColor((Integer) animator.getAnimatedValue());
                }
            });
            // you can also set a delay before start
            //buttonColorAnim.setStartDelay(2000); // 2 seconds
            // start the animator..
            buttonColorAnim.start();
            // add your code here to add to database
        }
    }

    @Override
    public void onDownVoteClick(int position, View view) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }
}
