package com.example.tsquared.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tsquared.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Screen3 extends AppCompatActivity implements View.OnClickListener{
    private Button nextButton;
    private Toolbar toolbar;
    private AlertDialog alertDialog;
    private Button okResponse;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup3);
        setUpToolBar();
        setUpPassword();
        setButtonViewAndListener();
    }

    private boolean checkInput() {
        if (password.getText().toString().trim().isEmpty()
                && confirmPassword.getText().toString().trim().isEmpty()) {
            passwordLayout.setError("Field cannot be empty");
            confirmPasswordLayout.setError("Field cannot be empty");
            return false;
        }

        else if (password.getText().toString().trim().isEmpty()) {
            passwordLayout.setError("Field cannot be empty");
            confirmPasswordLayout.setErrorEnabled(false);
            return false;
        }

        else if (confirmPassword.getText().toString().trim().isEmpty()) {
            confirmPasswordLayout.setError("Field cannot be empty");
            passwordLayout.setErrorEnabled(false);
            return false;
        }

        else {
            passwordLayout.setError(null);
            confirmPasswordLayout.setError(null);
            return true;
        }
    }

    public boolean isValidPassword(String password){
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public void setUpPassword(){
        password        = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.confirmRegisterPassword);
        passwordLayout  = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
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
        if(checkInput() && passwordMatch() && checkValidPassword()) {
            Intent register = new Intent(SignUp_Screen3.this, InterestsActivity.class);
            register.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            register.putExtra("password", Objects.requireNonNull(password.getText()).toString().trim());
            startActivity(register);
        }

        else if(!passwordMatch() && checkInput()){
            invokeAlertDialog(1);   // passwords do not match
        }

        else if(!checkValidPassword() && checkInput() && passwordMatch()){
            invokeAlertDialog(2);   // password is not valid
        }
    }

    private boolean passwordMatch(){
        if(!password.getText().toString().trim().equals(
                confirmPassword.getText().toString().trim())){
            return false;
        }
        return true;
    }

    private boolean checkValidPassword(){
        boolean returnValue = false;
        if(password.getText().toString().trim().length() < 8
                && !isValidPassword(password.getText().toString().trim())) {
            returnValue = false;
        }
        else if(password.getText().toString().trim().length() >= 8
                && isValidPassword(password.getText().toString().trim())){
           returnValue = true;
        }
        return returnValue;
    }

    public void invokeAlertDialog(int checkDialogType){
        // Change from AlertDialog to Dialog for more compact features
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp_Screen3.this, R.style.CustomAlertDialog);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(SignUp_Screen3.this);
        View view2 = layoutInflaterAndroid.inflate(R.layout.general_alert_dialog, null);
        TextView textView = (TextView) view2.findViewById(R.id.titlePrompt);

        if(checkDialogType == 1) {
            textView.setText("Passwords do not match");
        }
        else if(checkDialogType == 2) {
            String warningPrompt = "Please enter a valid password\n\n" +
                    "Password must:\n" +
                    "(be at least 8 characters long)\n" +
                    "(contain capital and lowercase letters)\n" +
                    "(contain numbers)\n" +
                    "(contain characters e.g., &,*,#)";
            textView.setText(warningPrompt);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        }

        builder.setCustomTitle(view2);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();

        Window window = alertDialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        okResponse = (Button) alertDialog.findViewById(R.id.okResponse);
        assert okResponse != null;
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
    }
}