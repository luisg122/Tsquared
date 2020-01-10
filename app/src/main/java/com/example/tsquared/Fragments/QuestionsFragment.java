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

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.example.tsquared.Activities.DetailActivity;
import com.example.tsquared.Activities.DrawerActivity;
import com.example.tsquared.Activities.QuestionWindow;
import com.example.tsquared.Adapters.QuestionItemAdapter;
import com.example.tsquared.Models.QuestionItemModel;
import com.example.tsquared.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;
import static java.util.Objects.*;

public class QuestionsFragment<adapter> extends Fragment implements QuestionItemAdapter.OnNoteListener {

    private View view;
    private RecyclerView mainRv;
    private QuestionItemAdapter adapter;
    private FloatingActionButton fab;

    private ArrayList<QuestionItemModel> mArrayList;
    private SwipeRefreshLayout swipeContainer;
    private FloatingSearchView mSearchView;
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;


    boolean mIsDarkSearchTheme = false;
    private String mLastQuery = "";

    private String fullName;
    private String college;

    private ShimmerFrameLayout shimmerFrameLayout;
    private RequestParams params, params1;
    private AsyncHttpClient client, client1;
    private String URL = "http://207.237.59.117:8080/TSquared/platform?todo=showQuestions";
    public QuestionsFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.item_main, container, false);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        // Correct sequence of function calls
        setUpSwipeContainer();
        setUpSearchListener();
        setupFloatingButtonAction();
        setUpRecyclerView();
        loadListOfQuestions();
        setUpSwipeListener();
        return view;
    }

    private void setUpSearchListener() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this.getActivity());
        View view = layoutInflaterAndroid.inflate(R.layout.drawer_activity, null);
        mSearchView = view.findViewById(R.id.searchView);

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked (final SearchSuggestion searchSuggestion){
            }

            @Override
            public void onSearchAction (String query){
                mLastQuery = query;
                adapter.findQuestions(getApplicationContext(), query,
                    new QuestionItemAdapter.OnFindQuestionsListener(){
                    @Override
                    public void onResults(ArrayList<QuestionItemModel>results){
                        adapter.swapData(results);
                    }
                });
                Log.d("TAG", "onSearchAction()");
            }
        });
    }

    private void setUpSwipeContainer() {
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer1);
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
                loadListOfQuestions();
            }
        });
    }

    private void setupFloatingButtonAction() {
        fab = view.findViewById(R.id.FAB);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                loadNameAndCollege();
                Intent questionWindow = new Intent(getApplicationContext(), QuestionWindow.class);
                questionWindow.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                questionWindow.putExtra("Full Name", fullName);
                questionWindow.putExtra("College", college);
                startActivity(questionWindow);
            }
        });
    }

    private void loadNameAndCollege(){
        DrawerActivity activity = (DrawerActivity) getActivity();
        assert activity != null;
        fullName = activity.getFullName();
        college  = activity.getCollege();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView(){
        mArrayList = new ArrayList<>();
        mainRv = view.findViewById(R.id.question_list_rv);
        mainRv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);
        mainRv.setLayoutManager(layoutManager);

        adapter = new QuestionItemAdapter(mArrayList, getApplicationContext(), this);
        mainRv.setAdapter(adapter);

        // Hide the floating action button when scrolling, purely for design purposes
        mainRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 && fab.getVisibility() == View.VISIBLE){
                    fab.hide();
                }
                else if(dy < 0 && fab.getVisibility() != View.VISIBLE){
                    fab.show();
                }
            }
        });
    }

    private void loadListOfQuestions(){
        client = new AsyncHttpClient();
        client.get(URL, params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //super.onSuccess(statusCode, headers, response);
                Log.e("STRING DATA: ", response.toString());
                ArrayList<QuestionItemModel> questionList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        QuestionItemModel question = QuestionItemModel.fromJson(object);
                        Drawable image = ContextCompat.getDrawable(requireNonNull(getApplicationContext()), R.drawable.blank_profile);
                        question.setProfileImage(image);
                        questionList.add(question);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                adapter.swapData(questionList);
                swipeContainer.setRefreshing(false);

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "EMAIL: " + "dummy");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause(){
        super.onPause();
        shimmerFrameLayout.stopShimmer();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnNoteClick(int position) {
        String question = mArrayList.get(position).question;
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("question", question);
        startActivity(intent);
    }
}