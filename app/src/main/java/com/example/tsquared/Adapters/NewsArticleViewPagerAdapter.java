package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tsquared.Models.NewsArticlesViewModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class NewsArticleViewPagerAdapter extends RecyclerView.Adapter<NewsArticleViewPagerAdapter.NewsViewPagerViewHolder> {

    private ArrayList<NewsArticlesViewModel> mArrayList;
    private Context mContext;

    public NewsArticleViewPagerAdapter(ArrayList<NewsArticlesViewModel> mArrayList, Context mContext){
        this.mArrayList = mArrayList;
        this.mContext   = mContext;
    }


    @NonNull
    @Override
    public NewsViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_article_layout, parent, false);
        return new NewsViewPagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewPagerViewHolder holder, int position) {
        NewsArticlesViewModel newsArticlesViewModel = mArrayList.get(position);
        // it would probably be a wise idea to fully understand the Glide dependency
        Glide.with(mContext)
                .load("https://images.squarespace-cdn.com/content/v1/52a0da60e4b0dfa4e47795de/1535498535340-PVPKE7556TCT3PE81QS1/ke17ZwdGBToddI8pDm48kHJjM-Evnp5g-1kf5Yv15cUUqsxRUqqbr1mOJYKfIPR7LoDQ9mXPOjoJoqy81S2I8N_N4V1vUb5AoIIIbLZhVYxCRW4BPu10St3TBAUQYVKcpWKe3KzaCrFDKPR1a1Ob8xobjReaxMuaKtrvUDoDmPO9EsdBHei1w8jR6w0UZiby/Errigal%2C-autumn-hues-X2.jpg")
                .fitCenter()
                .into(holder.newsArticleImage);

        holder.publisher.setText(newsArticlesViewModel.source);
        holder.title.setText(newsArticlesViewModel.titleOfNewsArticle);
        holder.firstLines.setText(newsArticlesViewModel.firstFewLines);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class NewsViewPagerViewHolder extends RecyclerView.ViewHolder{
        private ImageView newsArticleImage;
        private TextView  publisher;
        private TextView  title;
        private TextView  firstLines;

        public NewsViewPagerViewHolder(@NonNull View view) {
            super(view);
            newsArticleImage = (ImageView) view.findViewById(R.id.imageArticleContainer);
            publisher        = (TextView)  view.findViewById(R.id.source);
            title            = (TextView)  view.findViewById(R.id.title);
            firstLines       = (TextView)  view.findViewById(R.id.firstFewLines);
        }
    }
}
