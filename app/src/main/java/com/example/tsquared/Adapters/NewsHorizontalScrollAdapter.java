package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.Models.NewsHorizontalModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class NewsHorizontalScrollAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NewsHorizontalModel> arrayList;
    private Context mContext;
    private OnNewsListener onNews;
    private NewsMoreClickListener clickButton;

    public NewsHorizontalScrollAdapter(ArrayList<NewsHorizontalModel> arrayList, Context mContext, OnNewsListener onNews, NewsMoreClickListener clickButton){
        this.arrayList   = arrayList;
        this.mContext    = mContext;
        this.onNews      = onNews;
        this.clickButton = clickButton;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == R.layout.news_layout) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
            return new NewsViewHolder(view, onNews);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_button_large_layout, parent, false);
            return new NoViewHolder(view, clickButton);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NewsViewHolder) {
            NewsHorizontalModel news = arrayList.get(position);
            final NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
            Glide.with(mContext)
                    .load("https://images.squarespace-cdn.com/content/v1/52a0da60e4b0dfa4e47795de/1535498535340-PVPKE7556TCT3PE81QS1/ke17ZwdGBToddI8pDm48kHJjM-Evnp5g-1kf5Yv15cUUqsxRUqqbr1mOJYKfIPR7LoDQ9mXPOjoJoqy81S2I8N_N4V1vUb5AoIIIbLZhVYxCRW4BPu10St3TBAUQYVKcpWKe3KzaCrFDKPR1a1Ob8xobjReaxMuaKtrvUDoDmPO9EsdBHei1w8jR6w0UZiby/Errigal%2C-autumn-hues-X2.jpg")
                    .transform(new CenterCrop(), new RoundedCorners(30))
                    .into(newsViewHolder.newsImage);
            newsViewHolder.description.setText(news.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    @Override
    public int getItemViewType(int position){
        return (position == arrayList.size()) ? R.layout.more_button_large_layout : R.layout.news_layout;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView newsImage;
        private TextView  description;
        private RelativeLayout newsLayout;
        OnNewsListener onNoteListener;

        public NewsViewHolder(@NonNull View view, OnNewsListener onNoteListener){
            super(view);
            newsImage   = (ImageView) view.findViewById(R.id.newsImage);
            description = (TextView)  view.findViewById(R.id.descriptionNews);
            this.onNoteListener = onNoteListener;

            newsLayout = view.findViewById(R.id.newsLayout);
            newsLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.OnNewsClick(getAdapterPosition());
        }
    }

    public interface OnNewsListener{
        void OnNewsClick(int position);
    }

    public class NoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final Button moreButton;
        NewsMoreClickListener onButtonClickListener;
        public NoViewHolder(View view, NewsMoreClickListener onButtonClickListener){
            super(view);
            moreButton = (Button) view.findViewById(R.id.moreButton);
            this.onButtonClickListener = onButtonClickListener;

            moreButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onButtonClickListener.OnNewsMoreButtonClick();
        }
    }

    public interface NewsMoreClickListener{
        void OnNewsMoreButtonClick();
    }
}