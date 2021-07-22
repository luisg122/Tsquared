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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.Models.QuestionItemImageModel;
import com.example.tsquared.Models.QuestionItemTextModel;
import com.example.tsquared.Models.QuestionItemUrlModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class QuestionItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    private final ArrayList<Object> mArrayList;
    private Context mcontext;
    private OnNoteListener onNote;

    public QuestionItemAdapter(ArrayList<Object> mArrayList, Context mcontext, OnNoteListener onNote) {
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
        this.onNote     = onNote;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_text_post, parent, false);
            return new MyViewHolder(view, onNote);
        }

        else if(viewType == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_image_post, parent, false);
            return new MyImageViewHolder(view, onNote);
        }

        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_link_post, parent, false);
            return new MyUrlViewHolder(view, onNote);
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public int getItemViewType(int position){
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position % 3;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        Object item = mArrayList.get(position);
        if (holder instanceof MyViewHolder) {
            final MyViewHolder dataViewHolder = (MyViewHolder) holder;

            QuestionItemTextModel question = (QuestionItemTextModel) item;
            dataViewHolder.tv_topic.setText(question.topic);
            dataViewHolder.tv_question.setText(question.question);
            dataViewHolder.tv_dateSubmitted.setText(question.dateSubmitted);
            dataViewHolder.tv_responses.setText(question.responseNum);
            dataViewHolder.moreIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mcontext, dataViewHolder.moreIcon);
                    popupMenu.inflate(R.menu.menu_scrolling);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
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

        else if (holder instanceof MyImageViewHolder) {
            final MyImageViewHolder dataViewHolder = (MyImageViewHolder) holder;
            Glide.with(dataViewHolder.image.getContext())
                    .load("https://cdn.nextgov.com/media/img/cd/2020/10/19/NGspace20201019/860x394.jpg?1618395239")
                    .transform(new CenterCrop(), new RoundedCorners(20))
                    .into(dataViewHolder.image);

            QuestionItemImageModel question = (QuestionItemImageModel) item;
            dataViewHolder.tv_topic.setText(question.topic);
            dataViewHolder.tv_question.setText(question.question);
            dataViewHolder.tv_dateSubmitted.setText(question.dateSubmitted);
            dataViewHolder.tv_responses.setText(question.responseNum);
            dataViewHolder.moreIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mcontext, dataViewHolder.moreIcon);
                    popupMenu.inflate(R.menu.menu_scrolling);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
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

        else if (holder instanceof MyUrlViewHolder) {
            final MyUrlViewHolder dataViewHolder = (MyUrlViewHolder) holder;
            Glide.with(dataViewHolder.urlImage.getContext())
                    .load("https://stillmed.olympics.com/media/Images/OlympicOrg/News/2021/02/19/2021-02-19-tokyo-thumbnail.jpg")
                    .error(R.drawable.ic_link)
                    .into(dataViewHolder.urlImage);

            QuestionItemUrlModel question = (QuestionItemUrlModel) item;
            dataViewHolder.tv_topic.setText(question.topic);
            dataViewHolder.tv_question.setText(question.question);
            dataViewHolder.urlHeadLine.setText(question.headline);
            dataViewHolder.urlSource.setText(question.source);
            dataViewHolder.tv_dateSubmitted.setText(question.dateSubmitted);
            dataViewHolder.tv_responses.setText(question.responseNum);
            dataViewHolder.moreIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mcontext, dataViewHolder.moreIcon);
                    popupMenu.inflate(R.menu.menu_scrolling);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
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
    }

    @Override
    public Filter getFilter() {
        return null;
        //return exampleFilter;
    }

    public void swapData(ArrayList<QuestionItemTextModel> mNewDataSet) {
        this.mArrayList.clear();
        this.mArrayList.addAll(mNewDataSet);
        notifyDataSetChanged();
    }

    public void addItem(QuestionItemTextModel datum) {
        mArrayList.add(datum);
        notifyItemInserted(mArrayList.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // private final ImageView iv_image;
        // private final TextView  tv_name;
        private final TextView  tv_topic;
        private final TextView  tv_question;
        private final TextView  tv_dateSubmitted;
        private final TextView  tv_responses;
        private final CardView  cardViewLayout;
        private final Button moreIcon;
        OnNoteListener onNoteListener;

        public MyViewHolder(View view, OnNoteListener onNoteListener) {
            super(view);
            // iv_image       = view.findViewById(R.id.postIV);
            // tv_name        = view.findViewById(R.id.QuestionName);
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
            onNoteListener.OnNoteClick(getLayoutPosition());
        }
    }

    public interface OnNoteListener{
        void OnNoteClick(int position);
    }

    public class MyImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // private final ImageView iv_image;
        // private final TextView  tv_name;
        private final TextView  tv_topic;
        private final TextView  tv_question;
        private final TextView  tv_dateSubmitted;
        private final TextView  tv_responses;
        private final CardView  cardViewLayout;
        private final Button moreIcon;
        private final ImageView image;
        OnNoteListener onNoteListener;

        public MyImageViewHolder(View view, OnNoteListener onNoteListener) {
            super(view);
            // iv_image       = view.findViewById(R.id.postIV);
            // tv_name        = view.findViewById(R.id.QuestionName);
            tv_topic         = view.findViewById(R.id.topic);
            tv_question      = view.findViewById(R.id.questionContent);
            tv_dateSubmitted = view.findViewById(R.id.dateSubmitted);
            tv_responses     = view.findViewById(R.id.responseNum);
            moreIcon         = view.findViewById(R.id.three_dots);
            image = view.findViewById(R.id.imageContent);
            this.onNoteListener = onNoteListener;

            cardViewLayout = view.findViewById(R.id.cardViewLayout);
            cardViewLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.OnNoteClick(getLayoutPosition());
        }
    }

    public class MyUrlViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // private final ImageView iv_image;
        // private final TextView  tv_name;
        private final TextView  tv_topic;
        private final TextView  tv_question;
        private final TextView  tv_dateSubmitted;
        private final TextView  tv_responses;
        private final CardView  cardViewLayout;
        private final Button moreIcon;
        private final ImageView urlImage;
        private final TextView  urlHeadLine;
        private final TextView  urlSource;
        OnNoteListener onNoteListener;

        public MyUrlViewHolder(View view, OnNoteListener onNoteListener){
            super(view);
            // iv_image       = view.findViewById(R.id.postIV);
            // tv_name        = view.findViewById(R.id.QuestionName);
            tv_topic         = view.findViewById(R.id.topic);
            tv_question      = view.findViewById(R.id.questionContent);
            tv_dateSubmitted = view.findViewById(R.id.dateSubmitted);
            tv_responses     = view.findViewById(R.id.responseNum);
            moreIcon         = view.findViewById(R.id.three_dots);
            urlImage    = view.findViewById(R.id.linkImage);
            urlHeadLine = view.findViewById(R.id.headLine);
            urlSource   = view.findViewById(R.id.source);
            this.onNoteListener = onNoteListener;

            cardViewLayout = view.findViewById(R.id.cardViewLayout);
            cardViewLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.OnNoteClick(getLayoutPosition());
        }
    }
}