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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tsquared.Activities.NewsWebView;
import com.example.tsquared.Adapters.NewsAdapter;
import com.example.tsquared.Models.DiscoverImageModel;
import com.example.tsquared.Models.MoreNewsModel;
import com.example.tsquared.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Timer;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NewsFragment extends Fragment
        implements NewsAdapter.OnMoreNewsListener{

    private int saveClickCounter;

    private View view;
    private CardView cardView;
    //private SwipeRefreshLayout swipeContainer;

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private ArrayList<Object> mArrayList;

    private NestedScrollView nestedScrollView;
    private Handler handler;

    private ViewPager2 discoverCardImages;
    private ArrayList<DiscoverImageModel> discoverImages;
    private Timer timer;
    private int currentPosition = 0;

    RequestParams params;
    AsyncHttpClient client;
    String URL = "http://207.237.59.117:8080/TSquared/platform?todo=showPeople";


    public NewsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_item_list, container, false);
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
        newsRecyclerView  = (RecyclerView)  view.findViewById(R.id.newsRV);
        // discoverCardImages     = (ViewPager2)       view.findViewById(R.id.discoverCardVP);
        // nestedScrollView       = (NestedScrollView) view.findViewById(R.id.nestedScrollView                                                                                               );
        // cardView               = (CardView)   view.findViewById(R.id.shareLink);
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpRecyclerView(){
        dummyDataSetUp();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        newsAdapter = new NewsAdapter(mArrayList, getApplicationContext(), this);
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

        // headline prompt
        mArrayList.add("All articles");

        // lastly, setup the data for today's headlines
        mArrayList.add(new MoreNewsModel("https://www.cnbc.com/2021/06/12/bitcoin-taproot-upgrade-what-it-means.html", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Washington Post"));
        mArrayList.add(new MoreNewsModel("https://iamalexmathers.medium.com/14-things-i-wish-i-knew-at-25-now-that-im-38-b9796e9ba574", "https://miro.medium.com/max/1400/1*UC4VIARbLk5E6nnghg-IAA.jpeg", "14 things I wish I knew at 25 (now that I’m 38)", "Medium"));
        mArrayList.add(new MoreNewsModel("https://engineering.linkedin.com/blog/2022/linkedin-s-graphql-journey-for-integrations-and-partnerships", "https://engineering.linkedin.com/content/dam/engineering/site-assets/images/blog/posts/2022/10/graphql/GraphQLimage1.png", "LinkedIn Engineering", "Linkedin Blog"));
        mArrayList.add(new MoreNewsModel("https://abcnews.go.com/Politics/psaki-harris-refute-bidens-suggestion-2022-election-results/story?id=82373409", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3SNUtxps-1p637RF_g3IguGd5paXVVvjk7A&usqp=CAU", "The Dow Jones trends negative after opening with small gains", "New York Times"));
        mArrayList.add(new MoreNewsModel("https://www.theguardian.com/culture/2022/jan/20/stephen-colbert-donald-trump-late-night-tv", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTW8-rM0YtkaGvM2qOMlf3j14jYmlL-rbe3Rw&usqp=CAU", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Axios"));
        mArrayList.add(new MoreNewsModel("https://www.cnn.com/2022/01/20/politics/biden-russia-putin-ukraine-incursion/index.html", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "The Dow Jones trends negative after opening with small gains", "Washington Post"));
        mArrayList.add(new MoreNewsModel("https://www.foxnews.com/world/volcano-explosion-near-tonga-left-many-deaf-during-evacuation-witness-says", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3SNUtxps-1p637RF_g3IguGd5paXVVvjk7A&usqp=CAU", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "New York Times"));
        mArrayList.add(new MoreNewsModel("https://www.cnbc.com/2022/01/20/shares-of-autonomous-driving-tech-company-luminar-surge-on-mercedes-benz-deal-.html", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTW8-rM0YtkaGvM2qOMlf3j14jYmlL-rbe3Rw&usqp=CAU", "The Dow Jones trends negative after opening with small gains", "Axios"));
        mArrayList.add(new MoreNewsModel("https://www.cnn.com/2022/01/20/politics/zelensky-biden-ukraine-incursions/index.html", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Washington Post"));
        mArrayList.add(new MoreNewsModel("https://www.theverge.com/2022/1/19/22891046/google-play-games-android-games-windows-pc-beta-launch", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3SNUtxps-1p637RF_g3IguGd5paXVVvjk7A&usqp=CAU", "The Dow Jones trends negative after opening with small gains", "New York Times"));
        mArrayList.add(new MoreNewsModel("https://www.bbc.com/news/world-60060047", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTW8-rM0YtkaGvM2qOMlf3j14jYmlL-rbe3Rw&usqp=CAU", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Axios"));
        mArrayList.add(new MoreNewsModel("https://abcnews.go.com/International/19-year-break-record-youngest-woman-fly-solo/story?id=82312020", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "The Dow Jones trends negative after opening with small gains", "Washington Post"));
        mArrayList.add(new MoreNewsModel("https://news.yahoo.com/uae-says-missiles-drones-used-134607040.html", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3SNUtxps-1p637RF_g3IguGd5paXVVvjk7A&usqp=CAU", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "New York Times"));
        mArrayList.add(new MoreNewsModel("https://www.androidcentral.com/how-does-5g-pose-threat-airline-industry", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTW8-rM0YtkaGvM2qOMlf3j14jYmlL-rbe3Rw&usqp=CAU", "The Dow Jones trends negative after opening with small gains", "Axios"));
        mArrayList.add(new MoreNewsModel("https://www.smithsonianmag.com/history/new-nicer-nero-history-roman-emperor-180975776/", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Washington Post"));
        mArrayList.add(new MoreNewsModel("https://www.theverge.com/2022/1/19/22891256/halo-infinite-multiplayer-big-team-battle-patch-update-problems", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3SNUtxps-1p637RF_g3IguGd5paXVVvjk7A&usqp=CAU", "The Dow Jones trends negative after opening with small gains", "New York Times"));
        mArrayList.add(new MoreNewsModel("https://www.macrumors.com/2022/01/19/iphone-se-3-april-launch-rumor/", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTW8-rM0YtkaGvM2qOMlf3j14jYmlL-rbe3Rw&usqp=CAU", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Axios"));
        mArrayList.add(new MoreNewsModel("https://www.polygon.com/22891406/wordle-mobile-app-store-ios-creator-charity-donate", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "The Dow Jones trends negative after opening with small gains", "New York Times"));
    }


    /*@Override
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
    }*/

    @Override
    public void onMoreNewsClick(int position) {
        Object obj = mArrayList.get(position);

        if(obj instanceof MoreNewsModel) {
            MoreNewsModel moreNewsModel = (MoreNewsModel) obj;
            Intent intent = new Intent(getApplicationContext(), NewsWebView.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("articleURL", moreNewsModel.getURL());
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
        }
    }

    @Override
    public void onMoreIconClick(int position) {
        if(saveClickCounter++ == 0){
            MoreOptionsArticles bottomSheet = new MoreOptionsArticles();
            bottomSheet.show(requireActivity().getSupportFragmentManager(), bottomSheet.getTag());

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveClickCounter = 0;
                }
            }, 1000);
        }
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