package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.Models.MoreNewsModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> /*implements DiscoverViewPagerAdapter.OnImageClickListener*/{
    private final ArrayList<Object> mArrayList;
    private Context mcontext;

    private String headline;
    private OnMoreNewsListener onMoreNewsListener;

    public NewsAdapter(ArrayList<Object> mArrayList, Context mcontext, OnMoreNewsListener onMoreNewsListener) {
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
        this.onMoreNewsListener = onMoreNewsListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == R.layout.discover_headline){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_headline, parent, false);
            return new HeadlinePrompt(view);
        }

        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            return new MoreNewsViewHolder(view, onMoreNewsListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = mArrayList.get(position);

        if(holder.getItemViewType() == R.layout.discover_headline){
            assert holder instanceof HeadlinePrompt;
            HeadlinePrompt dataHolder = (HeadlinePrompt) holder;
            dataHolder.headlinePrompt.setText(headline);
        }

        else if(holder.getItemViewType() == R.layout.news_item){
            assert holder instanceof MoreNewsViewHolder;
            MoreNewsViewHolder dataHolder = (MoreNewsViewHolder) holder;

            MoreNewsModel moreNewsModel = (MoreNewsModel) item;
            Glide.with(dataHolder.iv_image.getContext())
                    .load(moreNewsModel.getImageURL())
                    .transform(new CenterCrop(), new RoundedCorners(20))
                    .into(dataHolder.iv_image);

            dataHolder.tv_desc.setText(moreNewsModel.getDescription());
        }
    }

    @Override
    public int getItemViewType(int position){

        Object obj = mArrayList.get(position);
        if(position == 0) {
            headline = (String) mArrayList.get(position);
            return R.layout.discover_headline;
        }

        if(obj instanceof MoreNewsModel) return R.layout.news_item;

        return -1;
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    private static class HeadlinePrompt extends RecyclerView.ViewHolder{
        private final TextView headlinePrompt;
        public HeadlinePrompt(View view){
            super(view);
            headlinePrompt = (TextView) view.findViewById(R.id.headlineTV);
        }
    }

    public static class MoreNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView iv_image;
        private final TextView tv_desc;
        private final CardView moreNewsItem;
        private final Button   moreIcon;
        OnMoreNewsListener onMoreNewsListener;

        public MoreNewsViewHolder(@NonNull View view, OnMoreNewsListener onMoreNewsListener) {
            super(view);
            iv_image       = (ImageView) view.findViewById(R.id.imageNewsStaggered);
            tv_desc        = (TextView)  view.findViewById(R.id.textViewStaggered);
            moreNewsItem   = (CardView)  view.findViewById(R.id.moreNewsLayout);
            moreIcon       = (Button)    view.findViewById(R.id.three_dots);

            this.onMoreNewsListener = onMoreNewsListener;
            moreNewsItem.setOnClickListener(this);
            moreIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            
            if(id == R.id.moreNewsLayout)
                onMoreNewsListener.onMoreNewsClick(getLayoutPosition());
            
            else if(id == R.id.three_dots)
                onMoreNewsListener.onMoreIconClick(getLayoutPosition());
        }
    }

    public interface OnMoreNewsListener{
        void onMoreNewsClick(int position);
        void onMoreIconClick(int position);
    }
}