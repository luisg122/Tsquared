package com.example.tsquared.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.Models.UserArticleModel;
import com.example.tsquared.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class UserArticlesAdapter extends RecyclerView.Adapter<UserArticlesAdapter.UserArticleVH> {
    private final ArrayList<UserArticleModel> mArrayList;

    private OnMoreNewsListener onMoreNewsListener;

    public UserArticlesAdapter(ArrayList<UserArticleModel> mArrayList, OnMoreNewsListener onMoreNewsListener){
        this.mArrayList = mArrayList;

        this.onMoreNewsListener = onMoreNewsListener;
    }

    @NonNull
    @Override
    public UserArticleVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);

        return new UserArticleVH(view, onMoreNewsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserArticleVH holder, int position) {
        UserArticleModel article = (UserArticleModel) mArrayList.get(position);

        Glide.with(holder.iv_image.getContext())
                .load(article.getImageURL())
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(holder.iv_image);

        holder.tv_desc.setText(article.getDescription());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public static class UserArticleVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView iv_image;
        private final TextView tv_desc;
        private final CardView moreNewsItem;
        private final Button moreIcon;


        OnMoreNewsListener onMoreNewsListener;
        public UserArticleVH(View view, OnMoreNewsListener onMoreNewsListener){
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
        public void onClick(View view) {
            int id = view.getId();

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
