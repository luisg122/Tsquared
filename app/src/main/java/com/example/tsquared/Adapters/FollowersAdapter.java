package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.Models.FollowersModel;
import com.example.tsquared.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.FollowersVH> {
    private Context mContext;
    private ArrayList<FollowersModel> mArrayList;
    private ProfileClickListener profileClickListener;

    public FollowersAdapter(ArrayList<FollowersModel> mArrayList, Context mContext, ProfileClickListener profileClickListener){
        this.mArrayList = mArrayList;
        this.mContext   = mContext;
        this.profileClickListener = profileClickListener;
    }

    @NonNull
    @Override
    public FollowersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item_layout, parent, false);

        return new FollowersVH(view, profileClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersVH holder, int position) {
        FollowersModel follower = mArrayList.get(position);

        Glide.with(mContext)
                .load(follower.getProfileImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(holder.profileImage);

        holder.name.setText(follower.getName());
        holder.followersNum.setText(follower.getFollowersNum());

        if(follower.getFollowing()) holder.followButton.setText(R.string.Following);
        else if(!follower.getFollowing()) holder.followButton.setText(R.string.Follow);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public static class FollowersVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView profileImage;
        public final TextView  name;
        public final TextView  followersNum;
        public final Button followButton;

        ProfileClickListener profileClickListener;
        public FollowersVH(View view, ProfileClickListener profileClickListener){
            super(view);

            profileImage = (ImageView) view.findViewById(R.id.userProfileImage);
            name = (TextView) view.findViewById(R.id.userName);
            followersNum = (TextView) view.findViewById(R.id.followersNum);
            followButton = (Button) view.findViewById(R.id.followButton);

            this.profileClickListener = profileClickListener;
            followButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();

            if(id == R.id.followButton) profileClickListener.followButton(getLayoutPosition(), view);
        }
    }
    public interface ProfileClickListener{
        void followButton(int position, View view);
    }
}
