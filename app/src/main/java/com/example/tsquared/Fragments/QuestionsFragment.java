package com.example.tsquared.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.tsquared.Activities.AnswersActivity;
import com.example.tsquared.Activities.DrawerActivity;
import com.example.tsquared.Adapters.QuestionItemAdapter;
import com.example.tsquared.Models.QuestionItemModel;
import com.example.tsquared.Models.NewsHorizontalModel;
import com.example.tsquared.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class QuestionsFragment extends Fragment
        implements QuestionItemAdapter.OnNoteListener, DrawerActivity.LoadNewQuestionListener{

    public static Boolean bottomSheetOpen = false;
    public static Boolean bottomSheetOpenTopics = false;

    private int saveClickCounter;

    private View view;
    private RecyclerView mainRv;
    private ArrayList<QuestionItemModel> mArrayList;
    private Handler handler;
    private QuestionItemAdapter adapter;

    private SwipeRefreshLayout swipeContainer;
    private FloatingSearchView mSearchView;
    private ArrayList<NewsHorizontalModel> mArrayList1;
    private RecyclerView mainRv1;

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
        view = inflater.inflate(R.layout.questions_list, container, false);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView(){
        dummyData();

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);

        mainRv = view.findViewById(R.id.question_list_rv);
        mainRv.setLayoutManager(layoutManager);
        mainRv.setHasFixedSize(false);

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
        for(int i = 0; i < 10; i++){
            QuestionItemModel questionItem = new QuestionItemModel(
                    "Science", "What has happened to Opportunity in Mars, and are there other explorers out there as well?",
                    "I understand Opportunity is a Rover set out to explore Mars, but how many of them are there exactly is what" +
                            "I wish to know, I also imagine the cost to manufacture these rovers are quite costly",
                    "Jan 02 2024", "5", "profileImage", "profileName",
                    null, null, null, null);
            mArrayList.add(questionItem);

            QuestionItemModel questionItem1 = new QuestionItemModel(
                    "Politics", "How well televised was the last Winter's Olympics?",
                    null, "Jan 09 2024", "7", "profileUrlImage", "profileName",
                    null, "imageUrl", "headline", "source");

            mArrayList.add(questionItem1);

            QuestionItemModel questionItem2 = new QuestionItemModel(
                    "Mathematics", "Is One Piece mid? How does it compare to Naruto in terms of character development?",
                    "I acknowledge that One Piece is not indeed mid, but other people are calling it so. I personally on the fence about it" +
                            "I think if the anime consists of character with personal growth, then it's an anime worth watching.",
                    "Jan 21 2024", "13", "profileImage", "profileName",
                    "https://platform.polygon.com/wp-content/uploads/sites/2/chorus/uploads/chorus_asset/file/25132508/one_piece_luffy_new_season.jpg?quality=90&strip=all&crop=0,3.4613147178592,100,93.077370564282", null, null, null);

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
                if(mArrayList.get(position) instanceof QuestionItemModel){
                    QuestionItemModel item = (QuestionItemModel) mArrayList.get(position);
                    Intent intent = new Intent(getApplicationContext(), AnswersActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("questionId", "1234");
                    intent.putExtra("question", item.getPostTitle());
                    intent.putExtra("numberOfAnswers", item.getResponseNum());
                    intent.putExtra("type", "QuestionItemTextModel");
                    startActivity(intent);
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        }, 100);
    }

    @Override
    public void OnMoreIconClick(int position) {
        if(saveClickCounter++ == 0){
            MoreOptionsQuestions bottomSheet = new MoreOptionsQuestions();
            bottomSheet.show(requireActivity().getSupportFragmentManager(), bottomSheet.getTag());

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveClickCounter = 0;
                }
            }, 1000);
        }

    }

    @Override
    public void OnMoreTopics(int position) {
        if(saveClickCounter++ == 0){
            TopicsBottomSheet bottomSheet = new TopicsBottomSheet();
            bottomSheet.show(requireActivity().getSupportFragmentManager(), bottomSheet.getTag());

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveClickCounter = 0;
                }
            }, 1000);
        }
    }

    @Override
    public void insertNewQuestion(final Intent postData) {
        if(mArrayList == null || adapter == null) {
            return;
        }

        assert postData != null;
        final String postTitle = postData.getStringExtra("PostTitle");
        final String postInformation = postData.getStringExtra("PostInformation");

        final QuestionItemModel questionItem = new QuestionItemModel(
                "Music", postTitle, postInformation, "Oct 30 2021",
                "5", "profileImage" , "Luis Gualpa",
                null, null, null, null);

        mArrayList.add(0, questionItem);
        adapter.notifyItemInserted(0);

        // do database stuff here, need the email address to associate question with user
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            ((DrawerActivity) requireActivity()).setLoadNewQuestionListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
}