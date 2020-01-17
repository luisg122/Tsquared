package com.example.tsquared.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tsquared.Models.QuestionItemModel;
import com.example.tsquared.R;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.MyViewHolder>
        implements Filterable {


    Context mContext;
    ArrayList<QuestionItemModel> mData ;
    ArrayList<QuestionItemModel> mDataFiltered ;

    public SearchResultsAdapter(ArrayList<QuestionItemModel> mData, Context mContext) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataFiltered = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.search_result_post,viewGroup,false);
        return new MyViewHolder(layout);
    }


    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                if (Key.isEmpty()) {

                    mDataFiltered = mData ;

                }
                else {
                    ArrayList<QuestionItemModel> lstFiltered = new ArrayList<>();
                    for (QuestionItemModel row : mData) {
                        if (row.question.toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);
                        }
                    }
                    mDataFiltered = lstFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values= mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataFiltered = (ArrayList<QuestionItemModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    // data filtered
    public void swapData(ArrayList<QuestionItemModel> mNewDataSet) {
        this.mDataFiltered.clear();
        this.mDataFiltered.addAll(mNewDataSet);
        notifyDataSetChanged();
    }

    // remove search results
    public void removeData(){
        this.mDataFiltered.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // we apply animation to views here
        // first lets create an animation for user photo
        QuestionItemModel question = mDataFiltered.get(position);
        holder.img_user.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation));
        // lets create the animation for the whole card
        // first lets create a reference to it
        // you ca use the previous same animation like the following
        // but i want to use a different one so lets create it ..
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));
        Glide.with(mContext)
                .load(question.getProfileImage())
                .into(holder.img_user);
        holder.tv_title.setText(question.name);
        holder.tv_content.setText(question.question);
        holder.tv_date.setText(question.dateSubmitted);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_content,tv_date;
        ImageView img_user;
        RelativeLayout container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_description);
            tv_date = itemView.findViewById(R.id.tv_date);
            img_user = itemView.findViewById(R.id.img_user);
        }
    }
}