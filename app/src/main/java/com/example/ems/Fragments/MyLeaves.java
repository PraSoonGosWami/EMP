package com.example.ems.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ems.Activity.MainActivity;
import com.example.ems.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyLeaves extends Fragment {


    public MyLeaves() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_my_leaves, container, false);

        return view;
    }


}
