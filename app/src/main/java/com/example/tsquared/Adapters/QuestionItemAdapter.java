package com.example.tsquared.Adapters;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.tsquared.Models.QuestionItemModel;
import com.example.tsquared.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionItemAdapter extends RecyclerView.Adapter<QuestionItemAdapter.QuestionViewHolder> {

    private final List<QuestionItemModel> data;
    private Context context;
    private OnNoteListener onNote;

    public QuestionItemAdapter(final List<QuestionItemModel> data, final Context context, OnNoteListener onNote) {
        this.data = data;
        this.context = context;
        this.onNote  = onNote;
    }

    @NonNull
    @Override
    public QuestionItemAdapter.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_post, parent, false);
        return new QuestionViewHolder(view, onNote);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionItemAdapter.QuestionViewHolder viewHolder, int position) {
        final QuestionItemModel question = data.get(position);

        performResetForRecycledViews(viewHolder, question);

        viewHolder.tv_topic.setText(question.topic);
        viewHolder.tv_question.setText(question.postTitle);
        viewHolder.tv_responses.setText(question.responseNum);

        if(viewHolder.tv_information != null) {
            viewHolder.tv_information.setVisibility(View.VISIBLE);
            viewHolder.tv_information.setText(question.postPreviewInformation);
        }

        if (containsUrlContent(question)) {
            Log.i("MyAdapter", "Inflating ViewStub for view for URL VIew at position: " + (position + 1));
            inflateUrlView(viewHolder, question);
        } else if (containsImageContent(viewHolder, question)) {
            Log.i("MyAdapter", "Inflating ViewStub for view for Image View at position: " + (position + 1));
            showImageContent(viewHolder, question);
        }

        Log.d("MyAdapter", "Current View Position: " + (position + 1));
    }

    private boolean containsUrlContent(final QuestionItemModel question) {
        return question.headline != null && question.source != null && question.urlImage != null;
    }

    private boolean containsImageContent(final QuestionViewHolder viewHolder, final QuestionItemModel question) {
        if (question.imageContent != null && viewHolder.imageContentImageView.getVisibility() == View.VISIBLE) {
            return true;
        } else if (question.imageContent != null && viewHolder.imageContentImageView.getVisibility() == View.GONE) {
            viewHolder.imageContentImageView.setVisibility(View.VISIBLE);
            return true;
        }

        return false;
    }

    private void performResetForRecycledViews(final QuestionViewHolder viewHolder, final QuestionItemModel question) {
        if(question.imageContent == null) {
            viewHolder.imageContentImageView.setVisibility(View.GONE);
        }

        if(!containsUrlContent(question) && viewHolder.urlContentCardView != null) {
            viewHolder.urlContentCardView.setVisibility(View.GONE);
        }
    }
    
    private void inflateUrlView(final QuestionViewHolder viewHolder, final QuestionItemModel question) {
        if(viewHolder.viewStub != null) {
            viewHolder.viewStub.setLayoutResource(R.layout.question_url_content);
            final View inflatedRootView = viewHolder.viewStub.inflate();

            viewHolder.urlContentCardView = inflatedRootView.findViewById(R.id.linkUrlView);
            viewHolder.headline = inflatedRootView.findViewById(R.id.headLine);
            viewHolder.source   = inflatedRootView.findViewById(R.id.source);
            viewHolder.urlImage = inflatedRootView.findViewById(R.id.linkImage);
            viewHolder.viewStub = null;
        } else {
            final View rootView = viewHolder.linearLayout;

            viewHolder.urlContentCardView = rootView.findViewById(R.id.linkUrlView);
            viewHolder.headline = rootView.findViewById(R.id.headLine);
            viewHolder.source   = rootView.findViewById(R.id.source);
            viewHolder.urlImage = rootView.findViewById(R.id.linkImage);
        }


        viewHolder.urlContentCardView.setVisibility(View.VISIBLE);
        setMargins(viewHolder.urlContentCardView, 20, 20, 20, 20);
        viewHolder.headline.setText(question.headline);
        viewHolder.source.setText(question.source);
        viewHolder.source.setText(question.source);
        Glide.with(viewHolder.urlImage.getContext())
                .load("https://stillmed.olympics.com/media/Images/OlympicOrg/News/2021/02/19/2021-02-19-tokyo-thumbnail.jpg")
                .error(R.drawable.ic_link)
                .into(viewHolder.urlImage);

    }

    private void showImageContent(final QuestionViewHolder viewHolder, final QuestionItemModel question) {
        setMargins(viewHolder.imageContentImageView, 0, 20, 0, 20);
        Glide.with(viewHolder.imageContentImageView.getContext())
                .load(question.imageContent)
                .transform(new CenterCrop())
                .into(viewHolder.imageContentImageView);
    }

    private void setMargins(final View view, int leftMarginDP, int topMarginDP, int rightMarginDP, int bottomMarginDP) {
        if(view == null) {
            return;
        }

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        final Resources resource = view.getContext().getResources();

        int left = DP_to_PX(resource, leftMarginDP);
        int top = DP_to_PX(resource, topMarginDP);
        int right = DP_to_PX(resource, rightMarginDP);
        int bottom = DP_to_PX(resource, bottomMarginDP);

        params.setMargins(left, top, right, bottom);
        view.setLayoutParams(params);
    }

    private int DP_to_PX(final Resources resource, int marginDP) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                marginDP,
                resource.getDisplayMetrics()
        );
    }

    public void swapData(ArrayList<QuestionItemModel> mNewDataSet) {
        this.data.clear();
        this.data.addAll(mNewDataSet);
        notifyDataSetChanged();
    }

    public void addItem(QuestionItemModel datum) {
        data.add(datum);
        notifyItemInserted(data.size());
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView  tv_topic;
        private final TextView  tv_question;
        private final TextView  tv_information;
        private final TextView  tv_responses;
        private final CardView  cardViewLayout;
        private final RelativeLayout topics;
        private final Button moreIcon;
        private final LinearLayout linearLayout;

        // Dynamic view rendering using viewStub inflation
        private ViewStub viewStub;
        private ImageView urlImage;
        private TextView headline;
        private TextView source;
        private ImageView imageContentImageView;
        private CardView urlContentCardView;

        final OnNoteListener onNoteListener;

        public QuestionViewHolder(View view, OnNoteListener onNoteListener) {
            super(view);
            tv_topic     = view.findViewById(R.id.topic);
            tv_question  = view.findViewById(R.id.questionContent);
            tv_information = view.findViewById(R.id.questionInformationPreview);
            viewStub = view.findViewById(R.id.contentViewStub);
            tv_responses = view.findViewById(R.id.responseNum);
            moreIcon = view.findViewById(R.id.three_dots);
            cardViewLayout = view.findViewById(R.id.cardViewLayout);
            topics = view.findViewById(R.id.topicTags);
            linearLayout = view.findViewById(R.id.linearLayout);
            imageContentImageView = view.findViewById(R.id.imageContent);

            this.onNoteListener = onNoteListener;
            cardViewLayout.setOnClickListener(this);
            moreIcon.setOnClickListener(this);
            topics.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if(id == R.id.cardViewLayout) onNoteListener.OnNoteClick(getLayoutPosition());
            else if(id == R.id.three_dots) onNoteListener.OnMoreIconClick(getLayoutPosition());
            else if(id == R.id.topicTags)  onNoteListener.OnMoreTopics(getLayoutPosition());
        }
    }

    public interface OnNoteListener{
        void OnNoteClick(int position);
        void OnMoreIconClick(int position);
        void OnMoreTopics(int position);
    }
}