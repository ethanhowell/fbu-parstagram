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

import com.ethanjhowell.parstagram.Post;
import com.ethanjhowell.parstagram.PostAdapter;
import com.ethanjhowell.parstagram.PostsDataSourceFactory;
import com.ethanjhowell.parstagram.R;

public class HomeFragment extends Fragment {
    private PostAdapter adapter;

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
        downloadPosts();
        RecyclerView rvPosts = view.findViewById(R.id.rvPosts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void downloadPosts() {
        PagedList.Config build = new PagedList.Config.Builder().setPageSize(20).build();
        PostsDataSourceFactory factory = new PostsDataSourceFactory();
        LiveData<PagedList<Post>> posts = new LivePagedListBuilder<>(factory, build).build();

        posts.observe(this, list -> adapter.submitList(list));
    }
}