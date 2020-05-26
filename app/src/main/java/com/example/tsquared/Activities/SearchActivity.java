package com.example.tsquared.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.tsquared.Adapters.QuestionItemAdapter;
import com.example.tsquared.Adapters.SearchResultsAdapter;
import com.example.tsquared.Adapters.ViewPagerAdapter;
import com.example.tsquared.Fragments.SearchQuestionsFragment;
import com.example.tsquared.Models.QuestionItemModel;
import com.example.tsquared.R;
import com.example.tsquared.ViewPager.CustomViewPager;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;
import static java.util.Objects.requireNonNull;

public class SearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imageView;
    private ArrayList<QuestionItemModel> questionList;
    private RequestParams params;
    private AsyncHttpClient client;
    private String URL = "http://207.237.59.117:8080/TSquared/platform?todo=qKeywords";

    RecyclerView mainRv;
    SearchResultsAdapter adapter;
    ArrayList<QuestionItemModel> mArrayList;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        setUpViews();
        setSupportActionBar(toolbar);
        setUpListeners();
        viewPagerInit();
    }

    private void setUpViews() {
        toolbar = findViewById(R.id.searchToolBar1);
        imageView = findViewById(R.id.cancelSearch);
    }

    private void setUpListeners(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void viewPagerInit(){
        CustomViewPager viewPager = findViewById(R.id.mainViewPager);
        setUpViewPager(viewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        viewPager.setCurrentItem(getIntent().getIntExtra("page", 0));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchQuestionsFragment(), "Questions");
        adapter.addFragment(new SearchQuestionsFragment(), "People");
        adapter.addFragment(new SearchQuestionsFragment(), "Groups");
        adapter.addFragment(new SearchQuestionsFragment(), "Articles");
        adapter.addFragment(new SearchQuestionsFragment(), "Interests");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        SearchView searchView = findViewById(R.id.search_view);
        //searchView.onActionViewExpanded(); // Keyboard is opened by default when user opens SearchActivity
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuestionsByKeywords(newText);
                return false;
            }
        });

        //How do we clear the search results when we press on the X (close Icon) button?
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mArrayList.clear();
                adapter = new SearchResultsAdapter(mArrayList, getApplicationContext());
                return false;
            }
        });
        return true;
    }

    private void searchQuestionsByKeywords(String currentQuery){
        questionList = new ArrayList<>();
        params = new RequestParams();
        params.put("query", currentQuery);
        client = new AsyncHttpClient();

        client.get(URL, params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Log.e("Keywords Found In ", response.toString());
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
                adapter.swapData(questionList);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("KeyQuestion", "Failed to Find");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }
}
//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);