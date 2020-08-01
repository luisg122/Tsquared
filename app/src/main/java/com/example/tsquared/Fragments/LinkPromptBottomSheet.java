package com.example.tsquared.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.tsquared.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LinkPromptBottomSheet extends BottomSheetDialogFragment {
    private View view;
    private TextInputLayout textInputLayout;
    private TextInputEditText editText;
    private FloatingActionButton fab;
    private Button button;

    private long delay = 500;  // half-second
    private long last_text_edit = 0;
    private Handler handler;
    private Runnable input_finish_checker;
    private ViewStub viewStub;
    private LinearLayout rootView;
    private LinearLayout containerOfViewStub;

    public LinkPromptBottomSheet(){

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.link_bottom_sheet, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog())
                .getWindow())
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setUpViews();
        initializeRunnable();
        detectTyping();
        return view;
    }

    private void setUpViews(){
        textInputLayout = view.findViewById(R.id.linkLayout);
        editText        = view.findViewById(R.id.linkEditText);
        fab             = view.findViewById(R.id.checkValidLink);
        viewStub        = view.findViewById(R.id.layout_stub);
        rootView        = view.findViewById(R.id.linearLayout);
        containerOfViewStub = view.findViewById(R.id.linearLayoutContainer);
    }

    private void initializeRunnable(){
        handler = new Handler();
        input_finish_checker = new Runnable() {
            @Override
            public void run() {
                if(System.currentTimeMillis() > (last_text_edit + delay - 500)){
                    fab.setVisibility(View.VISIBLE);
                    checkLinkInputAfterPost(fab);
                }
            }
        };
    }

    private void detectTyping(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(input_finish_checker);
                fab.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                } else{

                }
            }
        });
    }

    private void checkLinkInputAfterPost(FloatingActionButton fab){
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                // The index of viewStub is '4' in layout hierarchy
                // we need this index to remove the layout that the viewStub inflated
                // and dynamically add a second layout
                final int index = rootView.indexOfChild(containerOfViewStub);
                Log.d("indexOf", "position of LL of " + index);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        inFlateViewBasedOnValidUrl(index);
                    }
                }, 300);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void inFlateViewBasedOnValidUrl(int index){
        if(checkValidLink() && (viewStub.getParent() != null)){
            viewStub.setLayoutResource(R.layout.viewstub_valid_link);
            viewStub.inflate();
        }
        else if(checkValidLink() && (viewStub.getParent() == null)){
            containerOfViewStub.removeAllViews();
            containerOfViewStub.addView(LayoutInflater.from(getContext()).inflate(R.layout.viewstub_valid_link, containerOfViewStub, false));
        }
        else if(!checkValidLink() && (viewStub.getParent() != null)){
            viewStub.setLayoutResource(R.layout.viewstub_invalid_link);
            viewStub.inflate();
        }
        else if(!checkValidLink() && (viewStub.getParent() == null)){
            containerOfViewStub.removeAllViews();
            containerOfViewStub.addView(LayoutInflater.from(getContext()).inflate(R.layout.viewstub_invalid_link, containerOfViewStub, false));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void hideKeyboard(@NonNull View v) {
        //if(detectIfKeyboardIsOpen()) {
            InputMethodManager inputManager = (InputMethodManager) v.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        //}
    }

    // How to detect if the soft input keyboard is opened in a bottomSheetDialogFragment?
    private boolean detectIfKeyboardIsOpen(){
        final boolean[] value = new boolean[1];
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int maxRootViewHeight     = 0;
            int currentRootViewHeight = 0;
            @Override
            public void onGlobalLayout() {
                //currentRootViewHeight = view.getRootView().getHeight();
                currentRootViewHeight = view.getHeight();
                if(currentRootViewHeight > maxRootViewHeight){
                    maxRootViewHeight = currentRootViewHeight;
                }
                if(currentRootViewHeight >= maxRootViewHeight){
                    // Keyboard is hidden
                    value[0] = false;
                }
                else if(currentRootViewHeight < maxRootViewHeight){
                    // Keyboard is shown
                    value[0] = true;
                }
            }
        });
        return value[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkValidLink(){
        if(android.util.Patterns.WEB_URL.matcher(Objects.requireNonNull(editText.getText()).toString().trim()).matches()){
            return true;
        }
        return false;
    }
}
                /*Rect r = new Rect();
                //r will be populated with the coordinates of the view where that area still visible.
                view.getWindowVisibleDisplayFrame(r);
                int screenHeight = view.getRootView().getHeight();
                int heightDiff   = screenHeight - (r.bottom - r.top);
                boolean visible  = heightDiff > (screenHeight / 3);
                if(visible){
                    Log.e("isOpened", "Keyboard is open");
                    value[0]  = true;
                }
                else {
                    Log.e("isClosed", "Keyboard is closed");
                    value[0] = false;
                }*/