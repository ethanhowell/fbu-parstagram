package com.ethanjhowell.parstagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseQuery;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();
    ParseQuery<Post> query;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        queryPosts();
    }

    private void queryPosts() {
        query.findInBackground((posts, e) -> {
            if (e != null) {
                Log.e(TAG, "queryPosts: problem getting posts", e);
                Toast.makeText(this, "Something went wrong fetching posts", Toast.LENGTH_SHORT).show();
            } else {
                for (Post p : posts) {
                    Log.i(TAG, String.format("queryPosts: Username: %s, Caption: %s", p.getUser().getUsername(), p.getCaption()));
                }
            }
        });
    }
}