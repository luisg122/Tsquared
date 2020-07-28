package com.example.tsquared.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tsquared.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SignUp_Screen2 extends AppCompatActivity implements View.OnClickListener{
    private Button   nextButton;
    private Toolbar  toolbar;
    private TextInputEditText email;
    private TextInputLayout   emailLayout;

    private AlertDialog alertDialog;
    private Button okResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup2);
        setButtonViewAndListener();
        setUpEmail();
        setUpToolBar();
    }

    private boolean checkInput(){
        if(email.getText().toString().trim().isEmpty()){
            emailLayout.setError("Email cannot be empty");
            return false;
        }

        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
            emailLayout.setError("Please enter a valid email address");
            return false;
        }

        else {
            emailLayout.setErrorEnabled(false);
            return true;
        }
    }

    public void setUpEmail(){
        email = findViewById(R.id.registerEmail);
        emailLayout = findViewById(R.id.emailLayout);
    }

    private void setUpToolBar() {
        toolbar = findViewById(R.id.toolbarSignUp);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("Email");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setButtonViewAndListener() {
        nextButton = findViewById(R.id.signUpButton2);
        nextButton.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if(checkInput()){
            Intent register = new Intent(SignUp_Screen2.this, SignUp_Screen3.class);
            register.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            register.putExtra("email", Objects.requireNonNull(email.getText()).toString().trim());
            startActivity(register);
            // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            // finish();
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
