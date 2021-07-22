package com.example.tsquared.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.tsquared.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CommentBottomSheet extends BottomSheetDialogFragment {
    private View view;
    private EditText editText;
    private ImageButton respondButton;
    private Handler handler;
    private long delay = 500;  // half-second
    private long last_text_edit = 0;
    private Runnable input_finish_checker;

    public CommentBottomSheet(){

    }

    @Override
    public void onCreate(Bundle savedOnInstanceState){
        super.onCreate(savedOnInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.comment_bottom_sheet, container, false);

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
        initializeHandler();
        showSoftKeyboard();
        detectTyping();

        return view;
    }

    private void setUpViews(){
        editText = view.findViewById(R.id.commentText);
        respondButton = view.findViewById(R.id.respondButton);
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
                        }
                    }, 10);
                } else{
                    respondButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
