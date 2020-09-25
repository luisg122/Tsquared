package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsquared.Models.IdeasItemModel;
import com.example.tsquared.R;

import java.util.ArrayList;


public class IdeasItemAdapter extends RecyclerView.Adapter<IdeasItemAdapter.IdeasViewHolder> {

    private final ArrayList<IdeasItemModel> mArrayList;
    private final Context mContext;
    private final OnIdeaClickListener onIdeaClickListener;

    public IdeasItemAdapter(ArrayList<IdeasItemModel> mArrayList, Context mContext, OnIdeaClickListener onIdeaClickListener){
        this.mArrayList = mArrayList;
        this.mContext   = mContext;
        this.onIdeaClickListener = onIdeaClickListener;
    }

    @NonNull
    @Override
    public IdeasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ideas_container_item, parent, false);
        return new IdeasViewHolder(view, onIdeaClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull IdeasViewHolder holder, int position) {
        IdeasItemModel model = mArrayList.get(position);
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDesc());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class IdeasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView title;
        private final TextView description;
        private final CardView ideaItem;
        OnIdeaClickListener onIdeaClickListener;

        public IdeasViewHolder(@NonNull View view, OnIdeaClickListener onIdeaClickListener) {
            super(view);
            title       = (TextView) view.findViewById(R.id.previewTitle);
            description = (TextView) view.findViewById(R.id.previewText);
            ideaItem    = (CardView) view.findViewById(R.id.ideaCard);
            this.onIdeaClickListener = onIdeaClickListener;

            ideaItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onIdeaClickListener.onIdeaClick();
        }
    }

    public interface OnIdeaClickListener{
        void onIdeaClick();
    }
}