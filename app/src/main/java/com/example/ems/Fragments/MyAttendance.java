package com.example.ems.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ems.Adapters.AttendanceAdapter;
import com.example.ems.Model.Attendance;
import com.example.ems.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAttendance extends Fragment {


    private TextView present,absent;
    private RecyclerView recyclerView;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private int in,out;
    private AttendanceAdapter attendanceAdapter;
    private ArrayList<Attendance> attendanceArrayList = new ArrayList<>();

    public MyAttendance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_attendance, container, false);

        Bundle bundle = getArguments();


        recyclerView = view.findViewById(R.id.attendance_rView);
        present = view.findViewById(R.id.present);
        absent = view.findViewById(R.id.absent);

        attendanceAdapter = new AttendanceAdapter(attendanceArrayList,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(attendanceAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.keepSynced(true);

        String uid = null;
        String name = null;

        if(bundle!=null){
            uid = bundle.getString("UID");
            name = bundle.getString("NAME");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("Attendance: "+name);
        }
        if(uid==null)
            uid= firebaseUser.getUid();

        getAttendance(view,uid);


        return view;
    }

    public void getAttendance(View view,String uid){
        databaseReference.child("Attendance").child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Attendance attendance = null;
                        if(dataSnapshot.exists()){
                            attendanceArrayList.clear();
                            in = 0;
                            out = 0;
                            for(DataSnapshot data : dataSnapshot.getChildren()){
                                attendance = data.getValue(Attendance.class);
                                if(attendance.isMarked())
                                    in++;
                                else
                                    out++;
                                attendanceArrayList.add(attendance);

                            }

                            Collections.sort(attendanceArrayList,Collections.reverseOrder());
                            absent.setText("Absent: "+out+" Day(s)");
                            present.setText("Present: "+in+" Day(s)");
                            attendanceAdapter.notifyDataSetChanged();
                            setPieChart(in,out,view);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void setPieChart(int in, int out, View view){

        PieChart pieChart;
        pieChart = view.findViewById(R.id.attendance_chart);

        //setting up pie chart
        pieChart.setUsePercentValues(true);
        pieChart.setDescription(null);
        pieChart.animateXY(1200,1200);
        pieChart.setEntryLabelColor(R.color.black);
        pieChart.setEntryLabelTextSize(10f);
        pieChart.setCenterText("Attendance");

        List<PieEntry> value = new ArrayList<>();

        value.add(new PieEntry((float)in,"Present"));
        value.add(new PieEntry((float)out,"Absent"));

        PieDataSet pieDataSet = new PieDataSet(value,null);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

    }


}
