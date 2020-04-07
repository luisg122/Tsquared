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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tsquared.Adapters.AnswerAdapter;
import com.example.tsquared.Models.AnswerModel;
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
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UserAnswersFragment extends BottomSheetDialogFragment {
    private BottomSheetBehavior bottomSheetBehavior;
    private static RequestParams params;
    private static AsyncHttpClient client;
    private static String URL2 = "http://207.237.59.117:8080/TSquared/platform?todo=findUserA";
    private static String URL  = "http://207.237.59.117:8080/TSquared/platform?todo=showAnswers";
    private View view;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ArrayList<AnswerModel> mArrayList;
    private RecyclerView mainRv;
    private AnswerAdapter adapter;

    @Override
    @Nullable
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        view = View.inflate(getContext(), R.layout.user_answers, null);
        bottomSheet.setContentView(view);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container1);
        setUpBottomSheetBehavior();
        setUpRecyclerView();

        Bundle mArgs = getArguments();
        assert mArgs != null; //avoids a null pointer exception
        String email = mArgs.getString("email");
        getUserAnswers(email);
        return bottomSheet;
    }

    private void setUpRecyclerView() {
        mainRv = view.findViewById(R.id.answersRV);
        mArrayList = new ArrayList<>();
        mainRv.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mainRv.setLayoutManager(layoutManager);
        adapter = new AnswerAdapter(mArrayList, getApplicationContext());
        mainRv.setAdapter(adapter);
    }

    private void setUpBottomSheetBehavior(){
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

    private void getUserAnswers(String profileEmail){
        params = new RequestParams();
        params.put("email", profileEmail);
        client = new AsyncHttpClient();

        client.get(URL2, params, new JsonHttpResponseHandler(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){
                Log.e("ANSWERS: ", response.toString());
                ArrayList<AnswerModel> answerList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object  = response.getJSONObject(i);
                        AnswerModel answer = AnswerModel.fromJson(object);
                        Drawable image     = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.blank_profile);
                        answer.setProfileImage(image);
                        answerList.add(answer);
                        //Log.e("ANSWERS", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.swapData(answerList);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "Failed to load Answer");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
    }
}