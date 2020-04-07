package com.example.tsquared.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tsquared.Models.SphereModel;
import com.example.tsquared.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpheresAdapter extends RecyclerView.Adapter<SpheresAdapter.ViewHolder> {

        private Context context;
        private ArrayList<SphereModel> list;
        private OnCategoryListener onNote;

        public SpheresAdapter(ArrayList<SphereModel> list, Context context, OnCategoryListener onNote) {
            this.list = list;
            this.context = context;
            this.onNote = onNote;
        }

        @NonNull
        @Override
        public SpheresAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.subject_class, parent, false);
            return new ViewHolder(view, onNote);
        }

        @Override
        public void onBindViewHolder(@NonNull SpheresAdapter.ViewHolder holder, int i) {
            SphereModel model = list.get(i);
            Glide.with(context)
                    .load(model.getImage())
                    .into(holder.imageView);
            holder.textView.setText(model.getName());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            View view;
            ImageView imageView;
            TextView textView;
            OnCategoryListener onNoteListener;
            private final MaterialCardView cardViewLayout;


            public ViewHolder(@NonNull View itemView, OnCategoryListener onNoteListener) {
                super(itemView);
                view = itemView;
                imageView = view.findViewById(R.id.subjectImage);
                textView = view.findViewById(R.id.subjectName);
                this.onNoteListener = onNoteListener;

                cardViewLayout = view.findViewById(R.id.category);
                cardViewLayout.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
                onNoteListener.OnCategoryClick(getAdapterPosition());
            }
        }
    public interface OnCategoryListener{
        void OnCategoryClick(int position);
    }
}