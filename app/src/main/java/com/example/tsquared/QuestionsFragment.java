package com.example.tsquared;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class QuestionsFragment extends Fragment {

    private View view;
    private RecyclerView mainRv;
    private QuestionItemAdapter adapter;
    private FloatingActionButton fab;
    private Button cancel, post;
    public ArrayList<QuestionItemModel> mArrayList;
    public AlertDialog alertDialog;


    public QuestionsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.item_main, container, false);
        setUpRecyclerView();
        setupFloatingButtonAction();

        loadNormalList();
        adapter = new QuestionItemAdapter(mArrayList, this.getActivity());
        mainRv.setAdapter(adapter);

        return view;
    }

    private void setupFloatingButtonAction() {
        fab = view.findViewById(R.id.FAB);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    public void openDialog(){
        // Change from AlertDialog to Dialog for more compact features

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity(), R.style.CustomAlertDialog);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this.getActivity());
        View view2 = layoutInflaterAndroid.inflate(R.layout.floating_dialog_post, null);

        builder.setView(view2);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        Window window = alertDialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 1000);

        post   = (Button) alertDialog.findViewById(R.id.submitPostButton);
        cancel = (Button) alertDialog.findViewById(R.id.cancelSubmitButton);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Canceled();
            }
        });
    }

    public void Canceled(){
        alertDialog.dismiss();
    }

    private ArrayList<QuestionItemModel> loadNormalList() {
            mArrayList = new ArrayList<>();


            mArrayList.add(new QuestionItemModel("Luis Gualpa", "Calculus", "What is the idea behind scrum and how is it efficiently applied?", "12/17/19", "50 Answers", R.drawable.brian));
            mArrayList.add(new QuestionItemModel("John Smith", "Algebra", "What is the meaning of life?", "12/17/19", "50 Answers", R.drawable.brian));
            mArrayList.add(new QuestionItemModel("Jack Black", "Java", "What is the meaning of life?", "12/17/19", "50 Answers", R.drawable.brian));
            mArrayList.add(new QuestionItemModel("Izzy Doe", "C++", "What is the meaning of life?", "12/17/19", "50 Answers", R.drawable.brian));
            mArrayList.add(new QuestionItemModel("Luis Gualpa", "Python", "What is the meaning of life?", "12/17/19", "50 Answers", R.drawable.brian));
            mArrayList.add(new QuestionItemModel("John Smith", "Software Engineering", "What is the meaning of life?", "12/17/19", "50 Answers", R.drawable.brian));
            mArrayList.add(new QuestionItemModel("Jack Black", "Theory of Computation", "What is the meaning of life?", "12/17/19", "50 Answers", R.drawable.brian));
            mArrayList.add(new QuestionItemModel("Izzy Doe", "Computer Architecture", "What is the meaning of life?", "12/17/19", "50 Answers", R.drawable.brian));

        return mArrayList;
    }

    private void setUpRecyclerView(){
        mainRv = view.findViewById(R.id.question_list_rv);
        mainRv.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mainRv.setLayoutManager(layoutManager);

    }
}