package com.ethanjhowell.parstagram;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PositionalDataSource;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class PostsDataSourceFactory extends DataSource.Factory<Integer, Post> {
    private static final String TAG = PostsDataSourceFactory.class.getCanonicalName();
    public MutableLiveData<PostDataSource> postLiveData;

    @NonNull
    @Override
    public DataSource<Integer, Post> create() {
        PostDataSource postDataSource = new PostDataSource();
        postLiveData = new MutableLiveData<>();
        postLiveData.postValue(postDataSource);
        return postDataSource;
    }

    public static class PostDataSource extends PositionalDataSource<Post> {
        private ParseQuery<Post> generateBasicQuery(int limit, int startPos) {
            ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
            query.include(Post.KEY_USER);
            // order posts by creation date (newest first)
            query.addDescendingOrder(Post.KEY_CREATED_KEY);
            query.setLimit(limit);
            query.setSkip(startPos);
            return query;
        }

        @Override
        public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Post> callback) {
            ParseQuery<Post> query = generateBasicQuery(params.requestedLoadSize, params.requestedStartPosition);
            try {
                int count = query.count();
                List<Post> posts = query.find();
                callback.onResult(posts, params.requestedStartPosition, count);
            } catch (ParseException e) {
                Log.e(TAG, "queryPosts: problem getting posts", e);
            }
        }

        @Override
        public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Post> callback) {
            ParseQuery<Post> query = generateBasicQuery(params.loadSize, params.startPosition);
            try {
                callback.onResult(query.find());
            } catch (ParseException e) {
                Log.e(TAG, "queryPosts: problem getting posts", e);
            }
        }

    }
}
