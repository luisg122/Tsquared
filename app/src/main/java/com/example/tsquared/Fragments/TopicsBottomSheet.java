package com.example.tsquared.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.tsquared.Adapters.QuestionItemAdapter;
import com.example.tsquared.Adapters.TopicsAdapter;
import com.example.tsquared.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TopicsBottomSheet extends BottomSheetDialogFragment {
    private View view;
    private ImageView cancel_IV;
    private RecyclerView recyclerView;
    private TopicsAdapter topicsAdapter;
    private ArrayList<String> topicNames;


    @Override
    public void onCreate(Bundle savedOnInstanceState){
        super.onCreate(savedOnInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.topics, container, false);

        Objects.requireNonNull(getDialog()).setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                assert bottomSheet != null;
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        setUpViews();
        setUpRecyclerView();
        setUpClickListeners();

        return view;
    }

    private void setUpViews(){
        recyclerView = (RecyclerView) view.findViewById(R.id.topicsRV);
        cancel_IV = (ImageView) view.findViewById(R.id.cancel_button);
    }

    private void setUpClickListeners(){
        cancel_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setUpRecyclerView(){
        dummyData();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        topicsAdapter = new TopicsAdapter(topicNames);
        recyclerView.setAdapter(topicsAdapter);
    }

    private void dummyData(){
        topicNames = new ArrayList<>();
        topicNames.add("Mathematics");
        topicNames.add("STEM");
        topicNames.add("College");
        topicNames.add("Calculus");
        topicNames.add("Algebra");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e( "Fragment is visible", "Fragment is visible");
        QuestionsFragment.bottomSheetOpenTopics = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Fragment is not visible", "Fragment is not visible");
        QuestionsFragment.bottomSheetOpenTopics = false;
    }
    @Override
    public void dismiss(){
        super.dismiss();
    }
}
