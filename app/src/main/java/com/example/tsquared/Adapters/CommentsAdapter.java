package com.example.tsquared.Adapters;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tsquared.Fragments.CommentBottomSheet;
import com.example.tsquared.Models.CommentsModel;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> mArrayList;
    private Context mContext;
    private CommentBottomSheet commentBottomSheet;
    private OnReplyListener onReplyListener;

    public CommentsAdapter(ArrayList<Object> mArrayList, Context mContext, OnReplyListener onReplyListener){
        this.mArrayList = mArrayList;
        this.mContext   = mContext;
        this.onReplyListener = onReplyListener;
    }

    private void checkForLikeOrDislike(CommentsModel comment, CommentsViewHolder dataHolder){
        Integer upvoteColor   = (Integer) mContext.getResources().getColor(R.color.green);
        Integer downVoteColor = (Integer) mContext.getResources().getColor(R.color.crimsonRed);

        Integer darkModeDefaultColor  = (Integer) mContext.getResources().getColor(R.color.white);
        Integer lightModeDefaultColor = (Integer) mContext.getResources().getColor(R.color.black);

        if(comment.isUpVoted() && !comment.isDownVoted()) {
            dataHolder.upVote.setColorFilter(upvoteColor);
            dataHolder.downVote.setColorFilter(DarkSharedPref.isDark ? darkModeDefaultColor : lightModeDefaultColor);
        }

        else if(!comment.isUpVoted() && comment.isDownVoted()) {
            dataHolder.upVote.setColorFilter(DarkSharedPref.isDark ? darkModeDefaultColor : lightModeDefaultColor);
            dataHolder.downVote.setColorFilter(downVoteColor);
        }

        else if(!comment.isUpVoted() && !comment.isDownVoted()) {
            dataHolder.upVote.setColorFilter(DarkSharedPref.isDark ? darkModeDefaultColor : lightModeDefaultColor);
            dataHolder.downVote.setColorFilter(DarkSharedPref.isDark ? darkModeDefaultColor : lightModeDefaultColor);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == R.layout.comment_prompt){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_prompt, parent, false);
            return new CreateCommentPrompt(view, onReplyListener, commentBottomSheet);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_item, parent, false);
            return new CommentsViewHolder(view, onReplyListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        Object item = mArrayList.get(position);
        if(holder.getItemViewType() == R.layout.comment_prompt){
            CreateCommentPrompt dataHolder = (CreateCommentPrompt) holder;
            Glide.with(mContext)
                    .load("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500")
                    .into(dataHolder.profileImage);
        }

        else if(holder.getItemViewType() == R.layout.comments_item){
            CommentsModel commentsModel = (CommentsModel) item;
            CommentsViewHolder dataHolder = (CommentsViewHolder) holder;
            Glide.with(mContext)
                    .load("https://seventhqueen.com/themes/kleo/wp-content/uploads/rtMedia/users/44269/2020/07/dummy-profile.png")
                    .into(dataHolder.commentsProfileImage);
            dataHolder.name.setText(commentsModel.getName());
            dataHolder.date.setText(commentsModel.getDate());
            dataHolder.comment.setText(commentsModel.getComment());
            dataHolder.numberOfUpVotes.setText(String.valueOf(commentsModel.getNumberOfUpVotes()));
            checkForLikeOrDislike(commentsModel, dataHolder);
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public int getItemViewType(int position){
        if(position == 0) {
            commentBottomSheet = (CommentBottomSheet) mArrayList.get(position);
            return R.layout.comment_prompt;
        }
        else return R.layout.comments_item;
    }

    public static class CreateCommentPrompt extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView profileImage;
        private final TextView prompt;
        private final ConstraintLayout postComment;
        private CommentBottomSheet commentBottomSheet;

        OnReplyListener onReplyListener;
        public CreateCommentPrompt(View view, OnReplyListener onReplyListener, CommentBottomSheet commentBottomSheet){
            super(view);
            profileImage = (ImageView) view.findViewById(R.id.postCommentIV);
            prompt       = (TextView)  view.findViewById(R.id.promptComment);
            postComment  = (ConstraintLayout) view.findViewById(R.id.postCommentPrompt);
            this.commentBottomSheet = commentBottomSheet;

            this.onReplyListener = onReplyListener;
            postComment.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onReplyListener.onPostCommentClick(getLayoutPosition(), commentBottomSheet);
        }
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView commentsProfileImage;
        private final ImageView upVote;
        private final ImageView downVote;
        private final ImageView subComments;
        private final TextView comment;
        private final TextView name;
        private final TextView date;
        private final TextView replies;
        private final TextView numberOfUpVotes;
        private final TextView numberOfReplies;
        private final ImageView moreOptions;

        OnReplyListener onReplyListener;
        public CommentsViewHolder(@NonNull View view, OnReplyListener onReplyListener){
            super(view);
            comment = (TextView) view.findViewById(R.id.commentContent);
            name    = (TextView) view.findViewById(R.id.commentName);
            date    = (TextView) view.findViewById(R.id.commentSubmitted);
            replies = (TextView) view.findViewById(R.id.replies);
            numberOfUpVotes = (TextView) view.findViewById(R.id.numberOfUpVotes);
            numberOfReplies = (TextView) view.findViewById(R.id.numberOfReplies);
            moreOptions = (ImageView) view.findViewById(R.id.threeDots);

            commentsProfileImage = (ImageView) view.findViewById(R.id.commentIV);
            upVote = (ImageView) view.findViewById(R.id.upVote);
            downVote = (ImageView) view.findViewById(R.id.downVote);
            subComments = (ImageView) view.findViewById(R.id.subComments);

            this.onReplyListener = onReplyListener;
            replies.setOnClickListener(this);
            upVote.setOnClickListener(this);
            downVote.setOnClickListener(this);
            moreOptions.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if(id == R.id.replies) onReplyListener.onReplyClick(getLayoutPosition());
            else if(id == R.id.upVote) onReplyListener.onUpVoteClick(getLayoutPosition(), view, downVote);
            else if(id == R.id.downVote) onReplyListener.onDownVoteClick(getLayoutPosition(), view, upVote);
            else if(id == R.id.threeDots) onReplyListener.onMoreOptionsClick(getLayoutPosition());
        }
    }

    public interface OnReplyListener{
        void onPostCommentClick(int position, CommentBottomSheet commentBottomSheet);
        void onReplyClick(int position);
        void onUpVoteClick(int position, View view, ImageView downVote);
        void onDownVoteClick(int position, View view, ImageView upVote);
        void onMoreOptionsClick(int position);
    }
}
