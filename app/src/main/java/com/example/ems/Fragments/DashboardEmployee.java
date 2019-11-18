package com.example.ems.Fragments;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

    private CardView profile, notes,attendance,discussion,expense,team;

    public DashboardEmployee() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_employee, container, false);
        profile = view.findViewById(R.id.profile);
        notes = view.findViewById(R.id.notes);
        attendance = view.findViewById(R.id.attendance);
        discussion = view.findViewById(R.id.discussion);
        expense = view.findViewById(R.id.expense);
        team = view.findViewById(R.id.team);


        profile.setOnClickListener(this);
        notes.setOnClickListener(this);
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
                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Profile");
                break;
            case R.id.notes:
                Helper.swapFragmentsWithBackStack(new Notes(),R.id.container_frame_main,getFragmentManager());
                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("My Notes");
                break;
            case R.id.team:
                Helper.swapFragmentsWithBackStack(new MyTeam(),R.id.container_frame_main,getFragmentManager());
                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("My Team");
                break;
            case R.id.expense:
                Helper.swapFragmentsWithBackStack(new MyExpense(),R.id.container_frame_main,getFragmentManager());
                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("My Expenses");
                break;
            case R.id.discussion:
                Helper.swapFragmentsWithBackStack(new Discussion(),R.id.container_frame_main,getFragmentManager());
                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Discussion");
                break;
            case R.id.attendance:
                Helper.swapFragmentsWithBackStack(new MyAttendance(),R.id.container_frame_main,getFragmentManager());
                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("My Attendance");
                break;


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Dashboard");
    }
}
