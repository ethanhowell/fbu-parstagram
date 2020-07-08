package com.ethanjhowell.parstagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ethanjhowell.parstagram.fragments.CreateFragment;
import com.ethanjhowell.parstagram.fragments.HomeFragment;
import com.ethanjhowell.parstagram.fragments.ProfileFragment;
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

        final FragmentManager fragmentManager = getSupportFragmentManager();
        CreateFragment createFragment = new CreateFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        HomeFragment homeFragment = new HomeFragment();

        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.action_create:
                    fragment = createFragment;
                    break;
                case R.id.action_profile:
                    fragment = profileFragment;
                    break;
                case R.id.action_home:
                default:
                    fragment = homeFragment;
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.clFragment, fragment).commit();
            return true;
        });
        bottom_navigation.setSelectedItemId(R.id.action_home);
    }



}