package com.example.tsquared.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

public class AnswerWindow extends AppCompatActivity{
    private Toolbar toolbar;
    private EditText answerEditText;
    private ScrollView scrollView;
    private LinearLayout rootView;
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
        setContentView(R.layout.answer_window);
        setViews();
        initializeHandler();
        setUpToolBar();
        setListeners();
        showSoftKeyboard();
        disableInnerScroll();
    }

    private void initializeHandler() {
        handler = new Handler(Looper.getMainLooper());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void disableInnerScroll() {
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                answerEditText.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
    }

    private void setViews() {
        rootView        = (LinearLayout) findViewById(R.id.linearLayout);
        scrollView      = (ScrollView) findViewById(R.id.scrollView);
        toolbar         = (Toolbar)  findViewById(R.id.toolbar);
        answerEditText  = (EditText) findViewById(R.id.answerToQuestion);
    }

    private void setUpToolBar() {
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        setSupportActionBar(toolbar);
    }

    private void setListeners() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(AnswerWindow.this);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                }, 250);
            }
        });
    }

    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);

        answerEditText.requestFocus();
        answerEditText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        imm.showSoftInput(answerEditText, InputMethodManager.SHOW_IMPLICIT);

    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }
}
