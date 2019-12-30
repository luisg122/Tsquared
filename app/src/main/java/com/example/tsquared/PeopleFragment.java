package com.example.tsquared;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;

public class PeopleFragment extends Fragment {
    private View view;
    private RecyclerView mainRv;
    private PeopleItemAdapter adapter;
    private ArrayList<PeopleItemModel> mArrayList;
    private SwipeRefreshLayout swipeContainer;

    RequestParams params;
    AsyncHttpClient client;
    String URL = "http://207.237.59.117:8080/TSquared/platform?todo=showPeople";

    public PeopleFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.people_profiles_list, container, false);
        setUpSwipeContainer();
        setUpRecyclerView();
        loadListOfPeople();
        setUpSwipeListener();

        adapter = new PeopleItemAdapter(mArrayList, getContext());
        mainRv.setAdapter(adapter);
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

    private void setUpSwipeListener(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadListOfPeople();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView(){
        mainRv = view.findViewById(R.id.people_list_rv);
        mainRv.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mainRv.setLayoutManager(layoutManager);
    }

    private void loadListOfPeople(){
        mArrayList = new ArrayList<>();
        client = new AsyncHttpClient();
        client.get(URL, params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //super.onSuccess(statusCode, headers, response);
                Log.e("STRING DATA: ", response.toString());
                try {
                    //Log.e("ELEMENT: ", response.getString("Question1"));
                    for (int i = 0; i < response.length(); i++){
                        JSONObject object = response.getJSONObject(i);
                        String fullName   = object.getString("FullName");
                        String college    = object.getString("College");
                        String userDesc   = object.getString("Description");
                        String userEmail  = object.getString("Email");
                        Log.d("DATA People: ", fullName + " " + college + " " + userDesc + " " + userEmail);
                        Drawable image     = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.blank_profile);

                        fullName = capitalizeFirstCharOfEveryWordInString(fullName);
                        college  = capitalizeFirstCharOfEveryWordInString(college);
                        PeopleItemModel data = new PeopleItemModel(fullName,college, userDesc, image);
                        mArrayList.add(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new PeopleItemAdapter(mArrayList, getContext());
                adapter.notifyDataSetChanged();
                mainRv.setAdapter(adapter);
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

    private String capitalizeFirstCharOfEveryWordInString(String string){
        char[] ch = string.toCharArray();
        for(int i = 0; i < string.length(); i++) {
            // Find first char of a word
            // Make sure the character does not equal a space
            if (i == 0 && ch[i] != ' ' || ch[i] != ' ' && ch[i - 1] == ' ') {
                // If such character is lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {
                    // simply convert it into upper-case
                    // refer to the ASCII table to understand this line of code
                    ch[i] = (char) (ch[i] - 'a' + 'A');
                }
            }
            else if (ch[i] >= 'A' && ch[i] <= 'Z'){
                ch[i] = (char) (ch[i] + 'a' - 'A');
            }
        }
        return new String(ch);
    }
}