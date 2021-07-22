package com.example.tsquared.Adapters;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tsquared.Models.NewsArticlesViewModel;
import com.example.tsquared.R;
import com.example.tsquared.Utils.BlurTransformation;

import java.util.ArrayList;

public class NewsArticleViewPagerAdapter extends RecyclerView.Adapter<NewsArticleViewPagerAdapter.NewsViewPagerViewHolder> {

    private ArrayList<NewsArticlesViewModel> mArrayList;
    private Context mContext;
    private OnNewsClickListener onNewsClickListener;

    public NewsArticleViewPagerAdapter(ArrayList<NewsArticlesViewModel> mArrayList, Context mContext, OnNewsClickListener onNewsClickListener){
        this.mArrayList = mArrayList;
        this.mContext   = mContext;
        this.onNewsClickListener = onNewsClickListener;
    }


    @NonNull
    @Override
    public NewsViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_article_layout, parent, false);
        return new NewsViewPagerViewHolder(view, onNewsClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewPagerViewHolder holder, int position) {
        NewsArticlesViewModel newsArticlesViewModel = mArrayList.get(position);
        // it would probably be a wise idea to fully understand the Glide dependency
        Glide.with(mContext)
                .load(newsArticlesViewModel.getImageOfNewsArticle())
                .fitCenter()
                .into(holder.newsArticleImage);

        Glide.with(mContext)
                .load(newsArticlesViewModel.getImageOfNewsArticle())
                .transform(new BlurTransformation(mContext))
                .into(holder.blurredImage);


        holder.publisher.setText(newsArticlesViewModel.source);
        holder.title.setText(newsArticlesViewModel.titleOfNewsArticle);
        holder.firstLines.setText(newsArticlesViewModel.firstFewLines);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class NewsViewPagerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView newsArticleImage;
        private ImageView blurredImage;
        private TextView  publisher;
        private TextView  title;
        private TextView  firstLines;
        private CardView  readMore;
        OnNewsClickListener onNewsClickListener;

        public NewsViewPagerViewHolder(@NonNull View view, OnNewsClickListener onNewsClickListener) {
            super(view);
            newsArticleImage = (ImageView) view.findViewById(R.id.imageArticleContainer);
            blurredImage     = (ImageView) view.findViewById(R.id.blurredImage);
            publisher        = (TextView)  view.findViewById(R.id.source);
            title            = (TextView)  view.findViewById(R.id.title);
            firstLines       = (TextView)  view.findViewById(R.id.firstFewLines);
            readMore         = (CardView)  view.findViewById(R.id.clickToReadMore);

            this.onNewsClickListener = onNewsClickListener;

            readMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNewsClickListener.onImageClick(getAdapterPosition());
        }
    }

    public interface OnNewsClickListener{
        void onImageClick(int position);
    }
}
