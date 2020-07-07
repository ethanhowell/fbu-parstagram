package com.ethanjhowell.parstagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();
    private ParseQuery<Post> query;
    private ImageView ivImagePreview;
    private EditText etCaption;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImagePreview = findViewById(R.id.ivImagePreview);
        etCaption = findViewById(R.id.etCaption);

        Button btTakePicture = findViewById(R.id.btTakePicture);
        Button btSubmit = findViewById(R.id.btSubmit);

        btSubmit.setOnClickListener(v -> {
            Post post = new Post(etCaption.getText().toString(), ParseUser.getCurrentUser());
            post.saveInBackground(e -> {
                if (e != null) {
                    Log.e(TAG, "onCreate: Error saving post", e);
                    Toast.makeText(this, "Error saving post", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onCreate: Post save success!");
                    Toast.makeText(this, "Post save success!", Toast.LENGTH_SHORT).show();
                }
            });
        });

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