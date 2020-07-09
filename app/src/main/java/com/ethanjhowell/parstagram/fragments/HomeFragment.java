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
import com.ethanjhowell.parstagram.proxy.PostQuery;
import com.ethanjhowell.parstagram.proxy.PostsDataSourceFactory;

public class HomeFragment extends Fragment {
    private PostAdapter adapter;
    private PostsDataSourceFactory<PostQuery> factory;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new PostAdapter();
        factory = new PostsDataSourceFactory<>();

        RecyclerView rvPosts = view.findViewById(R.id.rvPosts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(view.getContext()));

        SwipeRefreshLayout swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> factory.postLiveData.getValue().invalidate());

        PagedList.Config build = new PagedList.Config.Builder().setPageSize(20).build();
        LiveData<PagedList<Post>> posts = new LivePagedListBuilder<>(factory, build).build();

        posts.observe(this, list -> {
            adapter.submitList(list);
            swipeContainer.setRefreshing(false);
        });
    }
}