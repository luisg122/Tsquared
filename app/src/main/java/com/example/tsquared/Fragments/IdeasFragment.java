package com.example.tsquared.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsquared.Activities.AddIdeaActivity;
import com.example.tsquared.Activities.Settings;
import com.example.tsquared.Adapters.IdeasItemAdapter;
import com.example.tsquared.Adapters.IdeasPreviewAdapter;
import com.example.tsquared.Models.IdeasItemModel;
import com.example.tsquared.Models.IdeasPreviewModel;
import com.example.tsquared.R;

import java.util.ArrayList;
import java.util.Objects;

public class IdeasFragment extends Fragment {
    private View view;
    private RecyclerView preViewRV;
    private IdeasPreviewAdapter preViewAdapter;
    private CardView ideaPrompt;

    private ArrayList<IdeasPreviewModel> ideasPreList;
    private ArrayList<IdeasItemModel> ideasList;

    public IdeasFragment(){

    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.ideas_list, container, false);
        setUpViews();
        setUpIdeaPrompt();
        setUpUsersIdeasRecyclerView();
        return view;
    }

    public void setUpViews(){
        preViewRV  = view.findViewById(R.id.allUsersIdeas);
        ideaPrompt = view.findViewById(R.id.ideaPrompt);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setUpIdeaPrompt(){
        ideaPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddIdeaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setUpUsersIdeasRecyclerView(){
        setUserIdeaPosts();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        preViewRV.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new
                DividerItemDecoration(preViewRV.getContext(),
                DividerItemDecoration.VERTICAL);
        divider.setDrawable((Objects.requireNonNull(ContextCompat.getDrawable(Objects.requireNonNull(getContext()),
                R.drawable.line_divider_black))));
        preViewRV.addItemDecoration(divider);

        preViewAdapter = new IdeasPreviewAdapter(ideasPreList, getContext());
        preViewRV.setHasFixedSize(true);
        preViewRV.setAdapter(preViewAdapter);
        ViewCompat.setNestedScrollingEnabled(preViewRV,false);
    }

    public void setUserIdeaPosts(){
        setIdeas();
        ideasPreList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            IdeasPreviewModel ideasPreviewPost = new IdeasPreviewModel();
            ideasPreviewPost.setName("Brian Doe");
            ideasPreviewPost.setNewsTitle("Stock market, what are the smart investments to make for the end of 2020");
            ideasPreviewPost.setSourceTitle("news.yahoo.com/");
            ideasPreviewPost.setIdeas(ideasList);
            ideasPreList.add(ideasPreviewPost);
        }
    }

    public void setIdeas(){
        ideasList = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            IdeasItemModel ideasItem = new IdeasItemModel("1", "Investing or speculating?", "In today's world many people believe to be investors but what they actually are doing is speculating, meaning they're buying assets for the short term and then dumping them like their ex-wives.");
            ideasList.add(ideasItem);
        }
    }
}