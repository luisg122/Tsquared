package com.example.tsquared.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tsquared.Fragments.ReplyBottomSheet;
import com.example.tsquared.Fragments.ReplyCommentBottomSheet;
import com.example.tsquared.Models.CommentsModel;
import com.example.tsquared.Models.CommentsRepliesModel;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsRepliesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> mArrayList;
    private Context mContext;
    private IconListener iconListener;

    public CommentsRepliesAdapter(ArrayList<Object> mArrayList, Context mContext, IconListener iconListener){
        this.mArrayList = mArrayList;
        this.mContext = mContext;
        this.iconListener = iconListener;
    }

    private void checkForLikeOrDislike(CommentsRepliesModel comment, CommentsRepliesVH dataHolder){
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

        else if(!comment.isUpVoted() && !comment.isDownVoted()){
            dataHolder.upVote.setColorFilter(DarkSharedPref.isDark ? darkModeDefaultColor : lightModeDefaultColor);
            dataHolder.downVote.setColorFilter(DarkSharedPref.isDark ? darkModeDefaultColor : lightModeDefaultColor);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == R.layout.comment_main_to_reply){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_main_to_reply, parent, false);
            return new MainCommentVH(view, iconListener);
        }

        else if(viewType == R.layout.comment_reply_prompt){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_reply_prompt, parent, false);
            return new CommentReplyPrompt(view, iconListener);
        }

        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_item, parent, false);
            return new CommentsRepliesVH(view, iconListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = mArrayList.get(position);
        if(holder.getItemViewType() == R.layout.comment_main_to_reply){
            MainCommentVH dataHolder = (MainCommentVH) holder;
            CommentsModel commentsModel = (CommentsModel) item;

            Glide.with(mContext)
                    .load("https://seventhqueen.com/themes/kleo/wp-content/uploads/rtMedia/users/44269/2020/07/dummy-profile.png")
                    .into(dataHolder.commentsProfileImage);

            dataHolder.name.setText(commentsModel.getName());
            dataHolder.date.setText(commentsModel.getDate());
            dataHolder.comment.setText(commentsModel.getComment());
            dataHolder.numberOfUpVotes.setText(String.valueOf(commentsModel.getNumberOfUpVotes()));

        }

        else if(holder.getItemViewType() == R.layout.comment_reply_prompt){
            CommentReplyPrompt dataHolder = (CommentReplyPrompt) holder;
            Glide.with(mContext)
                    .load("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500")
                    .into(dataHolder.profileImage);
        }

        else if(holder.getItemViewType() == R.layout.comments_item){
            CommentsRepliesModel commentsModel = (CommentsRepliesModel) item;
            CommentsRepliesVH dataHolder = (CommentsRepliesVH) holder;
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
            return R.layout.comment_main_to_reply;
        }
        else if(position == 1) {
            return R.layout.comment_reply_prompt;
        }
        else return R.layout.comments_item;
    }

    public static class MainCommentVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView commentsProfileImage;
        private final ImageView upVote;
        private final ImageView downVote;
        private final ImageView subComments;
        private final ImageView more;
        private final TextView numberOfUpVotes;
        private final TextView numberOfReplies;
        private final TextView name;
        private final TextView date;
        private final TextView comment;

        IconListener iconListener;
        public MainCommentVH(View view, IconListener iconListener){
            super(view);
            commentsProfileImage = (ImageView) view.findViewById(R.id.commentIV);
            upVote = (ImageView) view.findViewById(R.id.upVote);
            downVote = (ImageView) view.findViewById(R.id.downVote);
            subComments = (ImageView) view.findViewById(R.id.subComments);
            more = (ImageView) view.findViewById(R.id.threeDots);
            numberOfUpVotes = (TextView) view.findViewById(R.id.numberOfUpVotes);
            numberOfReplies = (TextView) view.findViewById(R.id.numberOfReplies);
            name = (TextView) view.findViewById(R.id.commentName);
            date = (TextView) view.findViewById(R.id.commentSubmitted);
            comment = (TextView) view.findViewById(R.id.commentContent);

            this.iconListener = iconListener;
            upVote.setOnClickListener(this);
            downVote.setOnClickListener(this);
            more.setOnClickListener(this);
        }

        @Override 
        public void onClick(View view) {
            int id = view.getId();
            if(id == R.id.upVote) iconListener.mainCommentUpVote(getLayoutPosition(), view, downVote);
            else if(id == R.id.downVote) iconListener.mainCommentDownVote(getLayoutPosition(), view, upVote);
            else if(id == R.id.threeDots) iconListener.mainCommentMore(getLayoutPosition(), view);
        }
    }

    public static class CommentReplyPrompt extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ConstraintLayout postReplyToComment;
        private final ImageView profileImage;
        private final TextView  prompt;

        IconListener iconListener;
        public CommentReplyPrompt(View view, IconListener iconListener){
            super(view);
            postReplyToComment = (ConstraintLayout) view.findViewById(R.id.postReplyPrompt);
            profileImage       = (ImageView) view.findViewById(R.id.postCommentIV);
            prompt             = (TextView) view.findViewById(R.id.promptComment);

            this.iconListener  = iconListener;
            postReplyToComment.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iconListener.postReplyComment(getLayoutPosition());
        }
    }

    public static class CommentsRepliesVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView commentsProfileImage;
        private final ImageView upVote;
        private final ImageView downVote;
        private final ImageView subComments;
        private final ImageView moreOptions;
        private final TextView comment;
        private final TextView name;
        private final TextView date;
        private final TextView replies;
        private final TextView numberOfUpVotes;
        private final TextView numberOfReplies;

        private ReplyCommentBottomSheet bottomSheet;
        IconListener iconListener;

        public CommentsRepliesVH(View view, IconListener iconListener){
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
            moreOptions = (ImageView) view.findViewById(R.id.threeDots);

            this.iconListener = iconListener;
            upVote.setOnClickListener(this);
            downVote.setOnClickListener(this);
            moreOptions.setOnClickListener(this);


            subComments.setVisibility(View.INVISIBLE);
            numberOfReplies.setVisibility(View.INVISIBLE);
            replies.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if(id == R.id.upVote) iconListener.upVote(getLayoutPosition(), view, downVote);
            else if(id == R.id.downVote) iconListener.downVote(getLayoutPosition(), view, upVote);
            else if(id == R.id.threeDots) iconListener.moreOptions(getLayoutPosition());
        }
    }

    public interface IconListener{
        void upVote(int position, View view, ImageView downVote);
        void downVote(int position, View view, ImageView upVote);

        void mainCommentUpVote(int position, View view, ImageView downVote);
        void mainCommentDownVote(int position, View view, ImageView upVote);
        void mainCommentMore(int position, View view);

        void postReplyComment(int position);
        void moreOptions(int position);
    }
}
