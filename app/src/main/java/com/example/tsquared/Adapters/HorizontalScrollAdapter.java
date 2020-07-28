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
import com.example.tsquared.Models.HorizontalModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class HorizontalScrollAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<HorizontalModel> arrayList;
    private Context mContext;
    private int intValue = 0;

    public HorizontalScrollAdapter(ArrayList<HorizontalModel> arrayList, Context mContext, int intValue){
        this.arrayList = arrayList;
        this.mContext  = mContext;
        this.intValue  = intValue;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
            return new NewsViewHolder(view);
        } else if(viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interests_layout, parent, false);
            return new InterestsViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NewsViewHolder){
            HorizontalModel news = arrayList.get(position);
            NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
            Glide.with(mContext)
                    .load(news.getImage())
                    .into(newsViewHolder.newsImage);
            newsViewHolder.description.setText(news.getDescription());
        }

        if(holder instanceof InterestsViewHolder){
            HorizontalModel news = arrayList.get(position);
            InterestsViewHolder interestsViewHolder = (InterestsViewHolder) holder;
            Glide.with(mContext)
                    .load(news.getImage())
                    .into(interestsViewHolder.interestsImage);
            interestsViewHolder.interestsName.setText(news.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position){
        int returnValue = 0;
        if(intValue == 0){
            returnValue = 0;
        }
        else if(intValue == 1){
            returnValue = 1;
        }
        return returnValue;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{
        private ImageView newsImage;
        private TextView  description;
        public NewsViewHolder(@NonNull View view){
            super(view);
            newsImage   = (ImageView) view.findViewById(R.id.image);
            description = (TextView)  view.findViewById(R.id.descriptionNews);
        }
    }

    public class InterestsViewHolder extends RecyclerView.ViewHolder{
        private ImageView interestsImage;
        private TextView  interestsName;
        public InterestsViewHolder(View view){
            super(view);
            interestsImage = (ImageView) view.findViewById(R.id.interestImage);
            interestsName  = (TextView)  view.findViewById(R.id.interestName);
        }
    }
}