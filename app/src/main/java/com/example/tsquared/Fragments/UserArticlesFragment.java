package com.example.tsquared.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tsquared.Activities.NewsWebView;
import com.example.tsquared.Adapters.UserAnswersAdapter;
import com.example.tsquared.Adapters.UserArticlesAdapter;
import com.example.tsquared.Models.UserAnswerModel;
import com.example.tsquared.Models.UserArticleModel;
import com.example.tsquared.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UserArticlesFragment extends Fragment implements UserArticlesAdapter.OnMoreNewsListener{
    private View view;

    private RecyclerView articlesRV;
    private UserArticlesAdapter articlesAdapter;
    private ArrayList<UserArticleModel> mArrayList;

    private int saveClickCounter;
    private Handler handler;

    public UserArticlesFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.user_articles_list, container, false);

        setUpViews();
        initializeHandler();
        setUpRecyclerView();
        return view;
    }

    private void setUpViews(){
        articlesRV = (RecyclerView) view.findViewById(R.id.article_list_rv);
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
    }

    private void setUpRecyclerView(){
        dummyDataSetUp();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        articlesAdapter = new UserArticlesAdapter(mArrayList,  this);
        articlesRV.setLayoutManager(layoutManager);


        articlesRV.setAdapter(articlesAdapter);
    }

    private void dummyDataSetUp(){
        mArrayList = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            mArrayList.add(new UserArticleModel("https://www.cnn.com/2022/01/20/politics/biden-russia-putin-ukraine-incursion/index.html", "https://www.thenation.com/wp-content/uploads/2021/07/biden-executive-order-monopoly-gty.jpg", "Today the dow has suffered a bloodshed when job reporting had turned out to be lower than expected", "Washington Post"));
        }
    }

    @Override
    public void onMoreNewsClick(int position) {
        UserArticleModel moreNewsModel = (UserArticleModel) mArrayList.get(position);
        Intent intent = new Intent(getApplicationContext(), NewsWebView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("articleURL", moreNewsModel.getURL());
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
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
}
