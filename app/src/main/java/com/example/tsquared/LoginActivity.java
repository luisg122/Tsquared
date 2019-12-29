package com.example.tsquared;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login = null;
    private EditText email;
    private EditText password;
    private TextView signUp;
    private String emailString;
    private String passwordString;

    private String firstName;
    private String lastName;
    private String userEmail;
    private String college;
    private String description;

    RequestParams params;
    AsyncHttpClient client;
    String URL = "http://207.237.59.117:8080/TSquared/platform?todo=findUser&email=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setListeners();
    }

    private void setViews() {
        email = (EditText) findViewById(R.id.email);
        String appendedEmail = "@qmail.cuny.edu";
        email.setText(appendedEmail);

        login = (Button) findViewById(R.id.buttonSignIn);
        password = (EditText) findViewById(R.id.password);
        signUp = (TextView) findViewById(R.id.SignUp);

        if(PreferenceUtils.getEmail(this) != null){
            Intent home = new Intent(LoginActivity.this, DrawerActivity.class);
            home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(home);
            finish();
        }
    }

    // Data is passed to DrawerActivity
    // Keeping the user logged in case they press the back button
    public void goToDrawerActivity(HashMap<String, String> userMap) {
        Intent home = new Intent(LoginActivity.this, DrawerActivity.class);
        home.putExtra("map", userMap);
        home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(home);
        finish();
    }

    private void setListeners() {
        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignIn:
                emailString = email.getText().toString().trim();
                passwordString = password.getText().toString().trim();
                tryToLogin(emailString, passwordString);
                break;

            case R.id.SignUp:
                Intent register = new Intent(LoginActivity.this, UserRegister.class);
                register.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(register);
                finish();
                break;
        }
    }

    public void tryToLogin(final String email, final String password) {

        client = new AsyncHttpClient();
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
        });
    }

    boolean isMatching(String enteredPassword, String userPassword) {
        return enteredPassword.equals(userPassword);
    }

    private String capitalizeFirstCharOfEveryWordInString(String string){
        char[] ch = string.toCharArray();
        for(int i = 0; i < string.length(); i++) {
            // Find first char of a word
            // Make sure the character does not equal a space
            if (i == 0 && ch[i] != ' ' || ch[i] != ' ' && ch[i - 1] == ' ') {
                // If such character is lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {
                    // simply convert it into upper-case
                    // refer to the ASCII table to understand this line of code
                    ch[i] = (char) (ch[i] - 'a' + 'A');
                }
            }
            else if (ch[i] >= 'A' && ch[i] <= 'Z'){
                ch[i] = (char) (ch[i] + 'a' - 'A');
            }
        }
        return new String(ch);
    }
}