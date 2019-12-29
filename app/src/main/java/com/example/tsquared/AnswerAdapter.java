package com.example.tsquared;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.ArrayList;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.MyViewHolder> {
    private final ArrayList<AnswerModel> mArrayList;
    private final Context mcontext;

    AnswerAdapter(ArrayList<AnswerModel> mArrayList, Context mcontext){
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerAdapter.MyViewHolder holder, int position) {
        Glide.with(mcontext)
                .load(mArrayList.get(position).getProfileImage())
                .into(holder.answerProfileImage);

        holder.answerProfileName.setText(mArrayList.get(position).getName());
        holder.answerProfileDate.setText(mArrayList.get(position).getDateAnswered());
        holder.answer.setText(mArrayList.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private final ImageView answerProfileImage;
        private final TextView  answerProfileName;
        private final TextView  answerProfileDate;
        private final TextView  answer;

        private final ConstraintLayout answerProfile;

        MyViewHolder(View view){
            super(view);
            answerProfileImage = view.findViewById(R.id.answerIV);
            answerProfileName  = view.findViewById(R.id.answerName);
            answerProfileDate  = view.findViewById(R.id.answerSubmitted);
            answer = view.findViewById(R.id.answer);

            answerProfile = view.findViewById(R.id.answersLayout);
        }
    }
}
