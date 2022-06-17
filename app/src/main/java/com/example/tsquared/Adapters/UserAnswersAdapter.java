package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.ExpandableTextView;
import com.example.tsquared.Models.AnswerModel;
import com.example.tsquared.Models.AnswerWithImages;
import com.example.tsquared.Models.InterestsModel;
import com.example.tsquared.Models.UserAnswerModel;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class UserAnswersAdapter extends RecyclerView.Adapter<UserAnswersAdapter.UserAnswerVH>{
    private final ArrayList<UserAnswerModel> mArrayList;
    private Context mContext;

    private OnUserAnswerClickListener onUserAnswerClickListener;
    public UserAnswersAdapter(ArrayList<UserAnswerModel> mArrayList, Context mContext, OnUserAnswerClickListener onUserAnswerClickListener){
        this.mArrayList = mArrayList;
        this.mContext   = mContext;

        this.onUserAnswerClickListener = onUserAnswerClickListener;
    }

    @NonNull
    @Override
    public UserAnswerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_answer_item, parent, false);

        return new UserAnswerVH(view, onUserAnswerClickListener);
    }

    private void checkForLikeOrDislike(UserAnswerModel answer, UserAnswerVH dataHolder){
        Integer upvoteColor   = (Integer) mContext.getResources().getColor(R.color.green);
        Integer downVoteColor = (Integer) mContext.getResources().getColor(R.color.crimsonRed);

        Integer darkModeDefaultColor  = (Integer) mContext.getResources().getColor(R.color.white);
        Integer lightModeDefaultColor = (Integer) mContext.getResources().getColor(R.color.black);


        if(answer.isUpVoted() && !answer.isDownVoted()) {
            dataHolder.upVote.setColorFilter(upvoteColor);
            dataHolder.downVote.setColorFilter(DarkSharedPref.isDark ? darkModeDefaultColor : lightModeDefaultColor);
        }

        else if(!answer.isUpVoted() && answer.isDownVoted()) {
            dataHolder.upVote.setColorFilter(DarkSharedPref.isDark ? darkModeDefaultColor : lightModeDefaultColor);
            dataHolder.downVote.setColorFilter(downVoteColor);
        }

        else if(!answer.isUpVoted() && !answer.isDownVoted()){
            dataHolder.upVote.setColorFilter(DarkSharedPref.isDark ? darkModeDefaultColor : lightModeDefaultColor);
            dataHolder.downVote.setColorFilter(DarkSharedPref.isDark ? darkModeDefaultColor : lightModeDefaultColor);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UserAnswerVH holder, int position) {
        final UserAnswerModel answer = (UserAnswerModel) mArrayList.get(position);

        Glide.with(mContext)
                .load("https://seventhqueen.com/themes/kleo/wp-content/uploads/rtMedia/users/44269/2020/07/dummy-profile.png")
                .transform(new CenterCrop(), new RoundedCorners(50))
                .into(holder.answerProfileImage);

        holder.answerProfileName.setText(answer.name);
        holder.answerDateSubmitted.setText(answer.dateAnswered);
        holder.answer.setText(answer.answer);
        holder.numberOfUpVotes.setText(String.valueOf(answer.numberOfVotes));

        checkForLikeOrDislike(answer, holder);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public static class UserAnswerVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView answerProfileImage;
        private final ImageView upVote;
        private final ImageView downVote;
        private final TextView  answerProfileName;
        private final TextView  answerDateSubmitted;
        private final TextView  question;
        private final ExpandableTextView answer;
        private final TextView  numberOfUpVotes;
        private final CardView commentsCard;
        private final CardView answerLayout;
        private final Button moreOptions;


        OnUserAnswerClickListener onUserAnswerClickListener;
        public UserAnswerVH(View view, OnUserAnswerClickListener onUserAnswerClickListener){
            super(view);

            answerProfileImage = (ImageView) view.findViewById(R.id.answerIV);
            upVote             = (ImageView) view.findViewById(R.id.upVote);
            downVote           = (ImageView) view.findViewById(R.id.downVote);
            answerProfileName  = (TextView)  view.findViewById(R.id.answerName);

            answerDateSubmitted = (TextView)  view.findViewById(R.id.answerSubmitted);
            question           = (TextView)  view.findViewById(R.id.question);
            answer             = (ExpandableTextView)  view.findViewById(R.id.answer);
            numberOfUpVotes    = (TextView)  view.findViewById(R.id.numberOfUpVotes);
            answerLayout       = (CardView)  view.findViewById(R.id.answersLayout);
            commentsCard       = (CardView)  view.findViewById(R.id.commentsSection);
            moreOptions        = (Button)    view.findViewById(R.id.three_dots);


            this.onUserAnswerClickListener = onUserAnswerClickListener;
            answerLayout.setOnClickListener(this);
            commentsCard.setOnClickListener(this);
            upVote.setOnClickListener(this);
            downVote.setOnClickListener(this);
            moreOptions.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();

            if(id == R.id.answersLayout) onUserAnswerClickListener.answerClick(getLayoutPosition());
            else if(id == R.id.commentsSection) onUserAnswerClickListener.onCommentsClick(getLayoutPosition());
            else if(id == R.id.upVote) onUserAnswerClickListener.upVote(getLayoutPosition(), view, downVote);
            else if(id == R.id.downVote) onUserAnswerClickListener.downVote(getLayoutPosition(), view, upVote);
            else if(id == R.id.three_dots) onUserAnswerClickListener.moreOptions(getLayoutPosition());
        }
    }

    public interface OnUserAnswerClickListener{
        void answerClick(int position);
        void onCommentsClick(int position);
        void upVote(int position, View view, ImageView downVote);
        void downVote(int position, View view, ImageView upVote);
        void moreOptions(int position);
    }
}
