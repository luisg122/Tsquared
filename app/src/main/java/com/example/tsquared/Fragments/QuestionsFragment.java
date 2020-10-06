package com.example.tsquared.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.tsquared.Activities.DetailActivity;
import com.example.tsquared.Activities.DrawerActivity;
import com.example.tsquared.Activities.HideOrShowObject;
import com.example.tsquared.Activities.PostQuestionWindow;
import com.example.tsquared.Adapters.QuestionItemAdapter;
import com.example.tsquared.Models.QuestionItemModel;
import com.example.tsquared.Models.NewsHorizontalModel;
import com.example.tsquared.R;
import com.example.tsquared.RecyclerviewListeners.CustomScrollListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class QuestionsFragment<adapter> extends Fragment
        implements QuestionItemAdapter.OnNoteListener {

    private View view;
    private RecyclerView mainRv;
    private RecyclerView mainRv1;

    private ArrayList<QuestionItemModel> mArrayList;
    private ArrayList<NewsHorizontalModel> mArrayList1;
    private QuestionItemAdapter adapter;
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
    private CardView cardView;
    private TextView textPrompt;
    private DrawerActivity object;

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
        setUpSwipeContainer();
        setUpRecyclerView();
        loadListOfQuestions();
        setUpSwipeListener();
        return view;
    }

    private void setUpSwipeContainer() {
        /*swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer1);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.black
        );*/
    }

    private void setUpSwipeListener(){
        /*swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //loadListOfQuestions();
            }
        });*/
    }

    private void loadNameAndCollege(){
        DrawerActivity activity = (DrawerActivity) getActivity();
        assert activity != null;
        fullName = activity.getFullName();
        college  = activity.getCollege();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView(){
        dummyData();
        mainRv = view.findViewById(R.id.question_list_rv);
        mainRv.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);

        adapter = new QuestionItemAdapter(mArrayList, getApplicationContext(), this);
        mainRv.setAdapter(adapter);
        mainRv.setLayoutManager(layoutManager);

        // design to expand and shrink the extended fab button
        mainRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && DrawerActivity.fab != null) {
                    // Scrolled Downwards
                    DrawerActivity.fab.shrink();
                } else if (dy < 0 && DrawerActivity.fab != null) {
                    // Scrolled Upwards
                    DrawerActivity.fab.extend();
                }
            }
        });
    }


    private void dummyData(){
        mArrayList  = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            QuestionItemModel questionItem = new QuestionItemModel("John Doe", "Mathematics", "What are polynomials and why are they important",
                    "September 09 2020", "5", R.drawable.blank_profile);
            mArrayList.add(questionItem);
        }
    }


    private void loadListOfQuestions(){
        /*client = new AsyncHttpClient();
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
        });*/
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void OnNoteClick(int position) {
        /*String question = mArrayList.get(position).question;
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("question", question);
        startActivity(intent);*/
    }
}