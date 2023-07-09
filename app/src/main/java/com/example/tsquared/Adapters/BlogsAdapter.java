package com.example.tsquared.Adapters;

import android.annotation.SuppressLint;
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.Models.BlogsModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class BlogsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Object> mArrayList;
    private Context mContext;
    private BlogItemListener blogItemListener;

    public BlogsAdapter(ArrayList<Object> mArrayList, Context mContext, BlogItemListener blogItemListener){
        this.mArrayList = mArrayList;
        this.mContext   = mContext;
        this.blogItemListener = blogItemListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == R.layout.blog_create_prompt) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_create_prompt, parent, false);
            return new CreateBlogPromptViewHolder(view, blogItemListener);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item, parent, false);
            return new BlogsItemViewHolder(view, blogItemListener);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = mArrayList.get(position);

        if(holder.getItemViewType() == R.layout.blog_create_prompt) {
            final CreateBlogPromptViewHolder createBlog = (CreateBlogPromptViewHolder) holder;

            Glide.with(mContext)
                    .load("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500")
                    .into(createBlog.profileImage);

            createBlog.prompt.setText("Write something . . .");
        }

        else if(holder.getItemViewType() == R.layout.blog_item) {
            final BlogsModel blogsModel = (BlogsModel) item;
            final BlogsItemViewHolder blogsItemViewHolder = (BlogsItemViewHolder) holder;

            Glide.with(mContext)
                    .load(blogsModel.getImage())
                    .transform(new CenterCrop(), new RoundedCorners(20))
                    .into(blogsItemViewHolder.image);


            blogsItemViewHolder.title.setText(blogsModel.getTitle());
            blogsItemViewHolder.name.setText(blogsModel.getName());
            blogsItemViewHolder.firstFewLines.setText(blogsModel.getFirstFewLines());
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return R.layout.blog_create_prompt;
        }
        else return R.layout.blog_item;
    }

    public class CreateBlogPromptViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView profileImage;
        private final TextView prompt;
        private final ConstraintLayout createBlog;
        BlogItemListener blogItemListener;

        public CreateBlogPromptViewHolder(View view, BlogItemListener blogItemListener) {
            super(view);
            profileImage = (ImageView) view.findViewById(R.id.createBlogIV);
            prompt = (TextView) view.findViewById(R.id.createBlogPromptTV);
            createBlog = (ConstraintLayout) view.findViewById(R.id.createBlogPrompt);

            this.blogItemListener = blogItemListener;
            createBlog.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            blogItemListener.createBlog(getLayoutPosition());
        }
    }

    public class BlogsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView image;
        private TextView  title;
        private TextView firstFewLines;
        private TextView name;
        BlogItemListener blogItemListener;

        public BlogsItemViewHolder(@NonNull View view, BlogItemListener onNewsClickListener) {
            super(view);
            image = (ImageView) view.findViewById(R.id.blogImage);
            title = (TextView)  view.findViewById(R.id.title);
            name  = (TextView)  view.findViewById(R.id.name);
            firstFewLines = (TextView) view.findViewById(R.id.firstFewLines);

            this.blogItemListener = onNewsClickListener;
        }

        @Override
        public void onClick(View v) {
            BlogsAdapter.this.blogItemListener.clickOnBlogItem(getLayoutPosition());
        }
    }

    public interface BlogItemListener {
        void clickOnBlogItem(int position);
        void createBlog(int position);
    }
}
