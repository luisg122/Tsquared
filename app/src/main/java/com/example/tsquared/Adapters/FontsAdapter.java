package com.example.tsquared.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tsquared.Models.FontModel;
import com.example.tsquared.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FontsAdapter extends RecyclerView.Adapter<FontsAdapter.FontsVH> {
    private ArrayList<FontModel> mArrayList;

    public FontsAdapter(ArrayList<FontModel> mArrayList){
        this.mArrayList = mArrayList;
    }

    @NonNull
    @Override
    public FontsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_accessibility_font, parent, false);

        return new FontsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FontsVH holder, int position) {
        FontModel fontModel = mArrayList.get(position);
        holder.font.setText(fontModel.getFont());
        holder.fontName.setText(fontModel.getFontName());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public static class FontsVH extends RecyclerView.ViewHolder{
        private final TextView font;
        private final TextView fontName;

        public FontsVH(View view){
            super(view);
            font = (TextView) view.findViewById(R.id.font);
            fontName = (TextView) view.findViewById(R.id.fontName);
        }
    }
}
