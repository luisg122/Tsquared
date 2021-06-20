package com.example.tsquared.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tsquared.Activities.MoreInterestsActivity;
import com.example.tsquared.Activities.MoreNewsActivity;
import com.example.tsquared.Activities.NewsArticleContainer;
import com.example.tsquared.Activities.NewsWebView;
import com.example.tsquared.Adapters.DiscoverViewPagerAdapter;
import com.example.tsquared.Adapters.InterestsHorizontalAdapter;
import com.example.tsquared.Adapters.NewsHorizontalScrollAdapter;
import com.example.tsquared.Adapters.PeopleItemAdapter;
import com.example.tsquared.Models.DiscoverImageModel;
import com.example.tsquared.Models.InterestsHorizontalModel;
import com.example.tsquared.Models.NewsHorizontalModel;
import com.example.tsquared.Models.PeopleItemModel;
import com.example.tsquared.R;
import com.example.tsquared.ViewPagerTransition.DepthPageTransformer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.FacebookSdk.getApplicationContext;

public class DiscoverFragment extends Fragment
        implements NewsHorizontalScrollAdapter.OnNewsListener, InterestsHorizontalAdapter.OnInterestListener,
                   NewsHorizontalScrollAdapter.NewsMoreClickListener, InterestsHorizontalAdapter.InterestsMoreClickListener,
                   DiscoverViewPagerAdapter.OnImageClickListener{

    private View view;
    private RecyclerView mainRv;
    private PeopleItemAdapter adapter;
    private ArrayList<PeopleItemModel> mArrayList;
    private CardView cardView;
    //private SwipeRefreshLayout swipeContainer;

    private ArrayList<NewsHorizontalModel> newsArrayList;
    private ArrayList<InterestsHorizontalModel> interestsArrayList;
    private RecyclerView newsRecyclerView;
    private RecyclerView interestsRecyclerView;
    private NewsHorizontalScrollAdapter newsHorizontalScrollAdapter;
    private InterestsHorizontalAdapter interestsHorizontalAdapter;
    private NestedScrollView nestedScrollView;
    private LinkPromptBottomSheet bottomSheet;
    private Handler handler;

    private ViewPager2 discoverCardImages;
    private ArrayList<DiscoverImageModel> discoverImages;
    private DiscoverViewPagerAdapter discoverViewPagerAdapter;
    private Timer timer;
    private int currentPosition = 0;

    RequestParams params;
    AsyncHttpClient client;
    String URL = "http://207.237.59.117:8080/TSquared/platform?todo=showPeople";

    public DiscoverFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.discover_list, container, false);
        setUpViews();
        initializeHandler();
        invokeLinkBottomSheet();
        setUpDiscoverViewPager();
        loadUpNewsRecyclerView();
        loadUpInterestsRecyclerView();
        //setUpSwipeContainer();
        //setUpRecyclerView();
        //loadListOfPeople();
        //setUpSwipeListener();

        ViewCompat.setNestedScrollingEnabled(newsRecyclerView, false);
        ViewCompat.setNestedScrollingEnabled(interestsRecyclerView, false);
        return view;
    }

    private void setUpViews() {
        newsRecyclerView       = (RecyclerView)     view.findViewById(R.id.news_horizontal_recycler_view);
        interestsRecyclerView  = (RecyclerView)     view.findViewById(R.id.interests_recycler_view);
        discoverCardImages     = (ViewPager2)       view.findViewById(R.id.discoverCardVP);
        nestedScrollView       = (NestedScrollView) view.findViewById(R.id.nestedScrollView                                                                                               );
        cardView               = (CardView)   view.findViewById(R.id.shareLink);
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
    }

    private void invokeLinkBottomSheet(){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheet == null || !bottomSheet.isVisible()){
                    bottomSheet = new LinkPromptBottomSheet();
                    assert getFragmentManager() != null;
                    bottomSheet.show(getFragmentManager(), "TAG");
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpDiscoverViewPager() {
        dummyDiscoverImages();
        discoverViewPagerAdapter = new DiscoverViewPagerAdapter(discoverImages, getApplicationContext(), this);
        discoverCardImages.setAdapter(discoverViewPagerAdapter);
        discoverCardImages.setPageTransformer(new DepthPageTransformer());
        discoverCardImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(slideRunnable);
                handler.postDelayed(slideRunnable, 8000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private Runnable slideRunnable = new Runnable(){
        @Override
        public void run() {
            if(discoverCardImages.getCurrentItem() + 1 == discoverImages.size()){
                discoverCardImages.setCurrentItem(0);
            }
            discoverCardImages.setCurrentItem(discoverCardImages.getCurrentItem() + 1, true);
        }
    };

    private void dummyDiscoverImages(){
        discoverImages = new ArrayList<>();
        DiscoverImageModel discoverImageModel;
        discoverImageModel = new DiscoverImageModel("https://p.bigstockphoto.com/GeFvQkBbSLaMdpKXF1Zv_bigstock-Aerial-View-Of-Blue-Lakes-And--227291596.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://www.freewalldownload.com/amazing-river-wallpaper/picture-green-river-desktop-wallpapers-images-hd-download.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://p.bigstockphoto.com/GeFvQkBbSLaMdpKXF1Zv_bigstock-Aerial-View-Of-Blue-Lakes-And--227291596.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://www.freewalldownload.com/amazing-river-wallpaper/picture-green-river-desktop-wallpapers-images-hd-download.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://p.bigstockphoto.com/GeFvQkBbSLaMdpKXF1Zv_bigstock-Aerial-View-Of-Blue-Lakes-And--227291596.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://www.freewalldownload.com/amazing-river-wallpaper/picture-green-river-desktop-wallpapers-images-hd-download.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://p.bigstockphoto.com/GeFvQkBbSLaMdpKXF1Zv_bigstock-Aerial-View-Of-Blue-Lakes-And--227291596.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://www.freewalldownload.com/amazing-river-wallpaper/picture-green-river-desktop-wallpapers-images-hd-download.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://p.bigstockphoto.com/GeFvQkBbSLaMdpKXF1Zv_bigstock-Aerial-View-Of-Blue-Lakes-And--227291596.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://www.freewalldownload.com/amazing-river-wallpaper/picture-green-river-desktop-wallpapers-images-hd-download.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
    }

    private void loadUpNewsRecyclerView(){
        fillDummyNewsRecyclerView();
        RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();
        newsHorizontalScrollAdapter = new NewsHorizontalScrollAdapter(newsArrayList, getApplicationContext(), this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setInitialPrefetchItemCount(10);        // would only want 10 items in the horizontal scroll
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        newsRecyclerView.setRecycledViewPool(sharedPool);
        newsRecyclerView.setHasFixedSize(true);
        newsRecyclerView.setAdapter(newsHorizontalScrollAdapter);
        newsRecyclerView.setNestedScrollingEnabled(false);

    }

    private void loadUpInterestsRecyclerView(){
        fillDummyInterestsRecyclerView();
        RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();
        interestsHorizontalAdapter = new InterestsHorizontalAdapter(interestsArrayList, getApplicationContext(), this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setInitialPrefetchItemCount(10);       // would only want 10 items in the horizontal scroll
        interestsRecyclerView.setLayoutManager(linearLayoutManager);
        interestsRecyclerView.setRecycledViewPool(sharedPool);
        interestsRecyclerView.setHasFixedSize(true);
        interestsRecyclerView.setAdapter(interestsHorizontalAdapter);
        interestsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void fillDummyNewsRecyclerView(){
        newsArrayList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            newsArrayList.add(new NewsHorizontalModel(" "," ", "The Dow Jones trends negative after opening with small gains"));
        }
    }

    private void fillDummyInterestsRecyclerView(){
        interestsArrayList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            interestsArrayList.add(new InterestsHorizontalModel(1, " ", "Computer Science"));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void OnInterestClick(final int position) {
        // must pass interest id
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), NewsArticleContainer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("interestID", interestsArrayList.get(position).getInterestID());
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 150);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void OnNewsClick(final int position) {
        // must pass url of article
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), NewsWebView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("articleURL", newsArrayList.get(position).getURL());
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
            }
        }, 150);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void OnNewsMoreButtonClick() {
        Intent intent = new Intent(getApplicationContext(), MoreNewsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void OnInterestsMoreButtonClick() {
        Intent intent = new Intent(getApplicationContext(), MoreInterestsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onImageClick(final int position) {
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), NewsArticleContainer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("position", position);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 150);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}

/*
*

        Glide.with(getApplicationContext())
                .load("https://i.pinimg.com/originals/08/07/95/08079583048f368002280805a5221b8b.jpg")
                .into(discoverImg);

        discoverCard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), NewsArticleContainer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }, 100);
            }
        });
* */