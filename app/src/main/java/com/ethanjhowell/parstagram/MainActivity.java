package com.ethanjhowell.parstagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();


    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(this::bottomNavigationItemSelect);
    }


    private boolean bottomNavigationItemSelect(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_home:
                break;
            case R.id.action_create:
                break;
            case R.id.action_profile:
                break;
        }
        return true;
    }


}