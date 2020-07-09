package com.ethanjhowell.parstagram.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ethanjhowell.parstagram.R;
import com.ethanjhowell.parstagram.adapters.PostAdapter;
import com.ethanjhowell.parstagram.models.Post;
import com.ethanjhowell.parstagram.proxy.PostQueryFactory;
import com.ethanjhowell.parstagram.proxy.PostsDataSourceFactory;

import java.util.Objects;

public abstract class FeedFragment extends Fragment {
    protected RecyclerView rvPosts;
    protected SwipeRefreshLayout swipeContainer;
    protected PostAdapter adapter = new PostAdapter();
    protected PostsDataSourceFactory factory = new PostsDataSourceFactory(new PostQueryFactory());

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    protected void loadFeed(View view) {
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(view.getContext()));

        swipeContainer.setOnRefreshListener(() -> Objects.requireNonNull(factory.postLiveData.getValue()).invalidate());

        PagedList.Config build = new PagedList.Config.Builder().setPageSize(20).build();
        LiveData<PagedList<Post>> posts = new LivePagedListBuilder<>(factory, build).build();

        posts.observe(this, list -> {
            adapter.submitList(list);
            swipeContainer.setRefreshing(false);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        swipeContainer = view.findViewById(R.id.swipeContainer);
    }
}