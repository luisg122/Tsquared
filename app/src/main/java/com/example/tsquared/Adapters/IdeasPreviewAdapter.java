package com.example.tsquared.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.Activities.Settings;
import com.example.tsquared.Models.IdeasItemModel;
import com.example.tsquared.Models.IdeasPreviewModel;
import com.example.tsquared.R;

import java.util.ArrayList;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;
import static com.facebook.FacebookSdk.getApplicationContext;

public class IdeasPreviewAdapter extends RecyclerView.Adapter<IdeasPreviewAdapter.IdeasPreViewHolder>
                                 implements IdeasItemAdapter.OnIdeaClickListener{

    private final ArrayList<IdeasPreviewModel> mArrayList;
    private final Context mContext;

    public IdeasPreviewAdapter(ArrayList<IdeasPreviewModel>mArrayList, Context mContext){
        this.mArrayList = mArrayList;
        this.mContext   = mContext;
    }

    // Disable touch detection for parent recyclerView if we use vertical nested recyclerViews
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch(action){
                case MotionEvent.ACTION_DOWN:
                    v.getParent().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        }
    };

    @NonNull
    @Override
    public IdeasPreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ideas_container, parent, false);

        view.findViewById(R.id.ideas_recycler_view).setOnTouchListener(mTouchListener);

        return new IdeasPreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IdeasPreViewHolder holder, int position) {
        IdeasPreviewModel modelItem = mArrayList.get(position);
        setUpIdeasRecyclerView(holder.ideas, modelItem.getIdeas());

        Glide.with(mContext)
                .load("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500")
                .into(holder.userImg);
        Glide.with(mContext)
                .load("https://insights.som.yale.edu/sites/default/files/styles/rectangle_xl/public/insights/background/What%20the%20Plunge%20in%20the%20Stock%20Market%20Means%20for%20Individual%20Investors.jpg?h=d0d46503&itok=fqSKqvO2")
                .transform(new CenterCrop(), new RoundedCorners(50))
                .into(holder.newsImage);

        holder.userName.setText(modelItem.getName());
        holder.newsTitle.setText(modelItem.getNewsTitle());
        holder.newsSource.setText(modelItem.getSourceTitle());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class IdeasPreViewHolder extends RecyclerView.ViewHolder{
        private final ImageView userImg;
        private final TextView  userName;
        private final ImageView newsImage;
        private final TextView  newsTitle;
        private final TextView  newsSource;
        private final RecyclerView ideas;
        public IdeasPreViewHolder(@NonNull View view) {
            super(view);
            userImg    = (ImageView)    view.findViewById(R.id.profilePic);
            userName   = (TextView)     view.findViewById(R.id.userName);
            newsImage  = (ImageView)    view.findViewById(R.id.imageInfo);
            newsTitle  = (TextView)     view.findViewById(R.id.descInfo);
            newsSource = (TextView)     view.findViewById(R.id.source);
            ideas      = (RecyclerView) view.findViewById(R.id.ideas_recycler_view);
        }
    }

    private void setUpIdeasRecyclerView(RecyclerView rv, ArrayList<IdeasItemModel> ideasList){
        RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();
        IdeasItemAdapter ideasItemAdapter        = new IdeasItemAdapter(ideasList, mContext, this);
        LinearLayoutManager linearManager        = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);

        linearManager.setInitialPrefetchItemCount(3);
        rv.setLayoutManager(linearManager);
        rv.setRecycledViewPool(sharedPool);
        rv.setHasFixedSize(true);
        rv.setAdapter(ideasItemAdapter);
        rv.setNestedScrollingEnabled(false);
    }

    @Override
    public void onIdeaClick() {
        Intent intent = new Intent(getApplicationContext(), Settings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}