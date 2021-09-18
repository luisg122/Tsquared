package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tsquared.Models.SliderImage;
import com.example.tsquared.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SliderImagesViewPagerAdapter extends RecyclerView.Adapter<SliderImagesViewPagerAdapter.SliderImageViewHolder> {
    private ArrayList<SliderImage> images;
    private Context context;

    public SliderImagesViewPagerAdapter(ArrayList<SliderImage> images, Context context){
        this.images  = images;
        this.context = context;
    }


    @NonNull
    @Override
    public SliderImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_item, parent, false);
        return new SliderImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderImageViewHolder holder, int position) {
        SliderImage sliderImage = images.get(position);
        Glide.with(context)
                .load(sliderImage.getImageURL())
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class SliderImageViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;
        public SliderImageViewHolder(@NonNull View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.imageSliderItem);
        }
    }
}
