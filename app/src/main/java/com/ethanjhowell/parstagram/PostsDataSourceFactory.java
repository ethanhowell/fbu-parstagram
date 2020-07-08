package com.ethanjhowell.parstagram;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;

import com.parse.ParseQuery;

public class PostsDataSourceFactory extends DataSource.Factory<String, Post> {
    private static final String TAG = PostsDataSourceFactory.class.getCanonicalName();
    private MutableLiveData<PostDataSource> postLiveData;

    @NonNull
    @Override
    public DataSource<String, Post> create() {
        PostDataSource postDataSource = new PostDataSource();
        postLiveData = new MutableLiveData<>();
        postLiveData.postValue(postDataSource);
        return postDataSource;
    }

    private static class PostDataSource extends ItemKeyedDataSource<String, Post> {
        private ParseQuery<Post> query;

        public PostDataSource() {
            query = ParseQuery.getQuery(Post.class);
            query.include(Post.KEY_USER);
            // order posts by creation date (newest first)
            query.addDescendingOrder(Post.KEY_CREATED_KEY);
        }

        @Override
        public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Post> callback) {
            query.setLimit(params.requestedLoadSize);
            query.findInBackground((posts, e) -> {
                if (e != null) {
                    Log.e(TAG, "queryPosts: problem getting posts", e);
                } else {
                    callback.onResult(posts);
                }
            });
        }

        @Override
        public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Post> callback) {
        }

        @Override
        public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<Post> callback) {

        }

        @NonNull
        @Override
        public String getKey(@NonNull Post item) {
            return item.getObjectId();
        }
    }
}
