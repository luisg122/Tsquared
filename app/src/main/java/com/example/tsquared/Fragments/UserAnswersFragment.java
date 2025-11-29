package com.example.tsquared.Fragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tsquared.Activities.AnswerCommentsSection;
import com.example.tsquared.Activities.AnswersActivity;
import com.example.tsquared.Adapters.UserAnswersAdapter;
import com.example.tsquared.Models.AnswerModel;
import com.example.tsquared.Models.AnswerWithImages;
import com.example.tsquared.Models.UserAnswerModel;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserAnswersFragment extends Fragment implements UserAnswersAdapter.OnUserAnswerClickListener {
    private View view;

    private RecyclerView answersRV;
    private UserAnswersAdapter answersAdapter;
    private ArrayList<UserAnswerModel> mArrayList;

    private int saveClickCounter;
    private Handler handler;

    public UserAnswersFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.user_answer_list, container, false);

        setUpViews();
        initializeHandler();
        setUpRecyclerView();
        return view;
    }

    private void setUpViews(){
        answersRV = (RecyclerView) view.findViewById(R.id.answer_list_rv);
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
    }

    private void setUpRecyclerView(){
        dummyDataSetUp();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        answersAdapter = new UserAnswersAdapter(mArrayList, getContext(), this);
        answersRV.setLayoutManager(layoutManager);


        answersRV.setAdapter(answersAdapter);
    }

    private void dummyDataSetUp(){
        mArrayList = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            mArrayList.add(new UserAnswerModel("John Doe", "What is a polynomial?", "polynomial is an expression consisting of variables and coefficients, that involves only the operations of addition, " +
                    "subtraction, multiplication, and non-negative integer exponentiation of variables."
                    , "Sept 09 2020", 15));
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
    public void answerClick(int position) {

    }

    @Override
    public void onCommentsClick(int position) {
        Intent intent = new Intent(getContext(), AnswerCommentsSection.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
    }

    @Override
    public void upVote(int position, View view, ImageView downVote) {
        UserAnswerModel answerItem = (UserAnswerModel) mArrayList.get(position);

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

        answersAdapter.notifyItemChanged(position, answerItem);
    }

    @Override
    public void downVote(int position, View view, ImageView upVote) {
        UserAnswerModel answerItem = (UserAnswerModel) mArrayList.get(position);

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

        answersAdapter.notifyItemChanged(position, answerItem);
    }

    @Override
    public void moreOptions(int position) {
        if(saveClickCounter++ == 0){
            MoreOptionsAnswers bottomSheet = new MoreOptionsAnswers();
            bottomSheet.show(requireActivity().getSupportFragmentManager(), bottomSheet.getTag());

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveClickCounter = 0;
                }
            }, 1000);
        }
    }
}
