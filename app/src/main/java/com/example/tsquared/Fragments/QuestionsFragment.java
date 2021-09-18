package com.example.tsquared.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.tsquared.Activities.AnswersActivity;
import com.example.tsquared.Activities.DrawerActivity;
import com.example.tsquared.Adapters.QuestionItemAdapter;
import com.example.tsquared.Models.QuestionItemImageModel;
import com.example.tsquared.Models.QuestionItemTextModel;
import com.example.tsquared.Models.NewsHorizontalModel;
import com.example.tsquared.Models.QuestionItemUrlModel;
import com.example.tsquared.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class QuestionsFragment extends Fragment
        implements QuestionItemAdapter.OnNoteListener {

    private View view;
    private RecyclerView mainRv;
    private RecyclerView mainRv1;

    private ArrayList<Object> mArrayList;
    private Handler handler;
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
    public  static  final int REQUEST_CODE_ADD_NOTE = 1;

    public QuestionsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.item_main, container, false);
        setUpViews();
        initializeHandler();
        setUpSwipeContainer();
        setUpRecyclerView();
        loadListOfQuestions();
        setUpSwipeListener();
        return view;
    }

    private void setUpViews(){
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
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
        mainRv.setLayoutManager(layoutManager);

        adapter = new QuestionItemAdapter(mArrayList, getApplicationContext(), this);
        mainRv.setAdapter(adapter);
        // design to expand and shrink the extended fab button
        mainRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
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
            QuestionItemTextModel questionItem   = new QuestionItemTextModel("Mathematics", "What are polynomials and why are they important?",
                    "September 09 2020", "5", R.mipmap.blank_profile);
            mArrayList.add(questionItem);

            QuestionItemImageModel questionItem1 = new QuestionItemImageModel("Mathematics", "What are polynomials and why are they important?",
                    "September 09 2020", "5", "profileUrlImage", "imageUrl");
            mArrayList.add(questionItem1);

            QuestionItemUrlModel questionItem2 = new QuestionItemUrlModel("Mathematics", "Are the 2020 Tokyo Olympics going to have spectators or no?",
                    "September 09 2020", "5", "profileUrlImage", "imageUrl", "Tokyo covid levels rises", "BBC.com");

            mArrayList.add(questionItem2);
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
        // shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause(){
        super.onPause();
        // shimmerFrameLayout.stopShimmer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void OnNoteClick(int position) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mArrayList.get(position) instanceof QuestionItemTextModel){
                    QuestionItemTextModel item = (QuestionItemTextModel) mArrayList.get(position);
                    Intent intent = new Intent(getApplicationContext(), AnswersActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("questionId", "1234");
                    intent.putExtra("question", item.getQuestion());
                    intent.putExtra("numberOfAnswers", item.getResponseNum());
                    intent.putExtra("type", "QuestionItemTextModel");
                    startActivity(intent);
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

                else if(mArrayList.get(position) instanceof  QuestionItemImageModel){
                    QuestionItemImageModel item = (QuestionItemImageModel) mArrayList.get(position);
                    Intent intent = new Intent(getApplicationContext(), AnswersActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("questionId", "1234");
                    intent.putExtra("question", item.getQuestion());
                    intent.putExtra("numberOfAnswers", item.getResponseNum());
                    intent.putExtra("type", "QuestionItemImageModel");
                    startActivity(intent);
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

                else {
                    QuestionItemUrlModel item = (QuestionItemUrlModel) mArrayList.get(position);
                    Intent intent = new Intent(getApplicationContext(), AnswersActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("questionId", "1234");
                    intent.putExtra("question", item.getQuestion());
                    intent.putExtra("numberOfAnswers", item.getResponseNum());
                    intent.putExtra("type", "QuestionItemUrlModel");
                    startActivity(intent);
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        }, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK){

        }
    }
}