package com.example.tsquared.Activities;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.example.tsquared.Utils.PreferenceUtils;
import com.example.tsquared.R;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 101;
    private Button login  = null;
    private Button signUp = null;
    private Button FB_Login     = null;
    private Button Google_Login = null;
    private EditText email;
    private EditText password;
    private String emailString;
    private String passwordString;

    private String firstName;
    private String lastName;
    private String userEmail;
    private String college;
    private String description;

    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    RequestParams params;
    AsyncHttpClient client;
    String URL = "http://207.237.59.117:8080/TSquared/platform?todo=findUser&email=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.signInBackground);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setListeners();
        setUpFaceBookLogin();
        setUpGoogleLogin();

    }

    private void updateUI(GoogleSignInAccount account){
        Intent intent = new Intent(this, DrawerActivity.class);
        intent.putExtra("Google Account", account);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void setUpGoogleLogin() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onStart(){
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            Toast.makeText(this, "Already logged in", Toast.LENGTH_SHORT).show();
            updateUI(account);
        }
        else {
            Log.d("TAG", "Not logged in");
        }
    }

    private void setUpFaceBookLogin() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    // required boiler-plate code for FB sign-in
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        if(requestCode == RC_SIGN_IN){
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                updateUI(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void setViews() {
        FB_Login     = (Button) findViewById(R.id.facebookSignIn);
        Google_Login = (Button) findViewById(R.id.googleSignIn);
        email    = (EditText) findViewById(R.id.email);
        login    = (Button) findViewById(R.id.buttonSignIn);
        signUp   = (Button) findViewById(R.id.SignUp);
        password = (EditText) findViewById(R.id.password);

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
        FB_Login.setOnClickListener(this);
        Google_Login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignIn:
                //emailString = email.getText().toString().trim();
                //passwordString = password.getText().toString().trim();
                //tryToLogin(emailString, passwordString);
                Intent home = new Intent(LoginActivity.this, DrawerActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(home);
                finish();

                break;

            case R.id.SignUp:
                Intent register = new Intent(LoginActivity.this, SignUp_Screen1.class);
                register.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(register);
                // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //finish();
                break;

            case R.id.facebookSignIn:
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                break;

            case R.id.googleSignIn:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
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