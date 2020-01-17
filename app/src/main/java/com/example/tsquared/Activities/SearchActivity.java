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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsquared.Adapters.QuestionItemAdapter;
import com.example.tsquared.Adapters.SearchResultsAdapter;
import com.example.tsquared.Models.QuestionItemModel;
import com.example.tsquared.R;
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
        toolbar = findViewById(R.id.searchToolBar1);
        setSupportActionBar(toolbar);
        setUpRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        SearchView searchView = findViewById(R.id.search_view);
        searchView.onActionViewExpanded(); // Keyboard is opened by default when user opens SearchActivity
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView(){
        mArrayList = new ArrayList<>();
        mainRv = findViewById(R.id.results_rv);
        mainRv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);

        adapter = new SearchResultsAdapter(mArrayList, getApplicationContext());
        mainRv.setAdapter(adapter);
        mainRv.setLayoutManager(layoutManager);
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
}

/*MenuInflater inflater = getMenuInflater();
  inflater.inflate(R.menu.main_menu_search, menu);
  SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
  SearchView searchView = (SearchView) menu.findItem(R.id.search1).getActionView();
  searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
  searchView.setIconifiedByDefault(true);
  searchView.setIconified(false);
  searchView.setFocusable(true);
  searchView.requestFocusFromTouch();
  return true;*/

/*MenuInflater inflater = getMenuInflater();
  inflater.inflate(R.menu.main_menu_search, menu);
  MenuItem searchItem = menu.findItem(R.id.search1);
  SearchView searchView = (SearchView) searchItem.getActionView();
  searchView.setQueryHint(getString(R.string.hint));
  searchView.setIconifiedByDefault(false);
  searchView.setIconified(false);
  searchView.onActionViewExpanded();
  searchItem.expandActionView();*/