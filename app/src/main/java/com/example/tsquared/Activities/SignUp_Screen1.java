package com.example.tsquared.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tsquared.R;

public class SignUp_Screen1 extends AppCompatActivity implements View.OnClickListener{
    private Button nextButton;
    private Toolbar toolbar;
    private AlertDialog alertDialog;
    private Button continueButton;
    private Button quitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup1);
        setButtonViewAndListener();
        setUpToolBar();
    }

    private void setUpToolBar() {
        toolbar = findViewById(R.id.toolbarSignUp);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("Name");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void setButtonViewAndListener(){
        nextButton = findViewById(R.id.signUpButton1);
        nextButton.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            openDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void openDialog(){
        // Change from AlertDialog to Dialog for more compact features
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp_Screen1.this, R.style.CustomAlertDialog);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(SignUp_Screen1.this);
        View view2 = layoutInflaterAndroid.inflate(R.layout.alert_dialog, null);

        builder.setView(view2);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        Window window = alertDialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        continueButton = (Button) alertDialog.findViewById(R.id.continueButton);
        quitButton     = (Button) alertDialog.findViewById(R.id.quitButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent register = new Intent(SignUp_Screen1.this, SignUp_Screen2.class);
        register.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(register);
    }

    @Override
    public void finish(){
        super.finish();
        alertDialog.dismiss();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
