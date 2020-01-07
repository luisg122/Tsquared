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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PeopleItemAdapter extends RecyclerView.Adapter<PeopleItemAdapter.MyViewHolder> {

    private final ArrayList<PeopleItemModel> mArrayList;
    private Context mcontext;
    private OnNoteListener onNoteListener;

    PeopleItemAdapter(ArrayList<PeopleItemModel> mArrayList, Context mcontext, OnNoteListener onNoteListener){
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

        final int pressedColor = mcontext.getResources().getColor(R.color.mainColor);
        holder.tv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tv_button.setBackgroundColor(pressedColor);
                holder.tv_button.setText("Following");
            }
        });
    }

    @Override
    public int getItemCount(){
        return mArrayList.size();
    }

    void swapData(ArrayList<PeopleItemModel> mNewDataSet) {
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
            tv_button   = (Button)    view.findViewById(R.id.followButton);
            this.onNoteListener = onNoteListener;

            cardViewLayout = view.findViewById(R.id.cardViewLayout1);
            cardViewLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            onNoteListener.OnNoteClick(getAdapterPosition());
        }
    }
    public interface OnNoteListener{
        void OnNoteClick(int position);
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