package com.example.tsquared.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tsquared.Adapters.NewsArticleViewPagerAdapter;
import com.example.tsquared.Adapters.NewsHorizontalScrollAdapter;
import com.example.tsquared.Models.NewsArticlesViewModel;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;
import com.example.tsquared.ViewPagerTransition.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NewsArticleContainer extends AppCompatActivity implements NewsArticleViewPagerAdapter.OnNewsClickListener {
    private ViewPager2 viewpager;
    private Toolbar    toolbar;
    private ArrayList<NewsArticlesViewModel> arrayList;
    private NewsArticleViewPagerAdapter viewPagerAdapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_article_viewpager);
        initializeHandler();
        setUpViews();
        setUpToolbar();
        setUpViewPager();
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
    }

    private void setUpViews(){
        viewpager = findViewById(R.id.newsViewPager);
        toolbar   = findViewById(R.id.toolbar);
    }

    private void setUpToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpViewPager(){
        setUpDummyData();
        viewPagerAdapter = new NewsArticleViewPagerAdapter(arrayList, getApplicationContext(), this);
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setPageTransformer(new ZoomOutPageTransformer());
    }

    private void setUpDummyData(){
        arrayList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            arrayList.add(new NewsArticlesViewModel(" ", "New York Times",
                    "The dow jones has now reversed all of its early gains",
                    "Dow Jones futures along with other major indexes were in the green but have long since reversed course",
                    " "));
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onImageClick(final int position) {
        // must pass url of article
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), NewsWebView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("articleURL", arrayList.get(position).getURL());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
            }
        }, 150);
    }
}
