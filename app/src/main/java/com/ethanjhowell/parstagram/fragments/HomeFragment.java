package com.ethanjhowell.parstagram.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ethanjhowell.parstagram.proxy.PostQuery;

public class HomeFragment extends FeedFragment<PostQuery> {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFeed(view);
    }
}
