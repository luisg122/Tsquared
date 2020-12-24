package com.example.tsquared.Adapters;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tsquared.Models.QuestionItemModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class QuestionItemAdapter extends RecyclerView.Adapter<QuestionItemAdapter.MyViewHolder>
        implements Filterable {

    private final ArrayList<QuestionItemModel> mArrayList;
    private Context mcontext;
    private OnNoteListener onNote;

    public QuestionItemAdapter(ArrayList<QuestionItemModel> mArrayList, Context mcontext, OnNoteListener onNote) {
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
        this.onNote     = onNote;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mcontext = parent.getContext();
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_main_post, parent, false);
        return new MyViewHolder(view, onNote);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        QuestionItemModel question = mArrayList.get(position);
        /*Glide.with(mcontext)
                .load(question.getProfileImage())
                .into(holder.iv_image);*/

        holder.tv_name.setText(question.name);
        holder.tv_topic.setText(question.topic);
        holder.tv_question.setText(question.question);
        holder.tv_dateSubmitted.setText(question.dateSubmitted);
        holder.tv_responses.setText(question.responseNum);
        holder.moreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mcontext, holder.moreIcon);
                popupMenu.inflate(R.menu.menu_scrolling);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.follow_info:
                                Toast.makeText(mcontext, "Following Question", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.bookmark:
                                Toast.makeText(mcontext, "Question Bookmarked", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
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

    public void swapData(ArrayList<QuestionItemModel> mNewDataSet) {
        this.mArrayList.clear();
        this.mArrayList.addAll(mNewDataSet);
        notifyDataSetChanged();
    }

    public void addItem(QuestionItemModel datum) {
        mArrayList.add(datum);
        notifyItemInserted(mArrayList.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private final ImageView iv_image;
        private final TextView  tv_name;
        private final TextView  tv_topic;
        private final TextView  tv_question;
        private final TextView  tv_dateSubmitted;
        private final TextView  tv_responses;
        private final CardView  cardViewLayout;
        private final Button moreIcon;
        OnNoteListener onNoteListener;

        public MyViewHolder(View view, OnNoteListener onNoteListener) {
            super(view);
            //iv_image       = view.findViewById(R.id.postIV);
            tv_name          = view.findViewById(R.id.QuestionName);
            tv_topic         = view.findViewById(R.id.topic);
            tv_question      = view.findViewById(R.id.questionContent);
            tv_dateSubmitted = view.findViewById(R.id.dateSubmitted);
            tv_responses     = view.findViewById(R.id.responseNum);
            moreIcon         = view.findViewById(R.id.three_dots);
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