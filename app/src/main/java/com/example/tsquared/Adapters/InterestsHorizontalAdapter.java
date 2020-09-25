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
import com.bumptech.glide.request.RequestOptions;
import com.example.tsquared.Glide_Module.GlideApp;
import com.example.tsquared.Models.InterestsHorizontalModel;
import com.example.tsquared.Models.InterestsModel;
import com.example.tsquared.Models.NewsHorizontalModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class InterestsHorizontalAdapter extends RecyclerView.Adapter<InterestsHorizontalAdapter.InterestsViewHolder> {
    private ArrayList<InterestsHorizontalModel> mArrayList;
    private Context mContext;
    private OnInterestListener onInterestListener;


    public InterestsHorizontalAdapter(ArrayList<InterestsHorizontalModel> mArrayList, Context mContext, OnInterestListener onInterestListener){
        this.mArrayList = mArrayList;
        this.mContext   = mContext;
        this.onInterestListener = onInterestListener;
    }

    @NonNull
    @Override
    public InterestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interests_layout, parent, false);
        return new InterestsViewHolder(view, onInterestListener);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestsViewHolder holder, int position) {
        InterestsHorizontalModel interests = mArrayList.get(position);
        Glide.with(mContext)
                .load("https://images.squarespace-cdn.com/content/v1/52a0da60e4b0dfa4e47795de/1535498535340-PVPKE7556TCT3PE81QS1/ke17ZwdGBToddI8pDm48kHJjM-Evnp5g-1kf5Yv15cUUqsxRUqqbr1mOJYKfIPR7LoDQ9mXPOjoJoqy81S2I8N_N4V1vUb5AoIIIbLZhVYxCRW4BPu10St3TBAUQYVKcpWKe3KzaCrFDKPR1a1Ob8xobjReaxMuaKtrvUDoDmPO9EsdBHei1w8jR6w0UZiby/Errigal%2C-autumn-hues-X2.jpg")
                .transform(new CenterCrop(), new RoundedCorners(50))
                .into(holder.interestsImage);
        holder.interestsName.setText(interests.getDescription());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class InterestsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView interestsImage;
        private TextView interestsName;
        private RelativeLayout interestLayout;
        OnInterestListener onInterestListener;
        public InterestsViewHolder(View view, OnInterestListener onInterestListener){
            super(view);
            interestsImage = (ImageView) view.findViewById(R.id.interestImage);
            interestsName  = (TextView)  view.findViewById(R.id.interestName);
            interestLayout = view.findViewById(R.id.interestsLayout);
            this.onInterestListener = onInterestListener;
            interestLayout.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            onInterestListener.OnInterestClick(getAdapterPosition());
        }
    }

    public interface OnInterestListener{
        void OnInterestClick(int position);
    }
}