package com.example.tsquared.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tsquared.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicsVH> {
    private ArrayList<String> mArrayList;

    public TopicsAdapter(ArrayList<String> mArrayList){
        this.mArrayList = mArrayList;
    }

    @NonNull
    @Override
    public TopicsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item, parent, false);

        return new TopicsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicsVH holder, int position) {
        holder.topic.setText(mArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public static class TopicsVH extends RecyclerView.ViewHolder{
        private final TextView topic;

        public TopicsVH(View view){
            super(view);
            topic = view.findViewById(R.id.topic);
        }
    }
}
