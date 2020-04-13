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

import java.util.Objects;

public class SignUp_Screen2 extends AppCompatActivity implements View.OnClickListener{
    private Button   nextButton;
    private Toolbar  toolbar;
    private TextInputEditText email;
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

    public void setUpEmail(){
        email = findViewById(R.id.registerEmail);
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
        String emailCheck = Objects.requireNonNull(email.getText()).toString().trim();
        if(emailCheck.isEmpty()){
            noFieldsEmptyDialog();
        }

        else {
            Intent register = new Intent(SignUp_Screen2.this, SignUp_Screen3.class);
            register.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            register.putExtra("email", emailCheck);
            startActivity(register);
            // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            // finish();
        }
    }

    public void noFieldsEmptyDialog(){
        // Change from AlertDialog to Dialog for more compact features
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp_Screen2.this, R.style.CustomAlertDialog);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(SignUp_Screen2.this);
        View view2 = layoutInflaterAndroid.inflate(R.layout.general_alert_dialog, null);
        TextView textView = (TextView) view2.findViewById(R.id.titlePrompt);
        textView.setText("Fields cannot be empty");

        builder.setCustomTitle(view2);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        Window window = alertDialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        okResponse = (Button) alertDialog.findViewById(R.id.okResponse);
        okResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
