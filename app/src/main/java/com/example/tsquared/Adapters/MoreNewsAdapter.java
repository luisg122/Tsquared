package com.example.tsquared.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tsquared.Activities.DrawerActivity;
import com.example.tsquared.Activities.NewsArticleContainer;
import com.example.tsquared.Fragments.LinkPromptBottomSheet;
import com.example.tsquared.Models.DiscoverImageModel;
import com.example.tsquared.Models.MoreNewsModel;
import com.example.tsquared.R;
import com.example.tsquared.ViewPagerTransition.DepthPageTransformer;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MoreNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> /*implements DiscoverViewPagerAdapter.OnImageClickListener*/{
    private final ArrayList<Object> mArrayList;
    private Context mcontext;

    private LinkPromptBottomSheet linkPromptBottomSheet;
    private ArrayList<DiscoverImageModel> discoverImages;
    private String headline;
    private OnMoreNewsListener onMoreNewsListener;

    public MoreNewsAdapter(ArrayList<Object> mArrayList, Context mcontext, OnMoreNewsListener onMoreNewsListener) {
        this.mArrayList = mArrayList;
        this.mcontext   = mcontext;
        this.onMoreNewsListener = onMoreNewsListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        /*if(viewType == R.layout.share_link){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_link, parent, false);
            return new ShareLinkViewHolder(view, onMoreNewsListener, linkPromptBottomSheet);
        }

        else if(viewType == R.layout.discover_card){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_card, parent, false);
            return new DiscoverViewHolder(view);
        }*/

        if(viewType == R.layout.discover_headline){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_headline, parent, false);
            return new HeadlinePrompt(view);
        }

        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_more_item, parent, false);
            return new MoreNewsViewHolder(view, onMoreNewsListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = mArrayList.get(position);
        /*if(holder.getItemViewType() == R.layout.share_link){
            ShareLinkViewHolder dataHolder = (ShareLinkViewHolder) holder;
            Glide.with(dataHolder.profileImage.getContext())
                    .load("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500")
                    .into(dataHolder.profileImage);
        }

        if(holder.getItemViewType() == R.layout.discover_card){
            assert holder instanceof DiscoverViewHolder;
            DiscoverViewHolder dataHolder = (DiscoverViewHolder) holder;
            setUpDiscoverViewPager(dataHolder.discoverCard);
        }*/

        if(holder.getItemViewType() == R.layout.discover_headline){
            assert holder instanceof HeadlinePrompt;
            HeadlinePrompt dataHolder = (HeadlinePrompt) holder;
            dataHolder.headlinePrompt.setText(headline);
        }

        else if(holder.getItemViewType() == R.layout.news_more_item){
            assert holder instanceof MoreNewsViewHolder;
            MoreNewsViewHolder dataHolder = (MoreNewsViewHolder) holder;

            MoreNewsModel moreNewsModel = (MoreNewsModel) item;
            Glide.with(dataHolder.iv_image.getContext())
                    .load(moreNewsModel.getImageURL())
                    .into(dataHolder.iv_image);
            dataHolder.tv_desc.setText(moreNewsModel.getDescription());
        }
    }

    @Override
    public int getItemViewType(int position){
        /*if(position == 0) {
            linkPromptBottomSheet = (LinkPromptBottomSheet) mArrayList.get(position);
            return R.layout.share_link;
        }

        else if(position == 1) {
            discoverImages = (ArrayList<DiscoverImageModel>) mArrayList.get(position);
            return R.layout.discover_card;
        }*/

        Object obj = mArrayList.get(position);
        if(position == 0) {
            headline = (String) mArrayList.get(position);
            return R.layout.discover_headline;
        }

        if(obj instanceof MoreNewsModel) return R.layout.news_more_item;

        return -1;
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    /*private void setUpDiscoverViewPager(ViewPager2 discoverCardImages) {
        Handler handler = new Handler(Looper.getMainLooper());

        Runnable slideRunnable = new Runnable(){
            @Override
            public void run() {
                if(discoverCardImages.getCurrentItem() + 1 == discoverImages.size()){
                    discoverCardImages.setCurrentItem(0);
                }

                else discoverCardImages.setCurrentItem(discoverCardImages.getCurrentItem() + 1, true);
            }
        };

        DiscoverViewPagerAdapter discoverViewPagerAdapter = new DiscoverViewPagerAdapter(discoverImages, getApplicationContext(), this);
        discoverCardImages.setAdapter(discoverViewPagerAdapter);
        discoverCardImages.setPageTransformer(new DepthPageTransformer());
        discoverCardImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(slideRunnable);
                handler.postDelayed(slideRunnable, 8000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    @Override
    public void onImageClick(int position) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                Intent intent = new Intent(mcontext, NewsArticleContainer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("position", position);
                mcontext.startActivity(intent);
                //((Activity) mcontext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 150);
    }*/

    public static class ShareLinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView profileImage;
        private final TextView  prompt;
        private final CardView  shareLink;
        private LinkPromptBottomSheet linkPromptBottomSheet;
        OnMoreNewsListener onMoreNewsListener;

        public ShareLinkViewHolder(View view, OnMoreNewsListener onMoreNewsListener, LinkPromptBottomSheet linkPromptBottomSheet){
            super(view);
            profileImage = view.findViewById(R.id.linkProfilePic);
            prompt       = view.findViewById(R.id.postLinkPrompt);
            shareLink    = view.findViewById(R.id.shareLink);
            this.linkPromptBottomSheet = linkPromptBottomSheet;

            this.onMoreNewsListener = onMoreNewsListener;
            shareLink.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMoreNewsListener.shareLinkClick(getLayoutPosition(), linkPromptBottomSheet);
        }
    }

    private static class DiscoverViewHolder extends RecyclerView.ViewHolder{
        private final ViewPager2 discoverCard;
        public DiscoverViewHolder(View view){
            super(view);
            discoverCard = (ViewPager2) view.findViewById(R.id.discoverCardVP);
        }
    }

    private static class HeadlinePrompt extends RecyclerView.ViewHolder{
        private final TextView headlinePrompt;
        public HeadlinePrompt(View view){
            super(view);
            headlinePrompt = (TextView) view.findViewById(R.id.headlineTV);
        }
    }

    public static class MoreNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView iv_image;
        private final TextView tv_desc;
        private final CardView moreNewsItem;
        private final CardView headlinePrompt;
        OnMoreNewsListener onMoreNewsListener;

        public MoreNewsViewHolder(@NonNull View view, OnMoreNewsListener onMoreNewsListener) {
            super(view);
            iv_image       = (ImageView) view.findViewById(R.id.imageNewsStaggered);
            tv_desc        = (TextView)  view.findViewById(R.id.textViewStaggered);
            moreNewsItem   = (CardView)  view.findViewById(R.id.moreNewsLayout);
            headlinePrompt = (CardView)  view.findViewById(R.id.headlinePrompt);
            this.onMoreNewsListener = onMoreNewsListener;

            moreNewsItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMoreNewsListener.onMoreNewsClick(getLayoutPosition());
        }
    }

    public interface OnMoreNewsListener{
        void shareLinkClick(int position, LinkPromptBottomSheet bottomSheet);
        void onMoreNewsClick(int position);
    }
}