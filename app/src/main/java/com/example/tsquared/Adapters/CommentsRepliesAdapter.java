package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tsquared.Models.CommentsModel;
import com.example.tsquared.Models.CommentsRepliesModel;
import com.example.tsquared.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsRepliesAdapter extends RecyclerView.Adapter<CommentsRepliesAdapter.CommentsReliesVH> {
    private ArrayList<CommentsRepliesModel> mArrayList;
    private Context mContext;
    private IconListener iconListener;

    public CommentsRepliesAdapter(ArrayList<CommentsRepliesModel> mArrayList, Context mContext, IconListener iconListener){
        this.mArrayList = mArrayList;
        this.mContext = mContext;
        this.iconListener = iconListener;
    }

    @NonNull
    @Override
    public CommentsReliesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_item, parent, false);
        return new CommentsReliesVH(view, iconListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsReliesVH holder, int position) {
        CommentsRepliesModel commentsModel = mArrayList.get(position);
        Glide.with(mContext)
                .load("https://seventhqueen.com/themes/kleo/wp-content/uploads/rtMedia/users/44269/2020/07/dummy-profile.png")
                .into(holder.commentsProfileImage);

        holder.name.setText(commentsModel.getName());
        holder.date.setText(commentsModel.getDate());
        holder.comment.setText(commentsModel.getComment());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class CommentsReliesVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView commentsProfileImage;
        private final ImageView upVote;
        private final ImageView downVote;
        private final ImageView subComments;
        private final ImageView replyIV;
        private final TextView comment;
        private final TextView name;
        private final TextView date;
        private final TextView replies;
        private final TextView numberOfUpVotes;
        private final TextView numberOfReplies;
        IconListener iconListener;

        public CommentsReliesVH(View view, IconListener iconListener){
            super(view);
            comment = (TextView) view.findViewById(R.id.commentContent);
            name    = (TextView) view.findViewById(R.id.commentName);
            date    = (TextView) view.findViewById(R.id.commentSubmitted);
            replies = (TextView) view.findViewById(R.id.replies);
            numberOfUpVotes = (TextView) view.findViewById(R.id.numberOfUpVotes);
            numberOfReplies = (TextView) view.findViewById(R.id.numberOfReplies);
            commentsProfileImage = (ImageView) view.findViewById(R.id.commentIV);
            upVote = (ImageView) view.findViewById(R.id.upVote);
            downVote = (ImageView) view.findViewById(R.id.downVote);
            subComments = (ImageView) view.findViewById(R.id.subComments);
            replyIV = (ImageView) view.findViewById(R.id.reply);
            replyIV.setOnClickListener(this);
            upVote.setOnClickListener(this);
            downVote.setOnClickListener(this);


            subComments.setVisibility(View.INVISIBLE);
            numberOfReplies.setVisibility(View.INVISIBLE);
            replies.setVisibility(View.GONE);
            replyIV.setVisibility(View.VISIBLE);

            this.iconListener = iconListener;
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if(id == R.id.reply) iconListener.replyClick(getLayoutPosition());
            else if(id == R.id.upVote) iconListener.upVote(getLayoutPosition(), view);
            else if(id == R.id.downVote) iconListener.upVote(getLayoutPosition(), view);
        }
    }

    public interface IconListener{
        void replyClick(int position);
        void upVote(int position, View view);
        void downVote(int position, View view);
    }
}
