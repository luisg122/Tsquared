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
import com.example.tsquared.Models.MoreInterestsModel;
import com.example.tsquared.Models.MoreNewsModel;
import com.example.tsquared.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MoreInterests_Adapter extends RecyclerView.Adapter<MoreInterests_Adapter.MoreInterestsViewHolder> {
    private final ArrayList<MoreInterestsModel> mArrayList;
    private Context mcontext;
    private OnMoreInterestsListener onMoreInterestsListener;

    public MoreInterests_Adapter(ArrayList<MoreInterestsModel> mArrayList, Context mcontext, OnMoreInterestsListener onMoreInterestsListener){
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
        this.onMoreInterestsListener = onMoreInterestsListener;
    }

    @NonNull
    @Override
    public MoreInterestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interests_more_item, parent, false);
        return new MoreInterestsViewHolder(view, onMoreInterestsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreInterestsViewHolder holder, int position) {
        MoreInterestsModel moreInterestsModel = mArrayList.get(position);
        Glide.with(mcontext)
                .load("https://images.squarespace-cdn.com/content/v1/52a0da60e4b0dfa4e47795de/1535498535340-PVPKE7556TCT3PE81QS1/ke17ZwdGBToddI8pDm48kHJjM-Evnp5g-1kf5Yv15cUUqsxRUqqbr1mOJYKfIPR7LoDQ9mXPOjoJoqy81S2I8N_N4V1vUb5AoIIIbLZhVYxCRW4BPu10St3TBAUQYVKcpWKe3KzaCrFDKPR1a1Ob8xobjReaxMuaKtrvUDoDmPO9EsdBHei1w8jR6w0UZiby/Errigal%2C-autumn-hues-X2.jpg")
                .transform(new CenterCrop(), new RoundedCorners(50))
                .into(holder.interestsImage);
        holder.interestsName.setText(moreInterestsModel.getInterestsName());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class MoreInterestsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView interestsImage;
        private final TextView  interestsName;
        private final RelativeLayout interestsItem;
        OnMoreInterestsListener onMoreInterestsListener;
        public MoreInterestsViewHolder(@NonNull View view, OnMoreInterestsListener onMoreInterestsListener) {
            super(view);
            interestsImage = (ImageView) view.findViewById(R.id.interests_image);
            interestsName  = (TextView)  view.findViewById(R.id.interests_name);
            interestsItem  = (RelativeLayout) view.findViewById(R.id.moreInterestsLayout);
            this.onMoreInterestsListener = onMoreInterestsListener;

            interestsItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMoreInterestsListener.onMoreInterestsClick();
        }
    }

    public interface OnMoreInterestsListener{
        void onMoreInterestsClick();
    }
}
