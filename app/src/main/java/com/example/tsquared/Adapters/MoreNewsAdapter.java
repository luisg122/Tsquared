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
                .load("https://images.squarespace-cdn.com/content/v1/52a0da60e4b0dfa4e47795de/1535498535340-PVPKE7556TCT3PE81QS1/ke17ZwdGBToddI8pDm48kHJjM-Evnp5g-1kf5Yv15cUUqsxRUqqbr1mOJYKfIPR7LoDQ9mXPOjoJoqy81S2I8N_N4V1vUb5AoIIIbLZhVYxCRW4BPu10St3TBAUQYVKcpWKe3KzaCrFDKPR1a1Ob8xobjReaxMuaKtrvUDoDmPO9EsdBHei1w8jR6w0UZiby/Errigal%2C-autumn-hues-X2.jpg")
                .transform(new CenterCrop(), new RoundedCorners(50))
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
            onMoreNewsListener.onMoreNewsClick();
        }
    }

    public interface OnMoreNewsListener{
        void onMoreNewsClick();
    }
}