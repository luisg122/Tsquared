package com.example.tsquared.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.tsquared.R;
import com.example.tsquared.Utils.PreferenceUtils;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;

public class LoginEmailActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button login;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputLayout   emailLayout;
    private TextInputLayout   passwordLayout;
    private Handler handler;


    private String emailString;
    private String passwordString;
    private String firstName;
    private String lastName;
    private String userEmail;


    RequestParams params;
    AsyncHttpClient client;
    String URL = "http://207.237.59.117:8080/TSquared/platform?todo=findUser&email=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.backgroundColorForSignIn);
        setContentView(R.layout.signin_with_email);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setViews();
        initializeHandler();
        setListeners();
        setUpToolBar();
    }

    private void setViews() {
        toolbar  = (Toolbar) findViewById(R.id.toolbarEmail);
        email    = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        login    = (Button) findViewById(R.id.buttonSignIn);

        emailLayout    = (TextInputLayout) findViewById(R.id.email_TextInputLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_TextInputLayout);
    }

    private void initializeHandler(){
        handler = new Handler(Looper.getMainLooper());
    }

    private void setUpToolBar() {
        toolbar = findViewById(R.id.toolbarEmail);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setTitle("Log in with email");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // Data is passed to DrawerActivity
    // Keeping the user logged in case they press the back button
    public void goToDrawerActivity(HashMap<String, String> userMap) {
        Intent home = new Intent(LoginEmailActivity.this, DrawerActivity.class);
        home.putExtra("map", userMap);
        home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(home);
        finish();
    }

    public boolean checkInput(){
        if(email.getText().toString().trim().isEmpty() &&
                password.getText().toString().trim().isEmpty()){
            emailLayout.setError("Field cannot be empty");
            passwordLayout.setError("Field cannot be empty");
            return false;
        }

        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
            emailLayout.setError("Please enter a valid email address");
            passwordLayout.setErrorEnabled(false);
            return false;
        }

        else if(email.getText().toString().trim().isEmpty()){
            emailLayout.setError("Field cannot be empty");
            passwordLayout.setErrorEnabled(false);
            return false;
        }

        else if(password.getText().toString().trim().isEmpty()){
            passwordLayout.setError("Field cannot be empty");
            emailLayout.setErrorEnabled(false);
            return false;
        }

        else{
            emailLayout.setError(null);
            passwordLayout.setError(null);
            return true;
        }
    }

    private void setListeners() {
        login.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignIn:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //emailString = email.getText().toString().trim();
                        //passwordString = password.getText().toString().trim();
                        //tryToLogin(emailString, passwordString);
                        if(checkInput()){
                            Intent home = new Intent(LoginEmailActivity.this, DrawerActivity.class);
                            home.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(home);
                            // remove previous activity 'LoginActivity' from the backstack
                            // remove current activity from backstack or do not save onto the stack
                            ActivityCompat.finishAffinity(LoginEmailActivity.this);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }
                }, 150);
                break;
        }
    }

    public void tryToLogin(final String email, final String password) {
        /*client = new AsyncHttpClient();
        URL += "" + email;

        client.get(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Log.e("STRING DATA: ", response.toString());

                try {

                    String userPassword = response.getString("Password");

                    if (isMatching(password, userPassword)) {
                        firstName = response.getString("First Name");
                        lastName = response.getString("Last Name");
                        userEmail = response.getString("Email");
                        college = response.getString("College");
                        description = null;
                        if (response.getString("Description") != null) {
                            description = response.getString("Description");
                        }

                        firstName   = capitalizeFirstCharOfEveryWordInString(firstName);
                        lastName    = capitalizeFirstCharOfEveryWordInString(lastName);
                        college     = capitalizeFirstCharOfEveryWordInString(college);

                        PreferenceUtils.saveEmail(email, getApplicationContext());
                        PreferenceUtils.savePassword(password, getApplicationContext());
                        PreferenceUtils.saveFirstName(firstName, getApplicationContext());
                        PreferenceUtils.saveLastName(lastName, getApplicationContext());
                        PreferenceUtils.saveCollege(college, getApplicationContext());

                        Log.e("FIRSTNAME:", firstName + " " + lastName + " " + userEmail + " " + userPassword + " " + college + " " + description);

                        HashMap<String, String> userMap = new HashMap<String, String>();
                        userMap.put("First Name", firstName);
                        userMap.put("Last Name", lastName);
                        userMap.put("College", college);
                        userMap.put("Email", userEmail);

                        goToDrawerActivity(userMap);

                    } else {
                        Snackbar.make(login, "Incorrect Password, Try Again", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("TAG", "EMAIL: " + "dummy");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });*/
    }

    boolean isMatching(String enteredPassword, String userPassword) {
        return enteredPassword.equals(userPassword);
    }

    private String capitalizeFirstCharOfEveryWordInString(String string) {
        char[] ch = string.toCharArray();
        for (int i = 0; i < string.length(); i++) {
            // Find first char of a word
            // Make sure the character does not equal a space
            if (i == 0 && ch[i] != ' ' || ch[i] != ' ' && ch[i - 1] == ' ') {
                // If such character is lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {
                    // simply convert it into upper-case
                    // refer to the ASCII table to understand this line of code
                    ch[i] = (char) (ch[i] - 'a' + 'A');
                }
            } else if (ch[i] >= 'A' && ch[i] <= 'Z') {
                ch[i] = (char) (ch[i] + 'a' - 'A');
            }
        }
        return new String(ch);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_out_up);
    }
}