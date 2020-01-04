package com.example.tsquared;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PeopleItemAdapter extends RecyclerView.Adapter<PeopleItemAdapter.MyViewHolder> {

    private final ArrayList<PeopleItemModel> mArrayList;
    private Context mcontext;

    PeopleItemAdapter(ArrayList<PeopleItemModel> mArrayList, Context mcontext){
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
        notifyDataSetChanged();

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
        PeopleItemModel people = mArrayList.get(position);
        Glide.with(mcontext)
                .load(people.getProfileImage())
                .into(holder.iv_image);

        holder.tv_name.setText(people.name);
        holder.tv_college.setText(people.college);
        holder.tv_desc.setText(people.desc);
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
        private final Button    tv_button;
        private final MaterialCardView cardViewLayout;

        MyViewHolder(View view){
            super(view);
            iv_image    = view.findViewById(R.id.profileImageList);
            tv_name     = view.findViewById(R.id.personName);
            tv_college  = view.findViewById(R.id.collegeName);
            tv_desc     = view.findViewById(R.id.desc);

            tv_button   = view.findViewById(R.id.followButton);
            //tv_button.setOnClickListener(this);

            cardViewLayout = view.findViewById(R.id.cardViewLayout1);
            cardViewLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            Intent intent = new Intent(mcontext, PersonProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            String name    = tv_name.getText().toString();
            String college = tv_college.getText().toString();
            String desc    = tv_desc.getText().toString();

            intent.putExtra("name", name);
            intent.putExtra("college", college);
            intent.putExtra("desc", desc);
            mcontext.startActivity(intent);
        }
    }
}