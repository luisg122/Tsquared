package com.example.tsquared.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
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
        editText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
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
        hideKeyboard(PostQuestionWindow.this);
        finish();
    }

    @Override
    public void finish(){
        super.finish();
        hideKeyboard(PostQuestionWindow.this);
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }
}
