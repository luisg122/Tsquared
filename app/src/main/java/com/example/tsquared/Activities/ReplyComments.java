package com.example.tsquared.Activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;

import com.example.tsquared.Adapters.CommentsAdapter;
import com.example.tsquared.Adapters.CommentsRepliesAdapter;
import com.example.tsquared.Fragments.ReplyBottomSheet;
import com.example.tsquared.Fragments.ReplyCommentBottomSheet;
import com.example.tsquared.Models.CommentsModel;
import com.example.tsquared.Models.CommentsRepliesModel;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReplyComments extends AppCompatActivity implements CommentsRepliesAdapter.IconListener{
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<CommentsRepliesModel> replies;
    private CommentsRepliesAdapter adapter;
    private ConstraintLayout addReplyComment;
    private ImageView upVote;
    private ImageView downVote;
    private ImageView more;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_replies);
        setUpViews();
        setUpToolbar();
        setUpToolbarListener();
        setUpMainCommentListeners();
        postReplyComment();
        setUpRecyclerView();
    }

    private void setUpViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.replies);
        addReplyComment = (ConstraintLayout) findViewById(R.id.postReplyPrompt);
        upVote = (ImageView) findViewById(R.id.upVote);
        downVote = (ImageView) findViewById(R.id.downVote);
        more = (ImageView) findViewById(R.id.threeDots);
    }

    private void setUpToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("Replies");
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

    private void setUpMainCommentListeners(){
        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(80);
            }
        });

        downVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(80);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void postReplyComment(){
        addReplyComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReplyBottomSheet bottomSheet = null;
                if(bottomSheet == null || !bottomSheet.isVisible()){
                    bottomSheet = new ReplyBottomSheet();
                    bottomSheet.show(getSupportFragmentManager(), "ReplyBottomSheet");
                }
            }
        });
    }

    private void setUpRecyclerView(){
        dummyDataSetUp();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CommentsRepliesAdapter(replies, getApplicationContext(), this);
        recyclerView.setAdapter(adapter);
    }

    private void dummyDataSetUp(){
        replies = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            replies.add(new CommentsRepliesModel("John Smith", "07/11/2021", "I agree with this answer, however I feel like it's too elementary"));
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void replyClick(int position) {
        ReplyCommentBottomSheet bottomSheet = null;
        if(bottomSheet == null || !bottomSheet.isVisible()) {
            bottomSheet = new ReplyCommentBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "ReplyBottomSheet");
        }
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
}
