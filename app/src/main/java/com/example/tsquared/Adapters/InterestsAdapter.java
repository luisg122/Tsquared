package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsquared.Models.InterestsModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.InterestsViewHolder> {
    private final ArrayList<InterestsModel> mArrayList;
    private Context mContext;
    private int lastPosition = -1;
    private int selected_position = -1;

    private OnCheckClickListener onCheckClickListener;

    public InterestsAdapter(ArrayList<InterestsModel> mArrayList, Context mContext, OnCheckClickListener onCheckClickListener){
        this.mArrayList = mArrayList;
        this.mContext   = mContext;
        this.onCheckClickListener = onCheckClickListener;
    }

    @NonNull
    @Override
    public InterestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.interests_list_card, parent, false);
        return new InterestsViewHolder(view, onCheckClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestsViewHolder holder, final int position) {
        final InterestsModel interests = mArrayList.get(position);
        holder.interests.setText(interests.getSubject());
        setAnimation(holder.container, position);
        holder.checked.setChecked(interests.isSelected());
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public static class InterestsViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener {
        private final TextView interests;
        private CardView container;
        private CheckBox checked;
        OnCheckClickListener onCheckClickListener;
        public InterestsViewHolder(View view, OnCheckClickListener onCheckClickListener){
            super(view);
            interests = (TextView) view.findViewById(R.id.interestSubject);
            container = (CardView) view.findViewById(R.id.interestCard);
            checked   = (CheckBox) view.findViewById(R.id.checkBox);
            this.onCheckClickListener = onCheckClickListener;

            checked.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            onCheckClickListener.onCheckedClick(getLayoutPosition(), buttonView, isChecked);
        }
    }

    public interface OnCheckClickListener{
        void onCheckedClick(int position, CompoundButton buttonView, boolean isChecked);
    }
}