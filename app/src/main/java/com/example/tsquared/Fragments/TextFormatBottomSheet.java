package com.example.tsquared.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.tsquared.Adapters.FontsAdapter;
import com.example.tsquared.Adapters.QuestionItemAdapter;
import com.example.tsquared.Adapters.TopicsAdapter;
import com.example.tsquared.Models.FontModel;
import com.example.tsquared.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TextFormatBottomSheet extends BottomSheetDialogFragment {
    private View view;
    private ImageView cancel_IV;
    private Slider slider;
    private RecyclerView recyclerView;
    private FontsAdapter fontsAdapter;
    private ArrayList<FontModel> fonts;
    private TextFormatChangeListener textFormatChangeListener;

    @Override
    public void onCreate(Bundle savedOnInstanceState){
        super.onCreate(savedOnInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.text_accessibility_modal, container, false);

        Objects.requireNonNull(getDialog()).setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                assert bottomSheet != null;
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        setUpViews();
        setUpRecyclerView();
        setUpClickListeners();
        return view;
    }

    private void setUpViews(){
        cancel_IV = (ImageView) view.findViewById(R.id.cancel_button);
        slider = (Slider) view.findViewById(R.id.slider);
        recyclerView = (RecyclerView) view.findViewById(R.id.fontsRV);
    }

    private void setUpClickListeners(){
        cancel_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                if(textFormatChangeListener != null)
                    textFormatChangeListener.changeTextSize(value);
            }
        });
    }

    private void setUpRecyclerView(){
        dummyData();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        fontsAdapter = new FontsAdapter(fonts);
        recyclerView.setAdapter(fontsAdapter);
    }

    private void dummyData(){
        fonts = new ArrayList<>();
        fonts.add(new FontModel("Ag", "Sans-serif"));
        fonts.add(new FontModel("Ag", "Sans-serif"));
        fonts.add(new FontModel("Ag", "Sans-serif"));
        fonts.add(new FontModel("Ag", "Sans-serif"));
        fonts.add(new FontModel("Ag", "Sans-serif"));
    }

    public void setTextFormatChangeListener(TextFormatChangeListener textFormatChangeListener){
        this.textFormatChangeListener = textFormatChangeListener;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void dismiss(){
        super.dismiss();
    }

    public interface TextFormatChangeListener {
        void changeTextSize(float value);
        void changeTextFont();
    }
}
