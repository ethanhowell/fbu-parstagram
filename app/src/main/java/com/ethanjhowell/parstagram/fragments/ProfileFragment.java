package com.ethanjhowell.parstagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ethanjhowell.parstagram.R;
import com.ethanjhowell.parstagram.activites.LoginActivity;
import com.ethanjhowell.parstagram.models.Post;
import com.ethanjhowell.parstagram.proxy.PostUserRestrictedQuery;
import com.parse.ParseUser;

import java.util.Objects;


public class ProfileFragment extends FeedFragment<PostUserRestrictedQuery> {

    // Required empty public constructor
    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        view.findViewById(R.id.btLogout).setOnClickListener(v -> {
            ParseUser.logOut();
            startActivity(new Intent(view.getContext(), LoginActivity.class));
            Objects.requireNonNull(getActivity()).finish();
        });
        return view;
    }


    protected void loadFeed(View view) {
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new GridLayoutManager(view.getContext(), 3));

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
        loadFeed(view);
    }
}