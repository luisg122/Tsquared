package com.example.tsquared;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login = null;
    private EditText email;
    private EditText password;
    private TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        setListeners();

    }

    private void setListeners() {
        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    private void setViews() {
        login    = (Button)   findViewById(R.id.buttonSignIn);
        email    = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        signUp   = (TextView) findViewById(R.id.SignUp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignIn:
                Intent home = new Intent(LoginActivity.this, DrawerActivity.class);
                startActivity(home);
                finish();
                break;

            case R.id.SignUp:
                Intent register = new Intent(LoginActivity.this, UserRegister.class);
                startActivity(register);
                break;
        }
    }
}
