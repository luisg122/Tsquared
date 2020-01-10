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
import com.example.tsquared.Activities.DrawerActivity;
import com.example.tsquared.Models.AnswerModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class AnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<AnswerModel> mArrayList;
    private final Context mcontext;

    public AnswerAdapter(ArrayList<AnswerModel> mArrayList, Context mcontext){
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 0){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.no_answers_yet, parent, false);
            return new NoViewHolder(view);
        }
        else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.answer_view, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            AnswerModel answer = mArrayList.get(position);
            final MyViewHolder dataHolder = (MyViewHolder) holder;
            Glide.with(mcontext)
                    .load(answer.getProfileImage())
                    .into(dataHolder.answerProfileImage);

            dataHolder.answerProfileName.setText(answer.name);
            dataHolder.answerProfileDate.setText(answer.dateAnswered);
            dataHolder.answer.setText(answer.answer);
        }

        else if (holder instanceof NoViewHolder){
            final NoViewHolder noViewHolder = (NoViewHolder) holder;
            // presumably bad programming practice
            String firstName = DrawerActivity.firstNameofUser;
            noViewHolder.promptUser.setText("Hey " + firstName +"! Be the first to answer this question");
        }
    }

    @Override
    public int getItemCount() {
        if(mArrayList.size() == 0){
            return 1;
        }
        else return mArrayList.size();
    }

    @Override
    public int getItemViewType(int position){
        if(mArrayList.size() == 0){
            return 0;
        }
        else return 1;
    }

    public void swapData(ArrayList<AnswerModel> mNewDataSet) {
        this.mArrayList.clear();
        this.mArrayList.addAll(mNewDataSet);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final ImageView answerProfileImage;
        private final TextView  answerProfileName;
        private final TextView  answerProfileDate;
        private final TextView  answer;

        private final CardView answerProfile;

        public MyViewHolder(View view){
            super(view);
            answerProfileImage = (ImageView) view.findViewById(R.id.answerIV);
            answerProfileName  = (TextView)  view.findViewById(R.id.answerName);
            answerProfileDate  = (TextView)  view.findViewById(R.id.answerSubmitted);
            answer             = (TextView)  view.findViewById(R.id.answer);
            answerProfile      = (CardView)  view.findViewById(R.id.answersLayout);
        }
    }

    public class NoViewHolder extends RecyclerView.ViewHolder{
        private final TextView promptUser;
        public NoViewHolder(View view){
            super(view);
            promptUser = (TextView) view.findViewById(R.id.promptCreateFirstAnswer);
        }
    }
}
