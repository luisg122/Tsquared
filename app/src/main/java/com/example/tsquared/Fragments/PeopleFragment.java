package com.example.tsquared.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tsquared.Adapters.PeopleItemAdapter;
import com.example.tsquared.Models.PeopleItemModel;
import com.example.tsquared.Activities.PersonProfile;
import com.example.tsquared.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PeopleFragment extends Fragment implements PeopleItemAdapter.OnNoteListener {
    private View view;
    private RecyclerView mainRv;
    private PeopleItemAdapter adapter;
    private ArrayList<PeopleItemModel> mArrayList;
    private SwipeRefreshLayout swipeContainer;

    RequestParams params;
    AsyncHttpClient client;
    String URL = "http://207.237.59.117:8080/TSquared/platform?todo=showPeople";

    public PeopleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.people_profiles_list, container, false);
        setUpSwipeContainer();
        setUpRecyclerView();
        loadListOfPeople();
        setUpSwipeListener();
        return view;
    }

    private void setUpSwipeContainer() {
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer2);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
    }

    private void setUpSwipeListener() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadListOfPeople();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView() {
        mArrayList = new ArrayList<>();
        mainRv = view.findViewById(R.id.people_list_rv);
        mainRv.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        adapter = new PeopleItemAdapter(mArrayList, getApplicationContext(), this);
        mainRv.setAdapter(adapter);
        mainRv.setLayoutManager(layoutManager);
    }

    private void loadListOfPeople() {
        client = new AsyncHttpClient();
        client.get(URL, params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //super.onSuccess(statusCode, headers, response);
                Log.e("STRING DATA: ", response.toString());
                ArrayList<PeopleItemModel> peopleList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        PeopleItemModel people = PeopleItemModel.fromJson(object);
                        Drawable image = ContextCompat.getDrawable(getApplicationContext(), R.drawable.blank_profile);
                        people.setProfileImage(image);
                        peopleList.add(people);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.swapData(peopleList);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("TAG", "EMAIL: " + "dummy");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }

    @Override
    public void OnNoteClick(int position) {
        Intent intent = new Intent(getApplicationContext(), PersonProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        String name    = mArrayList.get(position).name;
        String college = mArrayList.get(position).college;
        String desc    = mArrayList.get(position).desc;
        String email   = mArrayList.get(position).email;
        intent.putExtra("name", name);
        intent.putExtra("college", college);
        intent.putExtra("desc", desc);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    /*@Override
    public void OnFollowClick(int position){
        final int pressedColor = this.getResources().getColor(R.color.mainColor);
        mArrayList.get(position);
        Toast.makeText(getApplicationContext(), "Position " + mArrayList.get(position), Toast.LENGTH_SHORT).show();
    }*/
}

/*
            Intent intent = new Intent(mcontext, PersonProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            String name    = tv_name.getText().toString();
            String college = tv_college.getText().toString();
            String desc    = tv_desc.getText().toString();

            intent.putExtra("name", name);
            intent.putExtra("college", college);
            intent.putExtra("desc", desc);
            intent.putExtra("email", email);
            mcontext.startActivity(intent);
 */
