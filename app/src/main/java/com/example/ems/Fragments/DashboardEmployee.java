package com.example.ems.Fragments;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ems.R;
import com.example.ems.Utils.Helper;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardEmployee extends Fragment implements View.OnClickListener {

    private CardView profile,leaves,attendance,discussion,expense,team;

    public DashboardEmployee() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_employee, container, false);
        profile = view.findViewById(R.id.profile);
        leaves = view.findViewById(R.id.leaves);
        attendance = view.findViewById(R.id.attendance);
        discussion = view.findViewById(R.id.discussion);
        expense = view.findViewById(R.id.expense);
        team = view.findViewById(R.id.team);

        profile.setOnClickListener(this);
        leaves.setOnClickListener(this);
        attendance.setOnClickListener(this);
        discussion.setOnClickListener(this);
        expense.setOnClickListener(this);
        team.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.profile:
                Helper.swapFragmentsWithBackStack(new Profile(),R.id.container_frame_main,getFragmentManager());
                getActivity().setTitle("Profile");
        }
    }
}
