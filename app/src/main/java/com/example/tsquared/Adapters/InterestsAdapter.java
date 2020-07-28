package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsquared.Models.InterestsModel;
import com.example.tsquared.Models.QuestionItemModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.InterestsViewHolder> {
    private final ArrayList<InterestsModel> mArrayList;
    private Context mcontext;
    private int selected_position = -1;

    public InterestsAdapter(ArrayList<InterestsModel> mArrayList, Context mcontext){
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
    }

    @NonNull
    @Override
    public InterestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mcontext = parent.getContext();
        View view = LayoutInflater.from(mcontext).inflate(R.layout.interests_list_card, parent, false);
        return new InterestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestsViewHolder holder, final int position) {
        final InterestsModel interests = mArrayList.get(position);
        holder.interests.setText(interests.getSubject());
        //holder.container.setAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.fade_scale_animation));
        holder.checked.setOnCheckedChangeListener(null);
        holder.checked.setChecked(interests.isSelected());
        holder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                interests.setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class InterestsViewHolder extends RecyclerView.ViewHolder{
        private final TextView interests;
        private CardView container;
        private CheckBox checked;
        public InterestsViewHolder(View view){
            super(view);
            interests = (TextView) view.findViewById(R.id.interestSubject);
            container = (CardView) view.findViewById(R.id.interestCard);
            checked   = (CheckBox) view.findViewById(R.id.checkBox);
        }
    }
}