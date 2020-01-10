package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tsquared.Models.PeopleItemModel;
import com.example.tsquared.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PeopleItemAdapter extends RecyclerView.Adapter<PeopleItemAdapter.MyViewHolder> {

    private final ArrayList<PeopleItemModel> mArrayList;
    private Context mcontext;
    private OnNoteListener onNoteListener;

    public PeopleItemAdapter(ArrayList<PeopleItemModel> mArrayList, Context mcontext, OnNoteListener onNoteListener){
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
        this.onNoteListener = onNoteListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        mcontext  = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_profiles, parent, false);
        return new MyViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position){
        PeopleItemModel people = mArrayList.get(position);
        Glide.with(mcontext)
                .load(people.getProfileImage())
                .into(holder.iv_image);

        holder.tv_name.setText(people.name);
        holder.tv_college.setText(people.college);
        holder.tv_desc.setText(people.desc);
        holder.email = people.getEmail();

        //int[] androidColors = mcontext.getResources().getIntArray(R.array.androidcolors);
        //int randomColor     = androidColors[new Random().nextInt(androidColors.length)];
        //int color_bg = mcontext.getResources().getColor(R.color.card_bg1);
        //holder.cardViewLayout.setCardBackgroundColor(color_bg);
        holder.cardViewLayout.setRadius(30f);

        //final int pressedColor = mcontext.getResources().getColor(R.color.mainColor);
        /*holder.tv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tv_button.setBackgroundColor(pressedColor);
                holder.tv_button.setText("Following");
            }
        });*/
    }

    @Override
    public int getItemCount(){
        return mArrayList.size();
    }

    public void swapData(ArrayList<PeopleItemModel> mNewDataSet) {
        this.mArrayList.clear();
        this.mArrayList.addAll(mNewDataSet);
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView iv_image;
        private final TextView  tv_name;
        private final TextView  tv_college;
        private final TextView  tv_desc;
        private final Button    tv_button;
        private final MaterialCardView cardViewLayout;
        OnNoteListener onNoteListener;
        private String email = " ";

        public MyViewHolder(View view, OnNoteListener onNoteListener){
            super(view);
            iv_image    = (ImageView) view.findViewById(R.id.profileImageList);
            tv_name     = (TextView)  view.findViewById(R.id.personName);
            tv_college  = (TextView)  view.findViewById(R.id.collegeName);
            tv_desc     = (TextView)  view.findViewById(R.id.desc);
            this.onNoteListener = onNoteListener;


            tv_button      = (Button) view.findViewById(R.id.followButton);
            tv_button.setOnClickListener(this);
            cardViewLayout = (MaterialCardView) view.findViewById(R.id.cardViewLayout1);
            cardViewLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cardViewLayout1:
                    onNoteListener.OnNoteClick(getAdapterPosition());
                    break;
                case R.id.followButton:
                    //final int pressedColor = mcontext.getResources().getColor(R.color.mainColor);
                    //tv_button.setBackgroundColor(pressedColor);
                    //tv_button.setText("Following");
                    int position = getAdapterPosition();
                    Toast.makeText(getApplicationContext(), "Position: " + position, Toast.LENGTH_SHORT).show();

                    /*if(onNoteListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            onNoteListener.OnFollowClick(position);
                        }
                    }*/
                    break;
            }
        }
    }
    public interface OnNoteListener{
        void OnNoteClick(int position);
        //void OnFollowClick(int position);
    }
}

/*
            Intent intent = new Intent(mcontext, PersonProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            String name    = tv_name.getText().toString();
            String college = tv_college.getText().toString();
            String desc    = tv_desc.getText().toString();

            intent.putExtra("name", name);
            intent.putExtra("college", college);
            intent.putExtra("desc", desc);
            intent.putExtra("email", email);
            mcontext.startActivity(intent);
 */