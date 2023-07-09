package com.example.tsquared.Fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsquared.Activities.AnswerCommentsSection;
import com.example.tsquared.Activities.BlogWindow;
import com.example.tsquared.Activities.DrawerActivity;
import com.example.tsquared.Activities.ReplyComments;
import com.example.tsquared.Adapters.BlogsAdapter;
import com.example.tsquared.Models.BlogsModel;
import com.example.tsquared.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Set;

public class BlogsFragment extends Fragment implements BlogsAdapter.BlogItemListener {
    private View view;
    private RecyclerView blogsRecyclerView;
    private BlogsAdapter blogsAdapter;
    private ArrayList<Object> mArrayList;
    private ConstraintLayout createBlogPromptView;

    public BlogsFragment(){}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.blog_item_list, container, false);

        setUpViews();
        setUpRecyclerView();
        return view;
    }

    private void setUpViews(){
        blogsRecyclerView = (RecyclerView) view.findViewById(R.id.blogsRV);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpRecyclerView(){
        dummyDataSetUp();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        blogsAdapter = new BlogsAdapter(mArrayList, getApplicationContext(), this);
        blogsRecyclerView.setLayoutManager(layoutManager);

        blogsRecyclerView.setAdapter(blogsAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void dummyDataSetUp(){
        mArrayList = new ArrayList<>();

        mArrayList.add("");

        mArrayList.add(new BlogsModel("John Smith", "Day in the life of a Google Software Engineer" , "Come to work with me as I try to finish up a project, drink coffee and needlessly stress about the little things in life. Let's apply some CSS to a boring Page Layout", "https://miro.medium.com/fit/c/224/224/1*FF9z79YYMrnXTwN0RaZngw.jpeg"));
        mArrayList.add(new BlogsModel("Jane Smith", "How to be a 10X Engineer", "Come to work with me as I try to finish up a project, drink coffee and needlessly stress about the little things in life. Let's apply some CSS to a boring Page Layout","https://miro.medium.com/fit/c/224/224/1*eT-MP8W2pYaVzyYzptsRpg.jpeg"));
        mArrayList.add(new BlogsModel("Sam Rue", "Study Plan for Leetcode", "Come to work with me as I try to finish up a project, drink coffee and needlessly stress about the little things in life. Let's apply some CSS to a boring Page Layout","https://miro.medium.com/fit/c/224/224/1*OtWEaFrknr1Wkw13hjEnMA.jpeg"));
        mArrayList.add(new BlogsModel("Oscar Morales", "Why FAANG is overrated", "Come to work with me as I try to finish up a project, drink coffee and needlessly stress about the little things in life. Let's apply some CSS to a boring Page Layout","https://miro.medium.com/fit/c/224/224/0*TnTNB2r2qLJRI2P9.png"));
        mArrayList.add(new BlogsModel("Logan Di", "How to be financially responsible", "Come to work with me as I try to finish up a project, drink coffee and needlessly stress about the little things in life. Let's apply some CSS to a boring Page Layout","https://miro.medium.com/fit/c/224/224/1*5rzj64C1uAWqHraAwOvhFA.png"));
        mArrayList.add(new BlogsModel("Andy Yee", "Getting Accepted to Y-Combinator", "Come to work with me as I try to finish up a project, drink coffee and needlessly stress about the little things in life. Let's apply some CSS to a boring Page Layout","https://miro.medium.com/fit/c/224/224/1*5MS5Pf17hHK3RoVUYHrVqQ.jpeg"));
        mArrayList.add(new BlogsModel("John Smith", "Day in the life of a Google Software Engineer", "Come to work with me as I try to finish up a project, drink coffee and needlessly stress about the little things in life. Let's apply some CSS to a boring Page Layout","https://miro.medium.com/fit/c/224/224/1*IMfxcj_GVL04h1t5vmqwIg.png"));
        mArrayList.add(new BlogsModel("Jane Smith", "How to be a 10X Engineer", "Come to work with me as I try to finish up a project, drink coffee and needlessly stress about the little things in life. Let's apply some CSS to a boring Page Layout","https://miro.medium.com/fit/c/224/224/1*bK5gVZZ5sA8J6uDsNxfm9w.jpeg"));
        mArrayList.add(new BlogsModel("Sam Rue", "Study Plan for Leetcode", "Come to work with me as I try to finish up a project, drink coffee and needlessly stress about the little things in life. Let's apply some CSS to a boring Page Layout","https://miro.medium.com/fit/c/224/224/0*ZGXv4vivNccsbWAn.jpeg"));
        mArrayList.add(new BlogsModel("Oscar Morales", "Why FAANG is overrated", "Come to work with me as I try to finish up a project, drink coffee and needlessly stress about the little things in life. Let's apply some CSS to a boring Page Layout","https://miro.medium.com/fit/c/224/224/1*0gZJThP288BRmXOF4aXYJA.png"));
        mArrayList.add(new BlogsModel("Logan Di", "How to be financially responsible", "Come to work with me as I try to finish up a project, drink coffee and needlessly stress about the little things in life. Let's apply some CSS to a boring Page Layout","https://miro.medium.com/fit/c/224/224/1*dTLCbfJ19BizOIFeKcISAQ.jpeg"));
        mArrayList.add(new BlogsModel("Andy Yee", "Getting Accepted to Y-Combinator", "Come to work with me as I try to finish up a project, drink coffee and needlessly stress about the little things in life. Let's apply some CSS to a boring Page Layout","https://miro.medium.com/fit/c/224/224/1*g1xyDFA5tI5KsjFAtxv1jg.jpeg"));
    }

    @Override
    public void createBlog(int position) {
        Intent intent = new Intent(getApplicationContext(), BlogWindow.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        activityResultLauncher.launch(intent);
        requireActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_CANCELED) {
                        Intent intent = result.getData();
                        Snackbar.make(view, "Blog Draft Saved", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    public void clickOnBlogItem(int position) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
