package com.example.tsquared.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class BlogWindow extends AppCompatActivity {
    private EditText blogEditText;
    private ImageView close;
    private ImageView doneIcon;
    private ImageView undoIcon;
    private ImageView redoIcon;
    private Handler handler;
    private TextView publishPrompt;
    private CardView richContentFeatures;
    private CardView writePrompt;
    private AlertDialog alertDialog;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_create_window);
        setUpViews();
        initializeHandler();
        setListeners();
        showSoftKeyboard();
    }

    private void setUpViews() {
        doneIcon = (ImageView) findViewById(R.id.done);
        undoIcon = (ImageView) findViewById(R.id.undo);
        redoIcon = (ImageView) findViewById(R.id.redo);
        publishPrompt = (TextView) findViewById(R.id.publishPrompt);
        blogEditText = (EditText) findViewById(R.id.blogPost);
        close = (ImageView) findViewById(R.id.cancel_button);
        richContentFeatures = (CardView) findViewById(R.id.richContentFeatures);
        writePrompt = (CardView) findViewById(R.id.writePrompt);
        scrollView = (ScrollView) findViewById(R.id.blogScrollView);
    }

    private void initializeHandler() {
        handler = new Handler(Looper.getMainLooper());
    }

    private void setListeners() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(BlogWindow.this);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isContentEmpty()) {
                            onBackPressed();
                        } else {
                            openDialog();
                        }
                    }
                }, 100);
            }
        });

        blogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() <= 0) {
                    publishPrompt.setVisibility(View.INVISIBLE);
                    doneIcon.setVisibility(View.INVISIBLE);
                    undoIcon.setVisibility(View.INVISIBLE);
                    redoIcon.setVisibility(View.INVISIBLE);
                } else {
                    publishPrompt.setVisibility(View.VISIBLE);
                    doneIcon.setVisibility(View.VISIBLE);
                    undoIcon.setVisibility(View.VISIBLE);
                    redoIcon.setVisibility(View.VISIBLE);
                }
            }
        });

        doneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blogEditText.setEnabled(false);
                doneIcon.setVisibility(View.INVISIBLE);
                undoIcon.setVisibility(View.INVISIBLE);
                redoIcon.setVisibility(View.INVISIBLE);

                richContentFeatures.animate().translationY(richContentFeatures.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                writePrompt.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                writePrompt.setVisibility(View.VISIBLE);
            }
        });

        writePrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blogEditText.setEnabled(true);
                doneIcon.setVisibility(View.VISIBLE);
                undoIcon.setVisibility(View.VISIBLE);
                redoIcon.setVisibility(View.VISIBLE);

                richContentFeatures.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                writePrompt.animate().translationY(writePrompt.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                writePrompt.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean isContentEmpty() {
        return blogEditText.getText().toString().isEmpty();
    }

    private String getBlogContent() {
        return blogEditText.getText().toString();
    }

    private void showSoftKeyboard() {
        blogEditText.requestFocus();

        blogEditText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(blogEditText, InputMethodManager.SHOW_IMPLICIT);
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

    public void openDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        View view2 = LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
        TextView textView = (TextView) view2.findViewById(R.id.Quitprompt);

        textView.setText(R.string.save_as_draft);

        builder.setCustomTitle(view2);
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();

        Window window = alertDialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button saveButton = (Button) alertDialog.findViewById(R.id.continueButton);
        assert saveButton != null;
        saveButton.setText(R.string.option_yes);

        Button quitButton = (Button) alertDialog.findViewById(R.id.quitButton);
        assert quitButton != null;
        quitButton.setText(R.string.option_no);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 200);

                alertDialog.dismiss();
                finish();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && !isContentEmpty()) {
            openDialog();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }
}
