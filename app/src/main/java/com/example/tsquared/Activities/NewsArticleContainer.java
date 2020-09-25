package com.example.tsquared.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tsquared.Adapters.NewsArticleViewPagerAdapter;
import com.example.tsquared.Models.NewsArticlesViewModel;
import com.example.tsquared.R;
import com.example.tsquared.ViewPagerTransition.ZoomOutPageTransformer;

import java.util.ArrayList;

public class NewsArticleContainer extends AppCompatActivity {
    private ViewPager2 viewpager;
    private Toolbar    toolbar;
    private ArrayList<NewsArticlesViewModel> arrayList;
    private NewsArticleViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_article_viewpager);
        setUpViews();
        setUpToolbar();
        setUpViewPager();
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
        viewPagerAdapter = new NewsArticleViewPagerAdapter(arrayList, getApplicationContext());
        viewpager.setAdapter(viewPagerAdapter);
        viewpager.setPageTransformer(new ZoomOutPageTransformer());
    }

    private void setUpDummyData(){
        arrayList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            arrayList.add(new NewsArticlesViewModel("New York Times",
                    "The dow jones has now reversed all of its early gains",
                    "Dow Jones futures along with other major indexes were in the green but have long since reversed course",
                    R.drawable.blank_profile));
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
