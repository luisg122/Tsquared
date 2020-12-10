package com.example.tsquared.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

public class IdeaPostActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText editText;
    private TextView charCounter;
    private TextWatcher mTextEditorWatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idea_post);
        setUpViews();
        setUpToolbar();
        showSoftKeyboard();
        characterCounter();
    }

    private void setUpViews(){
        toolbar  = (Toolbar)  findViewById(R.id.toolbar);
        editText    = (EditText) findViewById(R.id.ideaContent);
        charCounter = (TextView) findViewById(R.id.charactersLimit);
    }

    private void setUpToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

    private void characterCounter(){
        mTextEditorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                charCounter.setText(String.valueOf(s.length()) + "/270");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        editText.addTextChangedListener(mTextEditorWatcher);
    }

    @Override
    public void finish(){
        super.finish();
        hideKeyboard(IdeaPostActivity.this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}