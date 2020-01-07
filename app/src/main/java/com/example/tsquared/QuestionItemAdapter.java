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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class QuestionItemAdapter extends RecyclerView.Adapter<QuestionItemAdapter.MyViewHolder> implements Filterable {

    private final ArrayList<QuestionItemModel> mArrayList;
    private Context mcontext;
    private OnNoteListener onNote;

    private ArrayList<QuestionItemModel> mArrayListFull;
    private Filter exampleFilter;

    QuestionItemAdapter(ArrayList<QuestionItemModel> mArrayList, Context mcontext, OnNoteListener onNote) {
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
        this.onNote     = onNote;
        // Want to copy off that object, not point to the same object, or reference its memory address
        mArrayListFull  = new ArrayList<>(mArrayList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mcontext = parent.getContext();
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_main_post, parent, false);
        return new MyViewHolder(view, onNote);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        QuestionItemModel question = mArrayList.get(position);
        Glide.with(mcontext)
                .load(question.getProfileImage())
                .into(holder.iv_image);

        holder.tv_name.setText(question.name);
        holder.tv_topic.setText(question.topic);
        holder.tv_question.setText(question.question);
        holder.tv_dateSubmitted.setText(question.dateSubmitted);
        holder.tv_responses.setText(question.responseNum);

        Log.d("MyAdapter", "position: " + position);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
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

    void swapData(ArrayList<QuestionItemModel> mNewDataSet) {
        this.mArrayList.clear();
        this.mArrayList.addAll(mNewDataSet);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView iv_image;
        private final TextView  tv_name;
        private final TextView  tv_topic;
        private final TextView  tv_question;
        private final TextView  tv_dateSubmitted;
        private final TextView  tv_responses;
        private final CardView  cardViewLayout;
        OnNoteListener onNoteListener;

        public MyViewHolder(View view, OnNoteListener onNoteListener) {
            super(view);
            iv_image         = view.findViewById(R.id.postIV);
            tv_name          = view.findViewById(R.id.QuestionName);
            tv_topic         = view.findViewById(R.id.topic);
            tv_question      = view.findViewById(R.id.questionContent);
            tv_dateSubmitted = view.findViewById(R.id.dateSubmitted);
            tv_responses     = view.findViewById(R.id.responseNum);
            this.onNoteListener = onNoteListener;

            cardViewLayout = view.findViewById(R.id.cardViewLayout);
            cardViewLayout.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            onNoteListener.OnNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void OnNoteClick(int position);
    }
}
/*Intent intent = new Intent(mcontext, DetailActivity.class);
  intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
  String question = tv_question.getText().toString();
  intent.putExtra("question", question);
  //String respNum  = tv_responses.getText().toString();
  //intent.putExtra("responseNum", respNum);
  mcontext.startActivity(intent);*/