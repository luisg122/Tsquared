package com.example.tsquared.Activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GeneralWebView extends AppCompatActivity {
    private String url = "";
    private String title = "";
    private Toolbar toolbar;
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_web_view);

        url   = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        setUpViews();
        setUpToolbar();
        initWebView(url);
    }

    private void setUpViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void setUpToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setTitle(title);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(String url){
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
}
