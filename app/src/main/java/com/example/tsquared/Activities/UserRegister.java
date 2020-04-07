package com.example.tsquared.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tsquared.R;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.testfairy.TestFairy;

import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserRegister extends AppCompatActivity implements View.OnClickListener{

    private CircleImageView image;
    private EditText emailRegister;
    private EditText passwordRegister;
    private EditText firstNameRegister;
    private EditText lastNameRegister;
    private EditText collegeRegister;
    private EditText descriptionRegister;
    private Button registerButton;
    private TextView loginText;

    private String emailLoginString;
    private String passwordFormString;
    private String firstNameString;
    private String lastNameString;
    private String collegeRegisterString;
    private String descriptionRegisterString;

    RequestParams params;
    AsyncHttpClient client;
    String URL = "http://207.237.59.117:8080/TSquared/platform?todo=register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        setViews();
        setListeners();
        //TestFairy.begin(this, "");
    }

    private void setListeners() {
        registerButton.setOnClickListener(this);
        loginText.setOnClickListener(this);
    }

    private void setViews() {
        emailRegister       = (EditText) findViewById(R.id.registerEmail);
        passwordRegister    = (EditText) findViewById(R.id.registerPassword);
        firstNameRegister   = (EditText) findViewById(R.id.firstName);
        lastNameRegister    = (EditText) findViewById(R.id.lastName);
        collegeRegister     = (EditText) findViewById(R.id.college);
        descriptionRegister = (EditText) findViewById(R.id.description);
        registerButton      = (Button)   findViewById(R.id.reg_button);
        loginText           = (TextView) findViewById(R.id.reg_link);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                image.setEnabled(true);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_button:

                if(!isGood()){
                    Snackbar.make(registerButton, "Sorry, Try Again", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else{
                    takeDataToPost();
                    firstNameString   = capitalizeFirstCharOfEveryWordInString(firstNameString);
                    lastNameString    = capitalizeFirstCharOfEveryWordInString(lastNameString);
                    collegeRegisterString = capitalizeFirstCharOfEveryWordInString(collegeRegisterString);

                    HashMap<String, String> userMap = new HashMap<String, String>();
                    userMap.put("First Name", firstNameString);
                    userMap.put("Last Name", lastNameString);
                    userMap.put("College", collegeRegisterString);
                    userMap.put("Email", emailLoginString);

                    Intent home = new Intent(UserRegister.this, DrawerActivity.class);
                    home.putExtra("map1", userMap);
                    home.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(home);
                    finish();
                }
                break;

            case R.id.reg_link:
                Intent login = new Intent(UserRegister.this, LoginActivity.class);
                login.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(login);
                finish();
                break;
        }
    }

    public boolean isGood() {
        emailLoginString = emailRegister.getText().toString().trim();

        if (!validEmailAddress(emailLoginString)) {
            Toast.makeText(getApplicationContext(), "Register with your CUNY Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        passwordFormString    = passwordRegister.getText().toString().trim();
        firstNameString       = firstNameRegister.getText().toString().trim();
        lastNameString        = lastNameRegister.getText().toString();
        collegeRegisterString = collegeRegister.getText().toString();
        descriptionRegisterString = descriptionRegister.getText().toString();

        if (emailLoginString == null || firstNameString == null || lastNameString == null || passwordFormString == null) {
            if (emailLoginString == null) {
                Toast.makeText(getApplicationContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
            } else if (firstNameString == null) {
                Toast.makeText(getApplicationContext(), "Enter Your First Name", Toast.LENGTH_SHORT).show();
            } else if (lastNameString == null) {
                Toast.makeText(getApplicationContext(), "Enter Your Last Name", Toast.LENGTH_SHORT).show();
            } else if (passwordFormString == null) {
                Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    public boolean validEmailAddress(String email){
        String pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@qmail.cuny.edu";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void takeDataToPost(){

        params = new RequestParams();
        params.put("firstName", firstNameString);
        params.put("lastName", lastNameString);
        params.put("email", emailLoginString);
        params.put("password", passwordFormString);
        params.put("college", collegeRegisterString);
        params.put("description", descriptionRegisterString);
        client = new AsyncHttpClient();
        client.post(URL, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Toast.makeText(getApplicationContext(), "Submit Success " + response, Toast.LENGTH_SHORT).show();
                Log.e("TAG", "EMAIL: " + "dummy");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TAG", "EMAIL: " + "dummy");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("ws", "---->>onFailure" + throwable.toString());
            }
        });
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