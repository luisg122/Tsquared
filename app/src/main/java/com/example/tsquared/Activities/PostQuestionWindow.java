package com.example.tsquared.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class PostQuestionWindow extends AppCompatActivity {
    private Toolbar  toolbar;
    private EditText questionTitleText;
    private EditText questionEditText;
    private EditText linkText;
    private TextView countCharacters;
    private ImageView imageContent;
    private Handler handler;
    private Button submitQuestion;
    private FloatingActionButton imageButton;
    private FloatingActionButton linkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_window);
        setViews();
        setUpToolBar();
        initializeHandler();
        setListeners();
        showSoftKeyboard();
        countCharactersMethod();
    }

    private void setViews() {
        toolbar = findViewById(R.id.toolbar);
        questionTitleText = findViewById(R.id.postTitle);
        questionEditText = findViewById(R.id.topicPost);
        countCharacters  = findViewById(R.id.charactersLeftPrompt);
        submitQuestion = findViewById(R.id.addButton);
        imageButton = findViewById(R.id.imageButton);
        linkButton = findViewById(R.id.linkButton);
        linkText = findViewById(R.id.linkText);
        imageContent = findViewById(R.id.imageContent);
    }

    private void initializeHandler() {
        handler = new Handler(Looper.getMainLooper());
    }

    private void setListeners()  {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(PostQuestionWindow.this);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                }, 180);
            }
        });

        submitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(PostQuestionWindow.this);

                final String postTitle = questionTitleText.getText().toString().trim();
                final String postInformation = questionEditText.getText().toString().trim();

                if(postTitle.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(submitQuestion, "Cannot leave fields empty", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("Action", null);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor((Integer) getResources().getColor(R.color.mainColor));
                    snackbar.show();
                }

                else if(postTitle.length() > 250){
                    Snackbar snackbar = Snackbar.make(submitQuestion, "Question cannot have more than 250 characters", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("Action", null);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor((Integer) getResources().getColor(R.color.mainColor));
                    snackbar.show();
                }

                else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra("PostTitle", postTitle);
                            intent.putExtra("PostInformation", postInformation);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }, 180);
                }
            }
        });

        addQuestionMedia();
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() < 200) {
                countCharacters.setVisibility(View.INVISIBLE);
            } else {
                if(s.length() > 250) {
                    countCharacters.setTextColor((Integer) getResources()
                            .getColor(R.color.crimsonRed));
                } else {
                    TypedValue typedValue = new TypedValue();
                    getTheme().resolveAttribute(R.attr.lightTextColor, typedValue, true);
                    int color = ContextCompat.getColor(getApplicationContext(),
                            typedValue.resourceId);
                    countCharacters.setTextColor(color);
                }

                countCharacters.setVisibility(View.VISIBLE);
                countCharacters.setText(String.valueOf(s.length()) + "/250");
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    private void countCharactersMethod(){
        questionTitleText.addTextChangedListener(mTextEditorWatcher);
    }

    private void showSoftKeyboard() {
        questionTitleText.requestFocus();

        questionTitleText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(questionTitleText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideKeyboard(Activity activity) {
        // Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();

        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }

        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void addQuestionMedia() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageContent.setVisibility(View.VISIBLE);
                linkText.setVisibility(View.GONE);

                Glide.with(imageContent.getContext())
                        .load("https://stillmed.olympics.com/media/Images/OlympicOrg/News/2021/02/19/2021-02-19-tokyo-thumbnail.jpg")
                        .error(R.drawable.ic_link)
                        .into(imageContent);
            }
        });

        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageContent.setVisibility(View.GONE);
                linkText.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setUpToolBar() {
        toolbar.setNavigationIcon(R.drawable.ic_close_drawable_24dp);
        toolbar.setTitle("New Post");
        setSupportActionBar(toolbar);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }
}