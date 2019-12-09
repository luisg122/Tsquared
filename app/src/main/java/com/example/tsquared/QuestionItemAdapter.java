package com.example.tsquared;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class QuestionItemAdapter extends RecyclerView.Adapter<QuestionItemAdapter.MyViewHolder> {

    private final ArrayList<QuestionItemModel> mArrayList;
    private Context mcontext;

    QuestionItemAdapter(ArrayList<QuestionItemModel> mArrayList, Context mcontext) {
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mcontext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(mcontext)
                .load(mArrayList.get(position).getProfileImage())
                .into(holder.iv_image);

        holder.tv_name.setText(mArrayList.get(position).getName());
        holder.tv_topic.setText(mArrayList.get(position).getTopic());
        holder.tv_question.setText(mArrayList.get(position).getQuestion());
        holder.tv_dateSubmitted.setText(mArrayList.get(position).getDateSubmitted());
        holder.tv_responses.setText(mArrayList.get(position).getResponseNum());

        Log.d("MyAdapter", "position: " + position);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView iv_image;
        private final TextView  tv_name;
        private final TextView  tv_topic;
        private final TextView  tv_question;
        private final TextView  tv_dateSubmitted;
        private final TextView  tv_responses;

        private final ConstraintLayout cardViewLayout;

        MyViewHolder(View view) {
            super(view);

            iv_image = view.findViewById(R.id.postIV);
            tv_name  = view.findViewById(R.id.QuestionName);
            tv_topic = view.findViewById(R.id.topic);
            tv_question = view.findViewById(R.id.questionContent);
            tv_dateSubmitted = view.findViewById(R.id.dateSubmitted);
            tv_responses = view.findViewById(R.id.responseNum);



            cardViewLayout = view.findViewById(R.id.cardViewLayout);
            cardViewLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mcontext, DetailActivity.class);
            mcontext.startActivity(intent);
        }
    }
}