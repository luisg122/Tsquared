package com.example.tsquared.Activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;

import com.example.tsquared.Adapters.CommentsAdapter;
import com.example.tsquared.Fragments.CommentBottomSheet;
import com.example.tsquared.Fragments.MoreOptionsAnswers;
import com.example.tsquared.Fragments.MoreOptionsComments;
import com.example.tsquared.Models.CommentsModel;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AnswerCommentsSection extends AppCompatActivity
        implements CommentsAdapter.OnReplyListener, CommentBottomSheet.LoadNewCommentListener, MoreOptionsComments.BottomSheetPromptListener{


    private int saveClickCounter;

    private ArrayList<Object> comments;
    private CommentsAdapter adapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private Handler handler;


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
        initializeHandler();
        setUpRecyclerView();
    }


    private void setUpViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.commentsList);
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
        comments.add(new CommentBottomSheet(this));
        for(int i = 0; i < 10; i++){
            comments.add(new CommentsModel("John Smith", "07/11/2021", "I agree with this answer, however I feel like it's too elementary", 7));
        }
    }

    @Override
    public void onPostCommentClick(int position, CommentBottomSheet commentBottomSheet) {
        if(saveClickCounter++ == 0){
            CommentBottomSheet bottomSheet = new CommentBottomSheet(this);
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
    public void onReplyClick(int position) {
        CommentsModel commentsModel = (CommentsModel) comments.get(position);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AnswerCommentsSection.this, ReplyComments.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("commentId", "39320");
                intent.putExtra("name", commentsModel.getName());
                intent.putExtra("date", commentsModel.getDate());
                intent.putExtra("comment", commentsModel.getComment());
                intent.putExtra("numberOfUpVotes", commentsModel.getNumberOfUpVotes());
                intent.putExtra("numberOfReplies", commentsModel.getReplies());

                // transition user to the ReplyComment activity but do not initiate the ReplyBottomSheet DialogFragment
                intent.putExtra("replyToComment", false);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }, 150);
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
    public void onUpVoteClick(int position, View view, ImageView downVote) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);

        Object v = comments.get(position);
        if(v instanceof CommentsModel){
            CommentsModel comment = (CommentsModel) v;

            // Comment has not been upVoted
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

            // Comment has been upVoted previously
            else if(comment.isUpVoted()){
                comment.decrementNumberOfVotes();
                comment.setUpVoted(false);
                setButtonColor(view, false, true);
            }

            adapter.notifyItemChanged(position, comment);
        }
    }

    @Override
    public void onDownVoteClick(int position, View view, ImageView upVote) {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);

        Object v = comments.get(position);
        if(v instanceof CommentsModel){
            CommentsModel comment = (CommentsModel) v;

            // Comment has not been downVoted
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

            // Comment has been downVoted previously
            else if(comment.isDownVoted()){
                comment.incrementNumberOfVotes();
                comment.setDownVoted(false);
                setButtonColor(view, false, false);
            }

            adapter.notifyItemChanged(position, comment);
        }
    }

    @Override
    public void onMoreOptionsClick(int position) {
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
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }

    @Override
    public void insertNewComment(Intent questionData) {
        String comment = questionData.getStringExtra("comment");
        comments.add(1, new CommentsModel("Luis Gualpa", "11/09/2021", comment, 5));
        adapter.notifyItemInserted(1);
    }

    @Override
    public void replyPromptClick(int position) {
        CommentsModel commentsModel = (CommentsModel) comments.get(position);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AnswerCommentsSection.this, ReplyComments.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("commentId", "39320");
                intent.putExtra("name", commentsModel.getName());
                intent.putExtra("date", commentsModel.getDate());
                intent.putExtra("comment", commentsModel.getComment());
                intent.putExtra("numberOfUpVotes", commentsModel.getNumberOfUpVotes());
                intent.putExtra("numberOfReplies", commentsModel.getReplies());

                // transition user to the ReplyComment activity and initiate the ReplyBottomSheet DialogFragment
                intent.putExtra("replyToComment", true);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }, 150);
    }

    @Override
    public void reportCommentClick(int position){

    }
}
