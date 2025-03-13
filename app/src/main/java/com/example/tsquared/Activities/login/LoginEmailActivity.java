package com.example.tsquared.Activities.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tsquared.Activities.DrawerActivity;
import com.example.tsquared.R;
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
        setContentView(R.layout.signin_with_email);
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
        if (v.getId() == R.id.buttonSignIn) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //emailString = email.getText().toString().trim();
                    //passwordString = password.getText().toString().trim();
                    //tryToLogin(emailString, passwordString);
                    if (checkInput()) {
                        Intent home = new Intent(LoginEmailActivity.this, DrawerActivity.class);
                        home.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(home);
                        hideKeyboard(LoginEmailActivity.this);

                        // remove previous activity 'LoginActivity' from the backStack
                        // remove current activity from backStack or do not save onto the stack
                        finishAffinity();
                    }
                }
            }, 50);
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

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void finish(){
        super.finish();
    }
}