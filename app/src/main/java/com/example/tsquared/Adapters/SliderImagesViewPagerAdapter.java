package com.example.tsquared.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tsquared.Models.SliderImage;
import com.example.tsquared.R;

import java.io.InputStream;
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

        Glide.with(holder.imageView.getContext()).
                load(sliderImage.getImageURL()).
                into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class SliderImageViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;

        public SliderImageViewHolder(@NonNull View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.imageSliderItem);
        }
    }

    /*private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        SubsamplingScaleImageView bmImage;

        public DownloadImageTask(SubsamplingScaleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImage(ImageSource.bitmap(result));
        }
    }*/
}
