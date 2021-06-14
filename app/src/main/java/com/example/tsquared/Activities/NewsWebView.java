package com.example.tsquared.Activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

public class NewsWebView extends AppCompatActivity {
    private Button back;
    private WebView webView;
    private ProgressBar progressBar;
    private Toolbar toolbar;
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
        setUpViews();
        setUpToolBar();
        setToolbarListener();
        loadProgessBar();
        initWebView("https://www.cnbc.com/2021/06/12/bitcoin-taproot-upgrade-what-it-means.html");
        setUpButtonListeners();
    }

    private void setUpViews(){
        //back    = (Button)  findViewById(R.id.backButton);
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        toolbar = (Toolbar) findViewById(R.id.siteOfNews);
    }

    private void loadProgessBar(){
        progressBar.setMax(100);
        progressBar.setProgress(1);
    }

    private void setUpButtonListeners(){
        /*back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
    }

    private void setUpToolBar() {
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);

        String url = "https://www.cnbc.com/2021/06/12/bitcoin-taproot-upgrade-what-it-means.html";
        String dotCom = ".com";
        String www = "www.";

        int indexBeg = url.indexOf(www) + www.length();
        int indexEnd = url.indexOf(dotCom) + dotCom.length();
        toolbar.setTitle(url.substring(indexBeg, indexEnd));

        setSupportActionBar(toolbar);
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