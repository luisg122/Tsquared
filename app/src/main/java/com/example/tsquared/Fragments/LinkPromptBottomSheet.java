package com.example.tsquared.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
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
    private LinearLayout containerOfViewStub;
    private ProgressDialog progressDialog;
    private TextView headLine;
    private ImageView image;
    private Bitmap bitmap;
    private TextView source;

    private String title;
    private String publisherSource;
    private String imageSource;

    private LinearLayout content;



    public LinkPromptBottomSheet(){

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.link_bottom_sheet, container, false);
        // these lines are very important, want to make sure that all of the view from the BottomSheetDialog fully shown
        // or expanded
        Objects.requireNonNull(getDialog()).setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                assert bottomSheet != null;
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

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

    // must detect what the user is typing
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
                new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            inFlateViewBasedOnValidUrl();
                        }
                    }, 300);
                }
        });
    }

    // should be important to document this
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void inFlateViewBasedOnValidUrl(){
        if(checkValidLink() && (viewStub.getParent() != null)){
            viewStub.setLayoutResource(R.layout.viewstub_valid_link);
            viewStub.inflate();

            headLine = view.findViewById(R.id.headLine);
            image    = view.findViewById(R.id.linkImage);
            content  = view.findViewById(R.id.validLink);

            //source   = view.findViewById(R.id.source);
            new Content().execute();
        }

        else if(checkValidLink() && (viewStub.getParent() == null)){
            containerOfViewStub.removeAllViews();
            containerOfViewStub.addView(LayoutInflater.from(getContext()).inflate(R.layout.viewstub_valid_link, containerOfViewStub, false));

            headLine = view.findViewById(R.id.headLine);
            source   = view.findViewById(R.id.source);
            content  = view.findViewById(R.id.validLink);
            content.setVisibility(View.INVISIBLE);

            new Content().execute();
        }
        else if(!checkValidLink() && (viewStub.getParent() != null)){
            viewStub.setLayoutResource(R.layout.viewstub_invalid_link);
            viewStub.inflate();

            content = view.findViewById(R.id.invalidLink);
            content.setVisibility(View.VISIBLE);
        }
        else if(!checkValidLink() && (viewStub.getParent() == null)){
            containerOfViewStub.removeAllViews();
            containerOfViewStub.addView(LayoutInflater.from(getContext()).inflate(R.layout.viewstub_invalid_link, containerOfViewStub, false));

            content = view.findViewById(R.id.invalidLink);
            content.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkIfInputIsEmpty(){
        if(Objects.requireNonNull(editText.getText()).toString().trim().isEmpty()){
            return true;    // returns true only if field is empty
        }
        else{
            return false;   // return false only if field is not empty
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void hideKeyboard(@NonNull View v) {
        InputMethodManager inputManager = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkValidLink(){
        boolean isValidLink = android.util.Patterns.WEB_URL.matcher(Objects.requireNonNull(editText.getText()).toString().trim()).matches();
        if(isValidLink && !checkIfInputIsEmpty())  return true;
        return false;
    }

    // web scrape title and link of article from provided Url
    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Connect to the website
                Document document = Jsoup.connect(Objects.requireNonNull(editText.getText()).toString().trim()).get();

                // get the title and thumbnail image of the webpage
                boolean gotTitle = false, gotImage = false;
                Elements elements = document.select("meta");
                for(Element e : elements){
                    imageSource = e.attr("content");
                    if(e.attr("property").equalsIgnoreCase("og:image")){
                        imageSource = e.attr("content");
                        gotImage = true;
                    }

                    if(e.attr("property").equalsIgnoreCase("og:title")){
                        title = e.attr("content");
                        gotTitle = true;
                    }

                    if(gotTitle && gotImage) break;
                }

                // InputStream input = new java.net.URL(imageSource).openStream();
                // bitmap = BitmapFactory.decodeStream(input);

                //publisherSource = document.baseUri();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Glide.with(Objects.requireNonNull(getActivity()))
                    .load(imageSource)
                    .error(R.drawable.ic_link)
                    .into(image);

            headLine.setText(title);
            //source.setText(publisherSource);

            content.setVisibility(View.VISIBLE);
            fab.setVisibility(View.INVISIBLE);
            progressDialog.dismiss();
        }
    }
}