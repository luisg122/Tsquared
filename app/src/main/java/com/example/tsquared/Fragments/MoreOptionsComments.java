package com.example.tsquared.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tsquared.Activities.AnswerCommentsSection;
import com.example.tsquared.Activities.AnswersActivity;
import com.example.tsquared.Activities.ReplyComments;
import com.example.tsquared.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class MoreOptionsComments extends BottomSheetDialogFragment implements View.OnClickListener{
    private View view;
    private ImageView cancel_IV;
    private TextView titlePrompt;

    private BottomSheetPromptListener bottomSheetPromptListener;
    private int position;


    private RelativeLayout editComment;
    private RelativeLayout replyToComment;
    private RelativeLayout reportComment;

    public MoreOptionsComments(BottomSheetPromptListener bottomSheetPromptListener, int position){
        this.bottomSheetPromptListener = bottomSheetPromptListener;

        this.position = position;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.options_comments, container, false);

        // these lines are very important, want to make sure that all of the view from the BottomSheetDialog fully shown
        // or expanded
        Objects.requireNonNull(getDialog()).setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                assert bottomSheet != null;
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        setUpViews();
        setUpClickListeners();

        return view;
    }

    private void setUpViews(){
        cancel_IV = (ImageView) view.findViewById(R.id.cancel_button);
        titlePrompt = (TextView) view.findViewById(R.id.promptOptions);

        editComment = (RelativeLayout) view.findViewById(R.id.editComment);
        replyToComment = (RelativeLayout) view.findViewById(R.id.replyToComment);
        reportComment  = (RelativeLayout) view.findViewById(R.id.report);
    }

    private void setUpClickListeners(){
        cancel_IV.setOnClickListener(this);

        editComment.setOnClickListener(this);
        replyToComment.setOnClickListener(this);
        reportComment.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.cancel_button) {
            dismiss();
        } else if (id == R.id.editComment) {
            // Handle edit comment click
        } else if (id == R.id.replyToComment) {
            bottomSheetPromptListener.replyPromptClick(position);
            dismiss();
        } else if (id == R.id.report) {
            bottomSheetPromptListener.reportCommentClick(position);
            dismiss();
        } else {
            // It's good practice to have a default case for unexpected IDs
            throw new IllegalStateException("Unexpected value: " + id);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e( "Fragment is visible", "Fragment is visible");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Fragment is not visible", "Fragment is not visible");
    }

    @Override
    public void dismiss(){
        super.dismiss();
    }

    // listens for user-clicks event listeners on the MoreOptionsComments bottomSheetDialogFragment
    public interface BottomSheetPromptListener{
        void replyPromptClick(int position);
        void reportCommentClick(int position);
    }
}
