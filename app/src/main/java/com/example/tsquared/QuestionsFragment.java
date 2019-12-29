package com.example.tsquared;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;

public class QuestionsFragment extends Fragment {

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


    RequestParams params, params1;
    AsyncHttpClient client, client1;
    String URL = "http://207.237.59.117:8080/TSquared/platform?todo=showQuestions";
    String ANONYMOUS = "Anonymous";

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

        // Correct sequence of function calls
        setUpSwipeContainer();
        setUpSearchListener();
        setUpRecyclerView();
        setupFloatingButtonAction();
        loadListOfQuestions();
        setUpSwipeListener();

        adapter = new QuestionItemAdapter(mArrayList, getContext());
        mainRv.setAdapter(adapter);

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
        mainRv = view.findViewById(R.id.question_list_rv);
        mainRv.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mainRv.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new
                DividerItemDecoration(mainRv.getContext(),
                DividerItemDecoration.VERTICAL);
        divider.setDrawable((Objects.requireNonNull(ContextCompat.getDrawable(Objects.requireNonNull((this).getContext()),
                R.drawable.line_divider))));
        mainRv.addItemDecoration(divider);

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
        mArrayList = new ArrayList<>();
        client = new AsyncHttpClient();
        client.get(URL, params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //super.onSuccess(statusCode, headers, response);
                Log.e("STRING DATA: ", response.toString());
                try {
                    for (int i = 0; i < response.length(); i++){
                        JSONObject object  = response.getJSONObject(i);
                        String name        = object.getString("PostedBy");
                        String topic       = object.getString("QuestionDetails");
                        String question    = object.getString("Content");
                        String dateSubmit  = object.getString("DatePosted");
                        String responseNum = object.getString("ResponseNumber");
                        int    isAnonymous = object.getInt("isAnonymous");
                        Log.d("DATA QUESTION: ", name + " " + topic + " " + question + " " + dateSubmit + " " + isAnonymous);
                        Drawable image     = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.blank_profile);

                        name  = capitalizeFirstCharOfEveryWordInString(name);
                        topic = capitalizeFirstCharOfEveryWordInString(topic);

                        if(isAnonymous == 1){
                            QuestionItemModel data = new QuestionItemModel(ANONYMOUS, topic, question, dateSubmit, responseNum, image);
                            mArrayList.add(data);
                        }
                        else if(isAnonymous == 0){
                            QuestionItemModel data = new QuestionItemModel(name, topic, question, dateSubmit, responseNum, image);
                            mArrayList.add(data);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new QuestionItemAdapter(mArrayList, getContext());
                adapter.notifyDataSetChanged();
                mainRv.setAdapter(adapter);
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