package com.example.tsquared.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

public class AnswerWindow extends AppCompatActivity{
    private Toolbar toolbar;
    private EditText answerEditText;

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
        setUpToolBar();
        setListeners();
        showSoftKeyboard();
    }

    private void setViews(){
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
                onBackPressed();
            }
        });
    }

    private void showSoftKeyboard() {
        answerEditText.requestFocus();
        answerEditText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
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
        hideKeyboard(AnswerWindow.this);
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }
}
