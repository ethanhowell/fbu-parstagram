package com.ethanjhowell.parstagram;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getCanonicalName();
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            Log.d(TAG, "onCreate: User already logged in");
            startActivity(MainActivity.getStartIntent(this));
            finish();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btLogin = findViewById(R.id.btLogin);

        btLogin.setOnClickListener(v -> loginUser(etUsername.getText().toString(), etPassword.getText().toString()));

    }

    private void loginUser(String username, String password) {
        Log.d(TAG, "loginUser: Preparing to log in " + username);
        ParseUser.logInInBackground(username, password, (ParseUser user, ParseException e) -> {
            if (e != null) {
                Log.e(TAG, "loginUser: login issue", e);
                Toast.makeText(LoginActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "loginUser: success for user " + username);
                startActivity(MainActivity.getStartIntent(this));
                finish();
            }
        });
    }
}