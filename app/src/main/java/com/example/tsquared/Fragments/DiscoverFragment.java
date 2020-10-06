package com.example.tsquared.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.Activities.DetailActivity;
import com.example.tsquared.Activities.MoreInterestsActivity;
import com.example.tsquared.Activities.MoreNewsActivity;
import com.example.tsquared.Activities.NewsArticleContainer;
import com.example.tsquared.Activities.Settings;
import com.example.tsquared.Adapters.InterestsHorizontalAdapter;
import com.example.tsquared.Adapters.NewsHorizontalScrollAdapter;
import com.example.tsquared.Adapters.PeopleItemAdapter;
import com.example.tsquared.Models.InterestsHorizontalModel;
import com.example.tsquared.Models.NewsHorizontalModel;
import com.example.tsquared.Models.PeopleItemModel;
import com.example.tsquared.Activities.PersonProfile;
import com.example.tsquared.R;
import com.example.tsquared.RecyclerviewListeners.CustomScrollListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class DiscoverFragment extends Fragment
        implements NewsHorizontalScrollAdapter.OnNewsListener, InterestsHorizontalAdapter.OnInterestListener {
    private View view;
    private RecyclerView mainRv;
    private PeopleItemAdapter adapter;
    private ArrayList<PeopleItemModel> mArrayList;
    private RelativeLayout cardView;
    private RelativeLayout discoverCard;

    //private SwipeRefreshLayout swipeContainer;

    private ArrayList<NewsHorizontalModel> newsArrayList;
    private ArrayList<InterestsHorizontalModel> interestsArrayList;
    private RecyclerView newsRecyclerView;
    private RecyclerView interestsRecyclerView;
    private NewsHorizontalScrollAdapter newsHorizontalScrollAdapter;
    private InterestsHorizontalAdapter interestsHorizontalAdapter;
    private ExtendedFloatingActionButton newsMoreFab;
    private ExtendedFloatingActionButton interestsMoreFab;
    private RelativeLayout relativeLayout;
    private NestedScrollView nestedScrollView;
    private LinkPromptBottomSheet bottomSheet;
    private ImageView discoverImg;
    private Handler handler;

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
        loadUpNewsRecyclerView();
        loadUpInterestsRecyclerView();
        moreButtonListener();
        //setUpSwipeContainer();
        //setUpRecyclerView();
        //loadListOfPeople();
        //setUpSwipeListener();

        ViewCompat.setNestedScrollingEnabled(newsRecyclerView, false);
        ViewCompat.setNestedScrollingEnabled(interestsRecyclerView, false);
        return view;
    }

    private void setUpViews() {
        newsRecyclerView       = (RecyclerView) view.findViewById(R.id.news_horizontal_recycler_view);
        interestsRecyclerView  = (RecyclerView) view.findViewById(R.id.interests_recycler_view);
        newsMoreFab            = (ExtendedFloatingActionButton) view.findViewById(R.id.newsHorizontalMoreButton);
        interestsMoreFab       = (ExtendedFloatingActionButton) view.findViewById(R.id.interestsHorizontalMore);
        relativeLayout         = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        nestedScrollView       = (NestedScrollView) view.findViewById(R.id.nestedScrollView                                                                                               );
        cardView               = (RelativeLayout) view.findViewById(R.id.shareLink);
        discoverCard           = (RelativeLayout) view.findViewById(R.id.discoverCard);
        discoverImg            = (ImageView) view.findViewById(R.id.image);
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
                }, 150);
            }
        });
    }

    private void loadUpNewsRecyclerView(){
        fillDummyNewsRecyclerView();
        RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();
        newsHorizontalScrollAdapter = new NewsHorizontalScrollAdapter(newsArrayList, getApplicationContext(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setInitialPrefetchItemCount(10);        // would only want 10 items in the horizontal scroll
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        newsRecyclerView.setRecycledViewPool(sharedPool);
        newsRecyclerView.setHasFixedSize(true);
        newsRecyclerView.setAdapter(newsHorizontalScrollAdapter);
        newsRecyclerView.setNestedScrollingEnabled(false);
        newsRecyclerView.addOnScrollListener(new CustomScrollListener(newsMoreFab));

    }

    private void loadUpInterestsRecyclerView(){
        fillDummyInterestsRecyclerView();
        RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();
        interestsHorizontalAdapter = new InterestsHorizontalAdapter(interestsArrayList, getApplicationContext(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setInitialPrefetchItemCount(10);       // would only want 10 items in the horizontal scroll
        interestsRecyclerView.setLayoutManager(linearLayoutManager);
        interestsRecyclerView.setRecycledViewPool(sharedPool);
        interestsRecyclerView.setHasFixedSize(true);
        interestsRecyclerView.setAdapter(interestsHorizontalAdapter);
        interestsRecyclerView.setNestedScrollingEnabled(false);
        interestsRecyclerView.addOnScrollListener(new CustomScrollListener(interestsMoreFab));
    }

    private void fillDummyNewsRecyclerView(){
        newsArrayList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            newsArrayList.add(new NewsHorizontalModel(R.drawable.blank_profile, "The Dow Jones trends negative after opening with small gains"));
        }
    }

    private void fillDummyInterestsRecyclerView(){
        interestsArrayList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            interestsArrayList.add(new InterestsHorizontalModel(R.drawable.blank_profile, "Computer Science"));
        }
    }

    private void moreButtonListener(){
        newsMoreFab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MoreNewsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        interestsMoreFab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MoreInterestsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void OnInterestClick(int position) {
        // must pass interest id
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), NewsArticleContainer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 150);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void OnNewsClick(int position) {
        // must pass url of article
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
            }
        }, 150);
    }
}