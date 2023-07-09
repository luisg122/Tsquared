package com.example.tsquared.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.tsquared.CustomWebView;
import com.example.tsquared.Fragments.MoreOptionsArticles;
import com.example.tsquared.Fragments.TextFormatBottomSheet;
import com.example.tsquared.Fragments.TopicsBottomSheet;
import com.example.tsquared.NewsWebView.EntryDetailsView;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class NewsWebView extends AppCompatActivity {
    private int saveClickCounter;
    private Handler handler;

    private EntryDetailsView webView;
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
        //setUpButtonListeners();
    }

    private void getUrlFromIntent(){
        Intent intent = getIntent();

        url = intent.getStringExtra("articleURL");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpViews(){
        webView = (EntryDetailsView) findViewById(R.id.entry_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        /*appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        bottomBar = (CardView) findViewById(R.id.buttonsSection);
        moreOptions = (Button) findViewById(R.id.more);*/
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

    private void setUpToolBar() {
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
    }

    private void setToolbarListener() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(){
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
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

        webView.setEntry(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.news_webview_menu, menu);

        // look into why this piece of code cannot be move to overriden method onOptionsItemSelected
        // menu item that makes use of actionLayout does not work in onOptionsItemSelected when clicked (why?)
        MenuItem item = menu.findItem(R.id.textFormat);
        View view = item.getActionView();
        FloatingActionButton textFormatAccessibility = view.findViewById(R.id.textAccessibility);
        textFormatAccessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveClickCounter++ == 0){
                    TextFormatBottomSheet bottomSheet = new TextFormatBottomSheet();
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

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.more){
            if(saveClickCounter++ == 0){
                TopicsBottomSheet bottomSheet = new TopicsBottomSheet();
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        saveClickCounter = 0;
                    }
                }, 1000);
            }
        }

        return super.onOptionsItemSelected(item);
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