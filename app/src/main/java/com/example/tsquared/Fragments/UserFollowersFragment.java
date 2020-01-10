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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsquared.Adapters.Following_Followers_Adapter;
import com.example.tsquared.Models.PeopleItemModel;
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
import static java.util.Objects.requireNonNull;

public class UserFollowersFragment extends BottomSheetDialogFragment
        implements Following_Followers_Adapter.OnNoteListener {
    private BottomSheetBehavior bottomSheetBehavior;
    private static RequestParams params;
    private static AsyncHttpClient client;
    private BottomSheetDialog bottomSheet;
    private ArrayList<PeopleItemModel> mArrayList;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView mainRv;
    private Following_Followers_Adapter adapter;
    private View view;
    private static String URL4 = "http://207.237.59.117:8080/TSquared/platform?todo=findFollowers";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    @Nullable
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        view = View.inflate(getContext(), R.layout.user_followers, null);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container1);
        setUpBottomSheetBehavior();
        setUpRecyclerView();

        Bundle mArgs = getArguments();
        assert mArgs != null; //avoids a null pointer exception
        String email = mArgs.getString("email");
        getUserFollowers(email);
        return bottomSheet;
    }

    private void setUpBottomSheetBehavior() {
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (BottomSheetBehavior.STATE_HIDDEN == i) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    private void getUserFollowers(String profileEmail) {
        params = new RequestParams();
        params.put("email", profileEmail);
        client = new AsyncHttpClient();

        client.get(URL4, params, new JsonHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.e("FOLLOWERS: ", response.toString());
                ArrayList<PeopleItemModel> peopleList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        PeopleItemModel people = PeopleItemModel.fromJson(object);
                        Drawable image = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.blank_profile);
                        people.setProfileImage(image);
                        peopleList.add(people);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.swapData(peopleList);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "Failed to load Followers");
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
        DividerItemDecoration divider = new
                DividerItemDecoration(mainRv.getContext(),
                DividerItemDecoration.VERTICAL);
        divider.setDrawable(requireNonNull(ContextCompat.getDrawable(requireNonNull(this.getContext()),
                R.drawable.line_divider)));
        mainRv.addItemDecoration(divider);
        adapter = new Following_Followers_Adapter(mArrayList, getApplicationContext(), this);
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

    }
}