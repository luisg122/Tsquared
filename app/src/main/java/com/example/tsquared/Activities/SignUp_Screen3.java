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
import androidx.core.app.ActivityCompat;

import com.example.tsquared.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class SignUp_Screen3 extends AppCompatActivity implements View.OnClickListener{
    private Button nextButton;
    private Toolbar toolbar;
    private AlertDialog alertDialog;
    private Button okResponse;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup3);
        setButtonViewAndListener();
        setUpPassword();
        setUpToolBar();
    }

    public void setUpPassword(){
        password = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.confirmRegisterPassword);
    }

    private void setUpToolBar() {
        toolbar = findViewById(R.id.toolbarSignUp);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("Password");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setButtonViewAndListener() {
        nextButton = findViewById(R.id.signUpButton3);
        nextButton.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        String password1 = Objects.requireNonNull(password.getText()).toString().trim();
        String password2 = Objects.requireNonNull(confirmPassword.getText()).toString().trim();
        int checkDialogType = 0;
        if(password1.isEmpty() || password2.isEmpty()){
            checkDialogType = 1;
            invokeAlertDialog(checkDialogType);
        }

        else if(!password1.equals(password2)){
            checkDialogType = 2;
            invokeAlertDialog(checkDialogType);
        }


        else {
            Intent register = new Intent(SignUp_Screen3.this, DrawerActivity.class);
            register.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            register.putExtra("password", password1);
            startActivity(register);
            // remove previous activity 'LoginActivity' from the backstack
            // remove current activity from backstack or do not save onto the stack
            ActivityCompat.finishAffinity(SignUp_Screen3.this);
        }
    }

    public void invokeAlertDialog(int checkDialogType){
        // Change from AlertDialog to Dialog for more compact features
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp_Screen3.this, R.style.CustomAlertDialog);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(SignUp_Screen3.this);
        View view2 = layoutInflaterAndroid.inflate(R.layout.general_alert_dialog, null);
        TextView textView = (TextView) view2.findViewById(R.id.titlePrompt);

        if(checkDialogType == 1) {
            textView.setText("Fields cannot be empty");
        }
        else if(checkDialogType == 2) {
            textView.setText("Passwords do not match");
        }

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
}