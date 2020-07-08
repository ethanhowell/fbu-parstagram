package com.ethanjhowell.parstagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ethanjhowell.parstagram.LoginActivity;
import com.ethanjhowell.parstagram.R;
import com.parse.ParseUser;


public class ProfileFragment extends Fragment {

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
            getActivity().finish();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}