package com.example.tsquared;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class QuestionItemAdapter extends RecyclerView.Adapter<QuestionItemAdapter.MyViewHolder> implements Filterable {

    private final ArrayList<QuestionItemModel> mArrayList;
    private Context mcontext;

    private ArrayList<QuestionItemModel> mArrayListFull;
    private Filter exampleFilter;

    QuestionItemAdapter(ArrayList<QuestionItemModel> mArrayList, Context mcontext) {
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;

        // Want to copy off that object, not point to the same object, or reference its memory address
        mArrayListFull  = new ArrayList<>(mArrayList);
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

    public void clear(){
        mArrayList.clear();
    }

    public void addQuestions(ArrayList<QuestionItemModel> questionList){
        questionList.addAll(mArrayList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return null;
        //return exampleFilter;
    }

    public interface OnFindQuestionsListener {
        void onResults(ArrayList<QuestionItemModel> results);
    }

    public void findQuestions(Context context, String query, final OnFindQuestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {


                ArrayList<QuestionItemModel> suggestionList = new ArrayList<>();

                if (!(constraint == null || constraint.length() == 0)) {

                    for (QuestionItemModel question : mArrayListFull) {
                        if (question.getQuestion().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {
                            suggestionList.add(question);
                        }
                    }

                }

                FilterResults results = new FilterResults();
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((ArrayList<QuestionItemModel>) results.values);
                }
            }
        }.filter(query);
    }

    public void swapData(ArrayList<QuestionItemModel> mNewDataSet) {
        mArrayList.clear();
        mArrayList.addAll(mNewDataSet);
        notifyDataSetChanged();
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
            iv_image         = view.findViewById(R.id.postIV);
            tv_name          = view.findViewById(R.id.QuestionName);
            tv_topic         = view.findViewById(R.id.topic);
            tv_question      = view.findViewById(R.id.questionContent);
            tv_dateSubmitted = view.findViewById(R.id.dateSubmitted);
            tv_responses     = view.findViewById(R.id.responseNum);

            cardViewLayout = view.findViewById(R.id.cardViewLayout);
            cardViewLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mcontext, DetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            String question = tv_question.getText().toString();
            intent.putExtra("question", question);
            mcontext.startActivity(intent);
        }
    }
}