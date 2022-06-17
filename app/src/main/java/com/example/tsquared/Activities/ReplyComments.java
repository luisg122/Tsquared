package com.example.tsquared.Activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tsquared.Adapters.CommentsAdapter;
import com.example.tsquared.Adapters.CommentsRepliesAdapter;
import com.example.tsquared.Fragments.MoreOptionsAnswers;
import com.example.tsquared.Fragments.MoreOptionsComments;
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

public class ReplyComments extends AppCompatActivity
        implements CommentsRepliesAdapter.IconListener, ReplyBottomSheet.LoadNewReplyListener,
        ReplyCommentBottomSheet.LoadNewReplyToUserListener, MoreOptionsComments.BottomSheetPromptListener{


    private int saveClickCounter, secondSaveClickCounter;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<Object> replies;
    private CommentsRepliesAdapter adapter;
    private Handler handler;

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
        setUpRecyclerView();
        initializeHandler();
        initiateReplyBottomSheet();
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
    }

    private void setUpViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.replies);
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

    private void setUpRecyclerView(){
        dummyDataSetUp();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CommentsRepliesAdapter(replies, getApplicationContext(), this);
        recyclerView.setAdapter(adapter);
    }

    private CommentsModel loadCommentsData() {
        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");
        String comment = intent.getStringExtra("comment");
        int upVotes = intent.getIntExtra("numberOfUpVotes", 0);

        String replies = intent.getStringExtra("numberOfReplies");

        return new CommentsModel(name, date, comment, upVotes);
    }

    private void dummyDataSetUp(){
        replies = new ArrayList<>();

        // main comment data
        replies.add(loadCommentsData());

        // prompt for user to reply to main comment
        replies.add("ReplyToMainCommentPrompt");

        // all of the replies to the main comment
        for(int i = 0; i < 10; i++){
            replies.add(new CommentsRepliesModel("John Smith", "07/11/2021", "I agree with this answer, however I feel like it's too elementary", 7));
        }
    }

    private void initiateReplyBottomSheet(){
        Intent intent = getIntent();

        // determine if we should open up ReplyBottomSheet DialogFragment, which would allow the user to reply to the main comment.
        boolean openReplyBottomSheet = intent.getBooleanExtra("replyToComment", false);

        if(openReplyBottomSheet && saveClickCounter++ == 0){
            ReplyBottomSheet replyBottomSheet = replyBottomSheet = new ReplyBottomSheet(ReplyComments.this);
            replyBottomSheet.show(getSupportFragmentManager(), replyBottomSheet.getTag());

            handler.postDelayed(new Runnable() {
                @Override
                public void run(){
                    saveClickCounter = 0;
                }
            }, 1000);
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void setButtonColor(View view, Boolean toChangeColor, boolean like){
        final ImageView button = (ImageView) view;

        Integer originalColor = null, changedColor = null;
        if(DarkSharedPref.isDark) {
            originalColor = (Integer) getResources().getColor(R.color.white);
        }

        else {
            originalColor = (Integer) getResources().getColor(R.color.black);
        }

        if(like){
            changedColor = (Integer) getResources().getColor(R.color.green);
        }

        else {
            changedColor = (Integer) getResources().getColor(R.color.crimsonRed);
        }


        if(toChangeColor) {
            // create a color value animator
            ValueAnimator buttonColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), originalColor, changedColor);

            buttonColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    button.setColorFilter((Integer) animator.getAnimatedValue());
                }
            });

            buttonColorAnim.setStartDelay(50);
            buttonColorAnim.start();
        }

        else {
            // create a color value animator
            ValueAnimator buttonColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), changedColor, originalColor);

            buttonColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    button.setColorFilter((Integer) animator.getAnimatedValue());
                }
            });

            buttonColorAnim.setStartDelay(50);
            buttonColorAnim.start();
        }
    }

    @Override
    public void upVote(int position, View view, ImageView downVote) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);

        Object v = replies.get(position);
        if(v instanceof CommentsRepliesModel){
            CommentsRepliesModel comment = (CommentsRepliesModel) v;

            // comment has not been upVoted
            if(!comment.isUpVoted()){
                if(comment.isDownVoted()){
                    comment.incrementNumberOfVotes();
                    comment.setDownVoted(false);
                    setButtonColor(downVote, false, false);
                }

                comment.incrementNumberOfVotes();
                comment.setUpVoted(true);
                setButtonColor(view, true, true);
            }

            // comment has been upVoted previously
            else if(comment.isUpVoted()){
                comment.decrementNumberOfVotes();
                comment.setUpVoted(false);
                setButtonColor(view, false, true);
            }

            adapter.notifyItemChanged(position, comment);
        }
    }

    @Override
    public void downVote(int position, View view, ImageView upVote) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);

        Object v = replies.get(position);
        if(v instanceof CommentsRepliesModel){
            CommentsRepliesModel comment = (CommentsRepliesModel) v;

            // comment has not been downVoted
            if(!comment.isDownVoted()){
                if(comment.isUpVoted()){
                    comment.decrementNumberOfVotes();
                    comment.setUpVoted(false);
                    setButtonColor(upVote, false, true);
                }

                comment.decrementNumberOfVotes();
                comment.setDownVoted(true);
                setButtonColor(view, true, false);

            }

            // comment has been downVoted previously
            else if(comment.isDownVoted()){
                comment.incrementNumberOfVotes();
                comment.setDownVoted(false);
                setButtonColor(view, false, false);
            }

            adapter.notifyItemChanged(position, comment);
        }
    }

    @Override
    public void mainCommentUpVote(int position, View view, ImageView downVote) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);

        Object v = replies.get(position);
        if(v instanceof CommentsModel){
            CommentsModel comment = (CommentsModel) v;

            // comment has not been upVoted
            if(!comment.isUpVoted()){
                if(comment.isDownVoted()){
                    comment.incrementNumberOfVotes();
                    comment.setDownVoted(false);
                    setButtonColor(downVote, false, false);
                }

                comment.incrementNumberOfVotes();
                comment.setUpVoted(true);
                setButtonColor(view, true, true);
            }

            // comment has been upVoted previously
            else if(comment.isUpVoted()){
                comment.decrementNumberOfVotes();
                comment.setUpVoted(false);
                setButtonColor(view, false, true);
            }

            adapter.notifyItemChanged(position, comment);
        }
    }

    @Override
    public void mainCommentDownVote(int position, View view, ImageView upVote) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);

        Object v = replies.get(position);
        if(v instanceof CommentsModel){
            CommentsModel comment = (CommentsModel) v;

            // comment has not been downVoted
            if(!comment.isDownVoted()){
                if(comment.isUpVoted()){
                    comment.decrementNumberOfVotes();
                    comment.setUpVoted(false);
                    setButtonColor(upVote, false, true);
                }

                comment.decrementNumberOfVotes();
                comment.setDownVoted(true);
                setButtonColor(view, true, false);

            }

            // comment has been downVoted previously
            else if(comment.isDownVoted()){
                comment.incrementNumberOfVotes();
                comment.setDownVoted(false);
                setButtonColor(view, false, false);
            }

            adapter.notifyItemChanged(position, comment);
        }
    }

    @Override
    public void mainCommentMore(int position, View view) {
        if(saveClickCounter++ == 0){
            MoreOptionsComments bottomSheet = new MoreOptionsComments(this, position);
            bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveClickCounter = 0;
                }
            }, 1000);
        }
    }

    @Override
    public void postReplyComment(int position) {
        if(saveClickCounter++ == 0){
            ReplyBottomSheet bottomSheet = new ReplyBottomSheet(this);
            bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveClickCounter = 0;
                }
            }, 1000);
        }
    }

    @Override
    public void moreOptions(int position) {
        if(saveClickCounter++ == 0){
            MoreOptionsComments bottomSheet = new MoreOptionsComments(this, position);
            bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveClickCounter = 0;
                }
            }, 1000);
        }
    }

    // comment reply to main comment
    @Override
    public void insertReplyComment(Intent questionData) {
        String comment = questionData.getStringExtra("comment");
        replies.add(2, new CommentsRepliesModel("Luis Gualpa", "11/09/2021", comment, 5));
        adapter.notifyItemInserted(2);
    }

    @Override
    public void insertReplyToUserComment(Intent questionData) {
        String comment = questionData.getStringExtra("comment");
        replies.add(2, new CommentsRepliesModel("Luis Gualpa", "11/09/2021", comment, 5));
        adapter.notifyItemInserted(2);
    }

    @Override
    public void replyPromptClick(int position) {
        Object obj  = replies.get(position);
        if(obj instanceof CommentsRepliesModel){
            CommentsRepliesModel commentsReply = (CommentsRepliesModel) obj;
            String name = commentsReply.getName();

            if(secondSaveClickCounter++ == 0){
                ReplyCommentBottomSheet bottomSheet = new ReplyCommentBottomSheet(this, name);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        secondSaveClickCounter = 0;
                    }
                }, 1000);
            }
        }
    }

    @Override
    public void reportCommentClick(int position){

    }
}