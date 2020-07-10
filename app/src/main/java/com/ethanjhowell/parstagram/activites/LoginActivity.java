package com.ethanjhowell.parstagram.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ethanjhowell.parstagram.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getCanonicalName();
    private EditText etUsername;
    private EditText etPassword;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            Log.d(TAG, "onCreate: User already logged in");
            startActivity(MainActivity.createIntent(this));
            finish();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btLogin = findViewById(R.id.btLogin);
        TextView tvSignUp = findViewById(R.id.tvSignUp);

        btLogin.setOnClickListener(v -> loginUser(etUsername.getText().toString(), etPassword.getText().toString()));
        tvSignUp.setOnClickListener(v -> registerUser(etUsername.getText().toString(), etPassword.getText().toString()));

        callbackManager = CallbackManager.Factory.create();
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: ");
                for (String p : loginResult.getRecentlyGrantedPermissions()) {
                    Log.d(TAG, "onSuccess: " + p);
                }
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: ");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void registerUser(String username, String password) {
        Log.d(TAG, "registerUser: Preparing to register " + username);
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "registerUser: registration issue", e);
                Toast.makeText(LoginActivity.this, "Unable to create account", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "registerUser: success for user " + username);
                startActivity(MainActivity.createIntent(this));
                finish();
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.d(TAG, "loginUser: Preparing to log in " + username);
        ParseUser.logInInBackground(username, password, (ParseUser user, ParseException e) -> {
            if (e != null) {
                Log.e(TAG, "loginUser: login issue", e);
                Toast.makeText(LoginActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "loginUser: success for user " + username);
                startActivity(MainActivity.createIntent(this));
                finish();
            }
        });
    }
}