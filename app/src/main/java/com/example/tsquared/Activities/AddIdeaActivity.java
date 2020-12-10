package com.example.tsquared.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.tsquared.Fragments.LinkPromptBottomSheet;
import com.example.tsquared.R;
import com.example.tsquared.SharedPreference.DarkSharedPref;

public class AddIdeaActivity extends AppCompatActivity {
    private CardView cardView;
    private Toolbar  toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(DarkSharedPref.isDark){
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_idea);

        setUpViews();
        invokeIdeaCreate();
        setUpToolbar();
    }

    private void setUpViews(){
        cardView = (CardView) findViewById(R.id.addIdeaCV);
        toolbar  = (Toolbar)  findViewById(R.id.toolbar);
    }

    private void invokeIdeaCreate() {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddIdeaActivity.this, IdeaPostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void setUpToolbar(){
        toolbar.setTitle("Add new ideas");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }
}
