package com.example.tsquared.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tsquared.R;

public class PostQuestionWindow extends AppCompatActivity implements View.OnClickListener{
    Toolbar  toolbar;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_post);
        setViews();
        setUpToolBar();
        setListeners();
        showSoftKeyboard();
    }

    private void setListeners() {
        toolbar.setNavigationOnClickListener(this);
    }

    private void showSoftKeyboard() {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void setViews() {
        toolbar = findViewById(R.id.toolbar);
        editText = findViewById(R.id.topicPost);
    }

    private void setUpToolBar() {
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitle("New Question");
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View v) {
        //overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
        finish();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }
}
