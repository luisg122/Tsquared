package com.example.tsquared.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.tsquared.CustomWebView;
import com.example.tsquared.Fragments.MoreOptionsArticles;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Objects;

public class NewsWebView extends AppCompatActivity {
    private int saveClickCounter;
    private Handler handler;

    private CustomWebView webView;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CardView bottomBar;
    private Button moreOptions;
    private String url;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_web_view);

        getUrlFromIntent();
        setUpViews();
        initializeHandler();
        setUpToolBar();
        setToolbarListener();
        loadProgressBar();
        initWebView();
        setUpButtonListeners();
        webViewScrollingBehavior();
    }

    private void getUrlFromIntent(){
        Intent intent = getIntent();

        url = intent.getStringExtra("articleURL");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpViews(){
        webView = (CustomWebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        toolbar = (Toolbar) findViewById(R.id.siteOfNews);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        bottomBar = (CardView) findViewById(R.id.buttonsSection);
        moreOptions = (Button) findViewById(R.id.more);
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
    }

    private void loadProgressBar(){
        progressBar.setMax(100);
        progressBar.setProgress(1);
    }

    private void setUpButtonListeners(){
        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(saveClickCounter++ == 0){
                    MoreOptionsArticles bottomSheet = new MoreOptionsArticles();
                    bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            saveClickCounter = 0;
                        }
                    }, 1000);
                }
            }
        });
    }

    private void webViewScrollingBehavior(){
        CustomGestureDetector gestureDetector = new CustomGestureDetector();

        webView.setGestureDetector(new GestureDetector(getApplicationContext(), gestureDetector));
    }

    private void setUpToolBar() {
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);


        toolbar.setTitle(getNewsPublisher());
    }

    private String getNewsPublisher(){
        // Typically urls have a similar form, like https://www.bbc.com, we wish to find the index positions of the two dots
        // such that the character after our first dot is the beginning of our publisher name
        // and the character prior to our second dot is the ending of our publisher name

        int firstIndex = -1, secondIndex = -1;

        for(int i = 0; i < url.length(); i++){
            if(url.charAt(i) == '.' && firstIndex == -1) {
                firstIndex = i + 1;
            }

            else if(url.charAt(i) == '.' && firstIndex != -1){
                secondIndex = i;
                break;
            }
        }

        // java substrings [first index, second Index), where the second parameter is not inclusive
        return url.substring(firstIndex, secondIndex);
    }

    private void setToolbarListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(){
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        //webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //webView.loadUrl("file:///android_asset/error.html");
            }

            public void onLoadResource(WebView view, String url) { //Doesn't work
                //swipe.setRefreshing(true);
            }

            public void onPageFinished(WebView view, String url) {
                //Hide the SwipeRefreshLayout
                progressBar.setVisibility(View.GONE);
            }

        });
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed(){
        if(webView.canGoBack()) webView.goBack();
        else finish();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }

    private class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1 == null || e2 == null) return false;
            if(e1.getPointerCount() > 1 || e2.getPointerCount() > 1) return false;

            else {
                try {
                    if(e1.getY() - e2.getY() > 20 ) {
                        // Hide Toolbar

                        // TODO (-toolbar)  plus means  2 view above ho jaye or not visible to user
                        //appBarLayout.animate().translationY(-appBarLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));

                        // TODO uncomment this Hide Footer in android when Scrolling
                        // TODO (+toolbar)  plus means  2 view forward ho jaye or not visible to user
                        bottomBar.animate().translationY(+bottomBar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

                        // TODO keshav Hide Also Floatng Button In Android
                        // FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabButton.getLayoutParams();
                        // int fabBottomMargin = lp.bottomMargin;
                        // mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
                        // TODO keshav Hide Also Floatng Button In Android


                        // TODO uncomment this Hide Footer in android when Scrolling
                        // toolbar_bottom.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                        // mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                        webView.invalidate();
                        return false;
                    }
                    else if (e2.getY() - e1.getY() > 20 ) {
                        // Show Actionbar
                        // appBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));

                        // TODO uncomment this Hide Footer in android when Scrolling
                        bottomBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                        // mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                        webView.invalidate();
                        return false;
                    }

                } catch (Exception e) {
                    webView.invalidate();
                }
                return false;
            }
        }
    }
}