package com.example.tsquared.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsquared.Models.QuestionItemTextModel;
import com.example.tsquared.R;

import java.util.ArrayList;

public class SearchQuestionsFragment extends Fragment {
    private View view;
    RecyclerView mainRv;
    // SearchResultsAdapter adapter;
    ArrayList<QuestionItemTextModel> mArrayList;

    public SearchQuestionsFragment(){}
    @Override
    public void onCreate(Bundle savedOnInstanceState){
        super.onCreate(savedOnInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.search_fragment, container, false);
        setUpRecyclerView();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView(){
        mArrayList = new ArrayList<>();
        mainRv = view.findViewById(R.id.results_rv);
        mainRv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);

        // adapter = new SearchResultsAdapter(mArrayList, getContext());
        // mainRv.setAdapter(adapter);
        // mainRv.setLayoutManager(layoutManager);
    }
}