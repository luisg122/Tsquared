package com.example.tsquared;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PeopleFragment extends Fragment {
    private View view;
    private RecyclerView mainRv;
    private PeopleItemAdapter adapter;
    private ArrayList<PeopleItemModel> mArrayList;

    public PeopleFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.people_profiles_list, container, false);
        setUpRecyclerView();

        loadNormalList();
        adapter = new PeopleItemAdapter(mArrayList, this.getActivity());
        mainRv.setAdapter(adapter);

        return view;
    }

    private ArrayList<PeopleItemModel> loadNormalList(){
        mArrayList = new ArrayList<>();

        mArrayList.add(new PeopleItemModel("Luis Gualpa", "Queens College", "First Year college student majoring in Computer Science", R.drawable.brian));
        mArrayList.add(new PeopleItemModel("John Smith", "Queens College", "First Year college student majoring in Computer Science", R.drawable.brian));
        mArrayList.add(new PeopleItemModel("Jack Black", "Queens College", "First Year college student majoring in Computer Science", R.drawable.brian));
        mArrayList.add(new PeopleItemModel("Izzy Doe", "Queens College", "First Year college student majoring in Computer Science", R.drawable.brian));
        mArrayList.add(new PeopleItemModel("Luis Gualpa", "Queens College", "First Year college student majoring in Computer Science",  R.drawable.brian));
        mArrayList.add(new PeopleItemModel("John Smith", "Queens College", "First Year college student majoring in Computer Science", R.drawable.brian));
        mArrayList.add(new PeopleItemModel("Jack Black", "Queens College", "First Year college student majoring in Computer Science", R.drawable.brian));
        mArrayList.add(new PeopleItemModel("Izzy Doe", "Queens College", "First Year college student majoring in Computer Science",  R.drawable.brian));

        return mArrayList;
    }


    private void setUpRecyclerView(){
        mainRv = view.findViewById(R.id.people_list_rv);
        mainRv.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mainRv.setLayoutManager(layoutManager);
    }
}
