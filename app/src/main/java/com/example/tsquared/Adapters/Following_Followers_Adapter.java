package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tsquared.Models.PeopleItemModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class Following_Followers_Adapter extends RecyclerView.Adapter<Following_Followers_Adapter.MyViewHolder> {

    private final ArrayList<PeopleItemModel> mArrayList;
    private Context mcontext;
    private OnNoteListener onNoteListener;

    public Following_Followers_Adapter(ArrayList<PeopleItemModel> mArrayList, Context mcontext, OnNoteListener onNoteListener){
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
        this.onNoteListener = onNoteListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        mcontext  = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.followers, parent, false);
        return new MyViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position){
        PeopleItemModel people = mArrayList.get(position);
        Glide.with(mcontext)
                .load(people.getProfileImage())
                .into(holder.iv_image);

        holder.tv_name.setText(people.name);
        holder.tv_desc.setText(people.desc);
    }

    @Override
    public int getItemCount(){
        return mArrayList.size();
    }

    public void swapData(ArrayList<PeopleItemModel> mNewDataSet) {
        this.mArrayList.clear();
        this.mArrayList.addAll(mNewDataSet);
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView iv_image;
        private final TextView  tv_name;
        private final TextView  tv_desc;
        private final CardView cardViewLayout;
        OnNoteListener onNoteListener;

        public MyViewHolder(View view, OnNoteListener onNoteListener){
            super(view);
            iv_image    = (ImageView) view.findViewById(R.id.userImage);
            tv_name     = (TextView)  view.findViewById(R.id.userName);
            tv_desc     = (TextView)  view.findViewById(R.id.userDescription);
            this.onNoteListener = onNoteListener;

            cardViewLayout = (CardView) view.findViewById(R.id.cardView);
            cardViewLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cardViewLayout1:
                    onNoteListener.OnNoteClick(getAdapterPosition());
                    break;
            }
        }
    }
    public interface OnNoteListener{
        void OnNoteClick(int position);
        //void OnFollowClick(int position);
    }
}
