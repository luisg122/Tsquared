package com.example.tsquared.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tsquared.Activities.AnswerCommentsSection;
import com.example.tsquared.Activities.ReplyComments;
import com.example.tsquared.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ReplyBottomSheet extends BottomSheetDialogFragment {
    private View view;
    private EditText editText;
    private ImageButton respondButton;
    private ImageView profileImage;
    private Handler handler;
    private long delay = 500;  // half-second
    private long last_text_edit = 0;
    private Runnable input_finish_checker;

    LoadNewReplyListener loadNewReplyListener;

    public ReplyBottomSheet(LoadNewReplyListener loadNewReplyListener){
        this.loadNewReplyListener = loadNewReplyListener;
    }

    @Override
    public void onCreate(Bundle savedOnInstanceState){
        super.onCreate(savedOnInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.comment_bottom_sheet, container, false);

        // getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        Objects.requireNonNull(getDialog()).setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        setUpViews();
        setUpProfileImage();
        modifyHintText();
        initializeHandler();
        showSoftKeyboard();
        detectTyping();

        return view;
    }

    private void setUpViews(){
        profileImage = view.findViewById(R.id.imageIV);
        editText = view.findViewById(R.id.commentText);
        respondButton = view.findViewById(R.id.respondButton);
    }

    private void setUpProfileImage(){
        Glide.with(profileImage.getContext())
                .load("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500")
                .into(profileImage);
    }

    private void modifyHintText(){
        editText.setHint("Add a reply . . .");
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
    }

    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);

        editText.requestFocus();
        editText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    // must detect what the user is typing
    private void detectTyping(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    // last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            respondButton.setVisibility(View.VISIBLE);
                            submitButtonListener();
                        }
                    }, 10);
                } else{
                    respondButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void submitButtonListener(){
        respondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentData = editText.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("comment", commentData);
                loadNewReplyListener.insertReplyComment(intent);
                dismiss();
            }
        });
    }

    public interface LoadNewReplyListener{
        void insertReplyComment(Intent questionData);
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
}

