package com.ethanjhowell.parstagram.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.ethanjhowell.parstagram.Post;
import com.ethanjhowell.parstagram.R;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class CreateFragment extends Fragment {
    private static final String TAG = CreateFragment.class.getCanonicalName();
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private static final String PHOTO_FILE_NAME = "photo.jpg";
    private ParseQuery<Post> query;
    private ImageView ivImagePreview;
    private EditText etCaption;
    private File photoFile;
    private Context context;


    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        ivImagePreview = view.findViewById(R.id.ivImagePreview);
        etCaption = view.findViewById(R.id.etCaption);

        Button btTakePicture = view.findViewById(R.id.btTakePicture);
        Button btSubmit = view.findViewById(R.id.btSubmit);

        btSubmit.setOnClickListener(this::savePost);
        btTakePicture.setOnClickListener(this::launchCamera);

        query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        queryPosts();
    }

    private void savePost(View v) {
        Post post = new Post(etCaption.getText().toString(), new ParseFile(photoFile), ParseUser.getCurrentUser());
        post.saveInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "savePost: Error saving post", e);
                Toast.makeText(context, "Error saving post", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "savePost: Post save success!");
                Toast.makeText(context, "Post save success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchCamera(View v) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri();

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(context, "com.ethanjhowell.parstagram.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap rawTakenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // Load the taken image into a preview
                ivImagePreview.setImageBitmap(rawTakenImage);
            } else { // Result was a failure
                Toast.makeText(context, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private File getPhotoFileUri() {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "getPhotoFileUri: failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + PHOTO_FILE_NAME);
    }

    private void queryPosts() {
        query.findInBackground((posts, e) -> {
            if (e != null) {
                Log.e(TAG, "queryPosts: problem getting posts", e);
                Toast.makeText(context, "Something went wrong fetching posts", Toast.LENGTH_SHORT).show();
            } else {
                for (Post p : posts) {
                    Log.i(TAG, String.format("queryPosts: Username: %s, Caption: %s", p.getUser().getUsername(), p.getCaption()));
                }
            }
        });
    }
}