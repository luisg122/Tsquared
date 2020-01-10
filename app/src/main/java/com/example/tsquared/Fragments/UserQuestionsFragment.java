package com.example.tsquared.Fragments;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsquared.Adapters.QuestionItemAdapter;
import com.example.tsquared.Models.QuestionItemModel;
import com.example.tsquared.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;
import static java.util.Objects.requireNonNull;


public class UserQuestionsFragment extends BottomSheetDialogFragment implements QuestionItemAdapter.OnNoteListener {
    private static RequestParams params;
    private static AsyncHttpClient client;
    private static String URL1 = "http://207.237.59.117:8080/TSquared/platform?todo=findUserQ";
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetDialog bottomSheet;
    private ArrayList<QuestionItemModel> mArrayList;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView mainRv;
    private QuestionItemAdapter adapter;
    private View view;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    @Nullable
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        view = View.inflate(getContext(), R.layout.user_questions, null);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container1);
        setUpBottomSheetBehavior();
        setUpRecyclerView();

        // obtaining the user's email address through a bundle
        Bundle mArgs = getArguments();
        assert mArgs != null; //avoids a null pointer exception
        String email = mArgs.getString("email");
        getUserQuestions(email);
        return bottomSheet;
    }

    private void setUpBottomSheetBehavior() {
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if(BottomSheetBehavior.STATE_HIDDEN == i){
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    private void getUserQuestions(String profileEmail){
        params = new RequestParams();
        params.put("email", profileEmail);
        client = new AsyncHttpClient();

        client.get(URL1, params, new JsonHttpResponseHandler(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.e("QUESTIONS: ", response.toString());
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
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "Failed to load Questions");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setUpRecyclerView() {
        mainRv = view.findViewById(R.id.answersRV);
        mArrayList = new ArrayList<>();
        mainRv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mainRv.setLayoutManager(layoutManager);
        adapter = new QuestionItemAdapter(mArrayList, getApplicationContext(), this);
        mainRv.setAdapter(adapter);
    }

    @Override
    public void OnNoteClick(int position) {

    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
        adapter.notifyDataSetChanged();
    }
}