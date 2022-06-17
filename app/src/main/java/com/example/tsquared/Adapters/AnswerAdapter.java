package com.example.tsquared.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.Activities.DrawerActivity;
import com.example.tsquared.DiffUtilsItems.AnswersDiffCallback;
import com.example.tsquared.ExpandableTextView;
import com.example.tsquared.Models.AnswerModel;
import com.example.tsquared.Models.AnswerWithImages;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Object> mArrayList;
    private final Context mcontext;
    private OnCommentsClickListener onCommentsClickListener;
    private Intent intent;

    public AnswerAdapter(ArrayList<Object> mArrayList, Context mcontext, OnCommentsClickListener onCommentsClickListener){
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
        this.onCommentsClickListener = onCommentsClickListener;
    }

    private void loadQuestionData(Question dataHolder) {
        String question = intent.getStringExtra("question");
        String numberOfAns = intent.getStringExtra("numberOfAnswers");
        dataHolder.question.setText(question);
        dataHolder.numberOfAnswers.setText(numberOfAns);
    }

    private void checkForLikeOrDislike(Object answerObj, Object dataHolderObj){
        Integer upvoteColor   = (Integer) mcontext.getResources().getColor(R.color.green);
        Integer downVoteColor = (Integer) mcontext.getResources().getColor(R.color.crimsonRed);

        Integer darkModeDefaultColor  = (Integer) mcontext.getResources().getColor(R.color.white);
        Integer lightModeDefaultColor = (Integer) mcontext.getResources().getColor(R.color.black);


        if(answerObj instanceof AnswerModel && dataHolderObj instanceof Answer) {
            AnswerModel answer = (AnswerModel) answerObj;
            Answer dataHolder = (Answer) dataHolderObj;

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

        if(answerObj instanceof AnswerWithImages && dataHolderObj instanceof AnswerAndImages) {
            AnswerWithImages answer = (AnswerWithImages) answerObj;
            AnswerAndImages dataHolder = (AnswerAndImages) dataHolderObj;

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
    }

    private void loopThroughImages(AnswerAndImages dataHolder, String[] images){
        int i = 0;
        while(i < images.length){
            if(i == 0){
                Glide.with(mcontext)
                        .load(images[i])
                        .transform(new CenterCrop(), new RoundedCorners(50))
                        .into(dataHolder.image1);
            }

            else if(i == 1){
                Glide.with(mcontext)
                        .load(images[i])
                        .transform(new CenterCrop(), new RoundedCorners(50))
                        .into(dataHolder.image2);
            }

            else if(i == 2){
                Glide.with(mcontext)
                        .load(images[i])
                        .transform(new CenterCrop(), new RoundedCorners(50))
                        .into(dataHolder.moreImages);

                int numberOfImagesLeft = images.length - 2;
                String numberOfImages = "+" + numberOfImagesLeft;
                dataHolder.numberOfImages.setText(numberOfImages);
            }

            i++;
        }

        if(i == 1){
            dataHolder.image2.setVisibility(View.INVISIBLE);
            dataHolder.moreImagesLayout.setVisibility(View.INVISIBLE);
        }

        else if(i == 2){
            dataHolder.moreImagesLayout.setVisibility(View.INVISIBLE);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == -1){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.no_answers_yet, parent, false);

            return new NoViewHolder(view);
        }

        else if(viewType == R.layout.answer_item_question){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.answer_item_question, parent, false);
            return new Question(view, onCommentsClickListener);
        }

        else if(viewType == R.layout.answer_view){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.answer_view, parent, false);

            return new Answer(view, onCommentsClickListener);
        }

        else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.answer_view_with_images, parent, false);

            return new AnswerAndImages(view, onCommentsClickListener);
        }
    }

    /*public void updateAnswersListItems(List<Object> mArrayList) {
        final AnswersDiffCallback diffCallback = new AnswersDiffCallback(this.mArrayList, mArrayList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mArrayList.clear();
        this.mArrayList.addAll(1, mArrayList);
        diffResult.dispatchUpdatesTo(this);
    }*/

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        Object item = mArrayList.get(position);

        if(holder.getItemViewType() == R.layout.answer_item_question){
            final Question dataHolder = (Question) holder;
            loadQuestionData(dataHolder);

            String value = intent.getStringExtra("type");
            assert value != null;
            if(value.equals("QuestionItemImageModel")){
                Glide.with(dataHolder.questionImage.getContext())
                        .load("https://cdn.nextgov.com/media/img/cd/2020/10/19/NGspace20201019/860x394.jpg?1618395239")
                        .transform(new CenterCrop(), new RoundedCorners(20))
                        .into(dataHolder.questionImage);
            }

            else if(value.equals("QuestionItemUrlModel")){
                Glide.with(dataHolder.urlImage.getContext())
                        .load("https://stillmed.olympics.com/media/Images/OlympicOrg/News/2021/02/19/2021-02-19-tokyo-thumbnail.jpg")
                        .into(dataHolder.urlImage);

                dataHolder.headline.setText("Tokyo Covid levels rise");
                dataHolder.source.setText("BBC.com");
            }
        }

        else if(holder.getItemViewType() == R.layout.answer_view){
            AnswerModel answer = (AnswerModel) item;
            final Answer dataHolder = (Answer) holder;
            Glide.with(mcontext)
                    .load("https://seventhqueen.com/themes/kleo/wp-content/uploads/rtMedia/users/44269/2020/07/dummy-profile.png")
                    .transform(new CenterCrop(), new RoundedCorners(50))
                    .into(dataHolder.answerProfileImage);

            dataHolder.answerProfileName.setText(answer.name);
            dataHolder.answerProfileDate.setText(answer.dateAnswered);
            dataHolder.answer.setText(answer.answer);
            dataHolder.numberOfUpVotes.setText(String.valueOf(answer.numberOfVotes));

            checkForLikeOrDislike(answer, dataHolder);

            if(answer.isFollowing()) dataHolder.followingPrompt.setText(R.string.Following);
            else dataHolder.followingPrompt.setText(R.string.Follow);

            if(answer.isTextExpanded()) dataHolder.answer.expand();
            else dataHolder.answer.collapse();
        }

        else if(holder.getItemViewType() == R.layout.answer_view_with_images){
            AnswerWithImages answer = (AnswerWithImages) item;
            final AnswerAndImages dataHolder = (AnswerAndImages) holder;
            Glide.with(mcontext)
                    .load("https://seventhqueen.com/themes/kleo/wp-content/uploads/rtMedia/users/44269/2020/07/dummy-profile.png")
                    .into(dataHolder.answerProfileImage);

            dataHolder.answerProfileName.setText(answer.name);
            dataHolder.answerProfileDate.setText(answer.dateAnswered);
            dataHolder.answer.setText(answer.answer);
            dataHolder.numberOfUpVotes.setText(String.valueOf(answer.numberOfVotes));

            checkForLikeOrDislike(answer, dataHolder);

            if(answer.isFollowing()) dataHolder.followingPrompt.setText(R.string.Following);
            else dataHolder.followingPrompt.setText(R.string.Follow);

            if(answer.isTextExpanded()) dataHolder.answer.expand();
            else dataHolder.answer.collapse();

            loopThroughImages(dataHolder, answer.getImageUrls());
        }

        else if (holder.getItemViewType() == -1){
            final NoViewHolder noViewHolder = (NoViewHolder) holder;
            // presumably bad programming practice
            String firstName = DrawerActivity.firstNameofUser;
            noViewHolder.promptUser.setText("Hey " + firstName +"! Be the first to answer this question");
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemViewType(int position){
        if(!mArrayList.isEmpty()) {
            Object obj = mArrayList.get(position);
            if (position == 0) {
                intent = (Intent) mArrayList.get(position);  // get the Intent
                return R.layout.answer_item_question;
            }

            else if (obj instanceof AnswerModel) return R.layout.answer_view;

            else if (obj instanceof AnswerWithImages) return R.layout.answer_view_with_images;
        }

        // no answers posted
        return -1;
    }

    public void swapData(ArrayList<AnswerModel> mNewDataSet) {
        this.mArrayList.clear();
        this.mArrayList.addAll(mNewDataSet);
        notifyDataSetChanged();
    }

    public void addItem(AnswerModel datum) {
        mArrayList.add(datum);
        notifyItemInserted(mArrayList.size() - 1);
    }

    public class Question extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView question;
        private final TextView numberOfAnswers;
        private final Button moreOptions;
        private final ViewStub viewStub;

        private ImageView questionImage;
        private ImageView urlImage;
        private TextView headline;
        private TextView source;

        OnCommentsClickListener onCommentsClickListener;
        public Question(View view, OnCommentsClickListener onCommentsClickListener){
            super(view);
            question = view.findViewById(R.id.question);
            numberOfAnswers = view.findViewById(R.id.numberOfAnswers);
            moreOptions = view.findViewById(R.id.three_dots);
            viewStub = view.findViewById(R.id.questionView_stub);

            // inflate based on type of question
            if (intent != null) {
                String value = intent.getStringExtra("type");
                assert value != null;

                // check the type of question
                if (value.equals("QuestionItemImageModel")) {
                    viewStub.setLayoutResource(R.layout.answer_item_image_view);
                    viewStub.inflate();

                    questionImage = view.findViewById(R.id.imageContent);

                } else if (value.equals("QuestionItemUrlModel")) {
                    viewStub.setLayoutResource(R.layout.answer_item_url_view);
                    viewStub.inflate();

                    urlImage = view.findViewById(R.id.linkImage);
                    headline = view.findViewById(R.id.headLine);
                    source   = view.findViewById(R.id.source);
                }
            }

            this.onCommentsClickListener = onCommentsClickListener;
            moreOptions.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();

            if(id == R.id.three_dots) onCommentsClickListener.moreOptions(getLayoutPosition());
        }
    }

    public class Answer extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableTextView.OnExpandListener{
        private final ImageView answerProfileImage;
        private final ImageView upVote;
        private final ImageView downVote;
        private final TextView  answerProfileName;
        private final TextView  answerProfileDate;
        private final ExpandableTextView  answer;
        private final TextView  numberOfUpVotes;
        private final TextView  followingPrompt;
        private final View circleDot;
        private final CardView  commentsCard;
        private final RelativeLayout readMore;
        private final TextView readMorePrompt;
        private final ImageView readMoreIndicator;
        private final Button moreOptions;

        OnCommentsClickListener onCommentsClickListener;

        private final CardView answerProfile;

        public Answer(View view, OnCommentsClickListener onCommentsClickListener){
            super(view);
            answerProfileImage = (ImageView) view.findViewById(R.id.answerIV);
            upVote             = (ImageView) view.findViewById(R.id.upVote);
            downVote           = (ImageView) view.findViewById(R.id.downVote);
            answerProfileName  = (TextView)  view.findViewById(R.id.answerName);

            answerProfileDate  = (TextView)  view.findViewById(R.id.answerSubmitted);
            answer             = (ExpandableTextView)  view.findViewById(R.id.answer);
            numberOfUpVotes    = (TextView)  view.findViewById(R.id.numberOfUpVotes);
            followingPrompt    = (TextView)  view.findViewById(R.id.followingPrompt);
            circleDot          = (View)      view.findViewById(R.id.circle_dot);
            answerProfile      = (CardView)  view.findViewById(R.id.answersLayout);
            commentsCard       = (CardView)  view.findViewById(R.id.commentsSection);
            moreOptions        = (Button) view.findViewById(R.id.three_dots);

            // read more layout and prompt
            readMore           = (RelativeLayout) view.findViewById(R.id.readMore);
            readMorePrompt     = (TextView) view.findViewById(R.id.readMorePrompt);
            readMoreIndicator  = (ImageView) view.findViewById(R.id.seeMoreImage);


            // set interpolator for both expanding and collapsing animations
            answer.setInterpolator(new OvershootInterpolator());

            this.onCommentsClickListener = onCommentsClickListener;
            readMore.setOnClickListener(this);
            answer.setOnExpandListener(this);
            commentsCard.setOnClickListener(this);
            upVote.setOnClickListener(this);
            downVote.setOnClickListener(this);
            followingPrompt.setOnClickListener(this);
            moreOptions.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.readMore) onCommentsClickListener.expandText(getLayoutPosition(), v, answer, readMorePrompt, readMoreIndicator);
            else if(id == R.id.commentsSection) onCommentsClickListener.OnCommentsClick(getLayoutPosition());
            else if(id == R.id.upVote) onCommentsClickListener.upVote(getLayoutPosition(), v, downVote);
            else if(id == R.id.downVote) onCommentsClickListener.downVote(getLayoutPosition(), v, upVote);
            else if(id == R.id.followingPrompt) onCommentsClickListener.follow(getLayoutPosition(), v);
            else if(id == R.id.three_dots) onCommentsClickListener.moreOptions(getLayoutPosition());
        }

        @Override
        public void onExpand(ExpandableTextView view) {
            onCommentsClickListener.textExpanded(getLayoutPosition(), view, readMore, readMorePrompt, readMoreIndicator);
        }

        @Override
        public void onCollapse(ExpandableTextView view) {
            onCommentsClickListener.textCollapsed(getLayoutPosition(), view, readMore, readMorePrompt, readMoreIndicator);
        }

        @Override
        public void onLayouted(ExpandableTextView view) {
            onCommentsClickListener.textExceedMaxLines(getLayoutPosition(), view, readMore, readMorePrompt, readMoreIndicator);
        }
    }

    public class AnswerAndImages extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableTextView.OnExpandListener{
        private final ImageView answerProfileImage;
        private final ImageView image1;
        private final ImageView image2;
        private final ImageView moreImages;
        private final ImageView upVote;
        private final ImageView downVote;
        private final TextView  numberOfImages;
        private final TextView  answerProfileName;
        private final TextView  answerProfileDate;
        private final ExpandableTextView answer;
        private final TextView  numberOfUpVotes;
        private final TextView  followingPrompt;
        private final View circleDot;
        private final CardView  commentsCard;
        private final RelativeLayout moreImagesLayout;
        private final RelativeLayout readMore;
        private final TextView readMorePrompt;
        private final ImageView readMoreIndicator;
        private final CardView answerProfile;
        private final Button moreOptions;

        OnCommentsClickListener onCommentsClickListener;


        public AnswerAndImages(View view, OnCommentsClickListener onCommentsClickListener){
            super(view);
            answerProfileImage = (ImageView) view.findViewById(R.id.answerIV);
            image1             = (ImageView) view.findViewById(R.id.image1);
            image2             = (ImageView) view.findViewById(R.id.image2);
            moreImages         = (ImageView) view.findViewById(R.id.moreImages);
            numberOfImages     = (TextView)  view.findViewById(R.id.numberOfMoreImages);

            upVote             = (ImageView) view.findViewById(R.id.upVote);
            downVote           = (ImageView) view.findViewById(R.id.downVote);
            answerProfileName  = (TextView)  view.findViewById(R.id.answerName);

            answerProfileDate  = (TextView)  view.findViewById(R.id.answerSubmitted);
            answer             = (ExpandableTextView)  view.findViewById(R.id.answer);
            numberOfUpVotes    = (TextView)  view.findViewById(R.id.numberOfUpVotes);
            followingPrompt    = (TextView)  view.findViewById(R.id.followingPrompt);
            circleDot          = (View)      view.findViewById(R.id.circle_dot);
            answerProfile      = (CardView)  view.findViewById(R.id.answersLayout);
            commentsCard       = (CardView)  view.findViewById(R.id.commentsSection);
            moreImagesLayout   = (RelativeLayout) view.findViewById(R.id.moreImagesLayout);
            moreOptions        = (Button) view.findViewById(R.id.three_dots);

            // read more layout and prompt
            readMore           = (RelativeLayout) view.findViewById(R.id.readMore);
            readMorePrompt     = (TextView) view.findViewById(R.id.readMorePrompt);
            readMoreIndicator  = (ImageView) view.findViewById(R.id.seeMoreImage);

            // set interpolator for both expanding and collapsing animations
            answer.setInterpolator(new OvershootInterpolator());

            this.onCommentsClickListener = onCommentsClickListener;
            readMore.setOnClickListener(this);
            answer.setOnExpandListener(this);
            commentsCard.setOnClickListener(this);
            upVote.setOnClickListener(this);
            downVote.setOnClickListener(this);
            followingPrompt.setOnClickListener(this);
            image1.setOnClickListener(this);
            image2.setOnClickListener(this);
            moreImages.setOnClickListener(this);
            moreOptions.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.readMore) onCommentsClickListener.expandText(getLayoutPosition(), v, answer, readMorePrompt, readMoreIndicator);
            else if(id == R.id.commentsSection) onCommentsClickListener.OnCommentsClick(getLayoutPosition());
            else if(id == R.id.upVote) onCommentsClickListener.upVote(getLayoutPosition(), v, downVote);
            else if(id == R.id.downVote) onCommentsClickListener.downVote(getLayoutPosition(), v, upVote);
            else if(id == R.id.followingPrompt) onCommentsClickListener.follow(getLayoutPosition(), v);
            else if(id == R.id.image1) onCommentsClickListener.imageOneClick(getLayoutPosition(), 0);
            else if(id == R.id.image2) onCommentsClickListener.imageTwoClick(getLayoutPosition(), 1);
            else if(id == R.id.moreImages) onCommentsClickListener.moreImageClick(getLayoutPosition(), 2);
            else if(id == R.id.three_dots) onCommentsClickListener.moreOptions(getLayoutPosition());
        }

        @Override
        public void onExpand(ExpandableTextView view) {
            onCommentsClickListener.textExpanded(getLayoutPosition(), view, readMore, readMorePrompt, readMoreIndicator);
        }

        @Override
        public void onCollapse(ExpandableTextView view) {
            onCommentsClickListener.textCollapsed(getLayoutPosition(), view, readMore, readMorePrompt, readMoreIndicator);
        }

        @Override
        public void onLayouted(ExpandableTextView view) {
            onCommentsClickListener.textExceedMaxLines(getLayoutPosition(), view, readMore, readMorePrompt, readMoreIndicator);
        }
    }

    public class NoViewHolder extends RecyclerView.ViewHolder{
        private final TextView promptUser;
        public NoViewHolder(View view){
            super(view);
            promptUser = (TextView) view.findViewById(R.id.promptCreateFirstAnswer);
        }
    }

    public interface OnCommentsClickListener{
        void OnCommentsClick(int position);
        void upVote(int position, View view, ImageView downVote);
        void downVote(int position, View view, ImageView upVote);
        void follow(int position, View view);
        void expandText(int position, View view, ExpandableTextView expandableTextView, TextView textView, ImageView imageView);
        void textExpanded(int position, View view, RelativeLayout relativeLayout, TextView textView, ImageView imageView);
        void textCollapsed(int position, View view, RelativeLayout relativeLayout, TextView textView, ImageView imageView);
        void textExceedMaxLines(int position, View view, RelativeLayout relativeLayout, TextView textView, ImageView imageView);

        void imageOneClick(int position, int imagePos);
        void imageTwoClick(int position, int imagePos);
        void moreImageClick(int position, int imagePos);

        void moreOptions(int position);
    }
}