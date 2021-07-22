package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tsquared.Models.DiscoverImageModel;
import com.example.tsquared.R;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.ArrayList;

public class DiscoverViewPagerAdapter extends RecyclerView.Adapter<DiscoverViewPagerAdapter.DiscoverImageViewHolder>{

    private ArrayList<DiscoverImageModel> mArrayList;
    private Context mContext;
    private OnImageClickListener onImageClickListener;

    public DiscoverViewPagerAdapter(ArrayList<DiscoverImageModel> mArrayList, Context mContext, OnImageClickListener onImageClickListener){
        this.mArrayList = mArrayList;
        this.mContext   = mContext;
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public DiscoverImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_card_layout, parent, false);
        return new DiscoverImageViewHolder(view, onImageClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverImageViewHolder holder, int position) {
        DiscoverImageModel discoverImageModel = mArrayList.get(position);
        Glide.with(mContext)
                .load(discoverImageModel.getImageURL())
                .centerCrop()
                .into(holder.imageView);
        holder.publisher.setText(discoverImageModel.getPublisherName());
        holder.articleDescription.setText(discoverImageModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public static class DiscoverImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private KenBurnsView imageView;
        private TextView  publisher;
        private TextView  articleDescription;
        private RelativeLayout relativeLayout;
        OnImageClickListener onImageClickListener;

        public DiscoverImageViewHolder(@NonNull View view, OnImageClickListener onImageClickListener){
            super(view);
            imageView = (KenBurnsView) view.findViewById(R.id.image);
            publisher = (TextView)     view.findViewById(R.id.publisher);
            articleDescription = (TextView) view.findViewById(R.id.descriptionNews);
            relativeLayout     = (RelativeLayout) view.findViewById(R.id.discoverCard);

            this.onImageClickListener = onImageClickListener;
            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onImageClickListener.onImageClick(getLayoutPosition());
        }
    }

    public interface OnImageClickListener{
        void onImageClick(int position);
    }
}
