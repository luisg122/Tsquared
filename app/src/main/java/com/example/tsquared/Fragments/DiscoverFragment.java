package com.example.tsquared.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tsquared.Activities.DrawerActivity;
import com.example.tsquared.Activities.MoreInterestsActivity;
import com.example.tsquared.Activities.MoreNewsActivity;
import com.example.tsquared.Activities.NewsArticleContainer;
import com.example.tsquared.Activities.NewsWebView;
import com.example.tsquared.Adapters.DiscoverViewPagerAdapter;
import com.example.tsquared.Adapters.InterestsHorizontalAdapter;
import com.example.tsquared.Adapters.MoreNewsAdapter;
import com.example.tsquared.Adapters.NewsHorizontalScrollAdapter;
import com.example.tsquared.Adapters.PeopleItemAdapter;
import com.example.tsquared.Models.DiscoverImageModel;
import com.example.tsquared.Models.InterestsHorizontalModel;
import com.example.tsquared.Models.MoreNewsModel;
import com.example.tsquared.Models.NewsHorizontalModel;
import com.example.tsquared.Models.PeopleItemModel;
import com.example.tsquared.R;
import com.example.tsquared.ViewPagerTransition.DepthPageTransformer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.FacebookSdk.getApplicationContext;

public class DiscoverFragment extends Fragment
        implements DiscoverViewPagerAdapter.OnImageClickListener, MoreNewsAdapter.OnMoreNewsListener{

    private View view;
    private CardView cardView;
    //private SwipeRefreshLayout swipeContainer;

    private RecyclerView newsRecyclerView;
    private MoreNewsAdapter newsAdapter;
    private ArrayList<Object> mArrayList;

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

    public DiscoverFragment() {}

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
        //invokeLinkBottomSheet();
        //setUpDiscoverViewPager();
        setUpRecyclerView();
        //setUpSwipeContainer();
        //setUpRecyclerView();
        //loadListOfPeople();
        //setUpSwipeListener();

        return view;
    }

    private void setUpViews() {
        newsRecyclerView       = (RecyclerView)     view.findViewById(R.id.newsRV);
        // discoverCardImages     = (ViewPager2)       view.findViewById(R.id.discoverCardVP);
        // nestedScrollView       = (NestedScrollView) view.findViewById(R.id.nestedScrollView                                                                                               );
        // cardView               = (CardView)   view.findViewById(R.id.shareLink);
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
    }

    /*
    private void invokeLinkBottomSheet(){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheet == null || !bottomSheet.isVisible()){
                    bottomSheet = new LinkPromptBottomSheet();
                    bottomSheet.show(requireActivity().getSupportFragmentManager(), bottomSheet.getTag());
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
    };*/

    private void dummyDiscoverImages(){
        discoverImages = new ArrayList<>();
        DiscoverImageModel discoverImageModel;
        discoverImageModel = new DiscoverImageModel("https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://insights.som.yale.edu/sites/default/files/styles/rectangle_xs/public/insights/background/What%20the%20Plunge%20in%20the%20Stock%20Market%20Means%20for%20Individual%20Investors.jpg?h=d0d46503&itok=vl15_CSn", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://www.reuters.com/resizer/vsRFGKTi9vWULwxZlnZkhbTmaLs=/1200x628/smart/filters:quality(80)/cloudfront-us-east-2.images.arcpublishing.com/reuters/HKYMFSORGZJRHMWZ6YYOZO4MTY.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://insights.som.yale.edu/sites/default/files/styles/rectangle_xs/public/insights/background/What%20the%20Plunge%20in%20the%20Stock%20Market%20Means%20for%20Individual%20Investors.jpg?h=d0d46503&itok=vl15_CSn", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://www.reuters.com/resizer/vsRFGKTi9vWULwxZlnZkhbTmaLs=/1200x628/smart/filters:quality(80)/cloudfront-us-east-2.images.arcpublishing.com/reuters/HKYMFSORGZJRHMWZ6YYOZO4MTY.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://insights.som.yale.edu/sites/default/files/styles/rectangle_xs/public/insights/background/What%20the%20Plunge%20in%20the%20Stock%20Market%20Means%20for%20Individual%20Investors.jpg?h=d0d46503&itok=vl15_CSn", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://www.reuters.com/resizer/vsRFGKTi9vWULwxZlnZkhbTmaLs=/1200x628/smart/filters:quality(80)/cloudfront-us-east-2.images.arcpublishing.com/reuters/HKYMFSORGZJRHMWZ6YYOZO4MTY.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
        discoverImageModel = new DiscoverImageModel("https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "New York Times", "Congress rushes to push hugh relief stimulus package");
        discoverImages.add(discoverImageModel);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpRecyclerView(){
        dummyDataSetUp();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        newsAdapter = new MoreNewsAdapter(mArrayList, getApplicationContext(), this);
        newsRecyclerView.setLayoutManager(layoutManager);

        /*DividerItemDecoration divider = new DividerItemDecoration(newsRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        divider.setDrawable((Objects.requireNonNull(ContextCompat.getDrawable(Objects.requireNonNull(newsRecyclerView.getContext()),
                R.drawable.line_divider_black))));
        newsRecyclerView.addItemDecoration(divider);*/

        newsRecyclerView.setAdapter(newsAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void dummyDataSetUp(){
        mArrayList = new ArrayList<>();

        // setup the share link component
        // mArrayList.add(new LinkPromptBottomSheet());

        // setup the data for the viewpager component
        // dummyDiscoverImages();
        // mArrayList.add(discoverImages);

        // headline prompt
        mArrayList.add("Headlines");

        // lastly, setup the data for today's headlines
        mArrayList.add(new MoreNewsModel(" ", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Washington Post"));
        mArrayList.add(new MoreNewsModel(" ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3SNUtxps-1p637RF_g3IguGd5paXVVvjk7A&usqp=CAU", "The Dow Jones trends negative after opening with small gains", "New York Times"));
        mArrayList.add(new MoreNewsModel(" ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTW8-rM0YtkaGvM2qOMlf3j14jYmlL-rbe3Rw&usqp=CAU", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Axios"));
        mArrayList.add(new MoreNewsModel(" ", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "The Dow Jones trends negative after opening with small gains", "Washington Post"));
        mArrayList.add(new MoreNewsModel(" ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3SNUtxps-1p637RF_g3IguGd5paXVVvjk7A&usqp=CAU", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "New York Times"));
        mArrayList.add(new MoreNewsModel(" ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTW8-rM0YtkaGvM2qOMlf3j14jYmlL-rbe3Rw&usqp=CAU", "The Dow Jones trends negative after opening with small gains", "Axios"));
        mArrayList.add(new MoreNewsModel(" ", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Washington Post"));
        mArrayList.add(new MoreNewsModel(" ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3SNUtxps-1p637RF_g3IguGd5paXVVvjk7A&usqp=CAU", "The Dow Jones trends negative after opening with small gains", "New York Times"));
        mArrayList.add(new MoreNewsModel(" ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTW8-rM0YtkaGvM2qOMlf3j14jYmlL-rbe3Rw&usqp=CAU", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Axios"));
        mArrayList.add(new MoreNewsModel(" ", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "The Dow Jones trends negative after opening with small gains", "Washington Post"));
        mArrayList.add(new MoreNewsModel(" ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3SNUtxps-1p637RF_g3IguGd5paXVVvjk7A&usqp=CAU", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "New York Times"));
        mArrayList.add(new MoreNewsModel(" ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTW8-rM0YtkaGvM2qOMlf3j14jYmlL-rbe3Rw&usqp=CAU", "The Dow Jones trends negative after opening with small gains", "Axios"));
        mArrayList.add(new MoreNewsModel(" ", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Washington Post"));
        mArrayList.add(new MoreNewsModel(" ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3SNUtxps-1p637RF_g3IguGd5paXVVvjk7A&usqp=CAU", "The Dow Jones trends negative after opening with small gains", "New York Times"));
        mArrayList.add(new MoreNewsModel(" ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTW8-rM0YtkaGvM2qOMlf3j14jYmlL-rbe3Rw&usqp=CAU", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Axios"));
        mArrayList.add(new MoreNewsModel(" ", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "The Dow Jones trends negative after opening with small gains", "New York Times"));
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
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 150);
    }

    @Override
    public void shareLinkClick(int position, LinkPromptBottomSheet bottomSheet) {
        if(bottomSheet == null || !bottomSheet.isAdded()){
            bottomSheet = new LinkPromptBottomSheet();
            bottomSheet.show(requireActivity().getSupportFragmentManager(), bottomSheet.getTag());
        }
    }

    @Override
    public void onMoreNewsClick(int position) {
        MoreNewsModel moreNewsModel = (MoreNewsModel) mArrayList.get(position);
        Intent intent = new Intent(getApplicationContext(), NewsWebView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("articleURL", moreNewsModel.getURL());
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
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