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
import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.Models.MoreNewsModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class MoreNewsAdapter extends RecyclerView.Adapter<MoreNewsAdapter.MoreNewsViewHolder> {
    private final ArrayList<MoreNewsModel> mArrayList;
    private Context mcontext;
    private OnMoreNewsListener onMoreNewsListener;

    public MoreNewsAdapter(ArrayList<MoreNewsModel> mArrayList, Context mcontext, OnMoreNewsListener onMoreNewsListener) {
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
        this.onMoreNewsListener = onMoreNewsListener;
    }

    @NonNull
    @Override
    public MoreNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_more_item, parent, false);
        return new MoreNewsViewHolder(view, onMoreNewsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreNewsViewHolder holder, int position) {
        MoreNewsModel moreNewsModel = mArrayList.get(position);
        Glide.with(mcontext)
                .load(moreNewsModel.getImageURL())
                .into(holder.iv_image);
        holder.tv_desc.setText(moreNewsModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public static class MoreNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView iv_image;
        private final TextView tv_desc;
        private final RelativeLayout moreNewsItem;
        OnMoreNewsListener onMoreNewsListener;

        public MoreNewsViewHolder(@NonNull View view, OnMoreNewsListener onMoreNewsListener) {
            super(view);
            iv_image     = (ImageView) view.findViewById(R.id.imageNewsStaggered);
            tv_desc      = (TextView)  view.findViewById(R.id.textViewStaggered);
            moreNewsItem = (RelativeLayout) view.findViewById(R.id.moreNewsLayout);
            this.onMoreNewsListener = onMoreNewsListener;

            moreNewsItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMoreNewsListener.onMoreNewsClick(getLayoutPosition());
        }
    }

    public interface OnMoreNewsListener{
        void onMoreNewsClick(int position);
    }
}