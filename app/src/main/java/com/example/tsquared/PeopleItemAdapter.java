package com.example.tsquared;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PeopleItemAdapter extends RecyclerView.Adapter<PeopleItemAdapter.MyViewHolder> {

    private final ArrayList<PeopleItemModel> mArrayList;
    private Context mcontext;

    PeopleItemAdapter(ArrayList<PeopleItemModel> mArrayList, Context mcontext){
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        mcontext  = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_profiles, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position){
        Glide.with(mcontext)
                .load(mArrayList.get(position).getProfileImage())
                .into(holder.iv_image);

        holder.tv_name.setText(mArrayList.get(position).getName());
        holder.tv_college.setText(mArrayList.get(position).getCollege());
        holder.tv_desc.setText(mArrayList.get(position).getDesc());
    }

    @Override
    public int getItemCount(){
        return mArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView iv_image;
        private final TextView  tv_name;
        private final TextView  tv_college;
        private final TextView  tv_desc;

        private ConstraintLayout cardViewLayout;

        MyViewHolder(View view){
            super(view);

            iv_image = view.findViewById(R.id.profileImageList);
            tv_name  = view.findViewById(R.id.personName);
            tv_college  = view.findViewById(R.id.collegeName);
            tv_desc = view.findViewById(R.id.desc);


            cardViewLayout = view.findViewById(R.id.cardViewLayout1);
            cardViewLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            //Intent intent = new Intent(mcontext, AppActivity.class);
            //mcontext.startActivity(intent);
        }
    }
}
