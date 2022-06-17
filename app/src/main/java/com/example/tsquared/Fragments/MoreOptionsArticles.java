package com.example.tsquared.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.tsquared.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class MoreOptionsArticles extends BottomSheetDialogFragment {
    private View view;
    private ImageView cancel_IV;

    public MoreOptionsArticles(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.options_news_articles, container, false);

        // these lines are very important, want to make sure that all of the view from the BottomSheetDialog fully shown
        // or expanded
        Objects.requireNonNull(getDialog()).setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                assert bottomSheet != null;
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        setUpViews();
        setUpClickListeners();

        return view;
    }

    private void setUpViews(){
        cancel_IV = (ImageView) view.findViewById(R.id.cancel_button);
    }

    private void setUpClickListeners(){
        cancel_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e( "Fragment is visible", "Fragment is visible");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Fragment is not visible", "Fragment is not visible");
    }
    @Override
    public void dismiss(){
        super.dismiss();
    }
}
