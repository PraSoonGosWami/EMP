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
import android.widget.Toast;

import com.example.ems.Adapters.TeamAdapter;
import com.example.ems.Model.Teams;
import com.example.ems.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTeam extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Teams> teamList = new ArrayList<>();
    private TeamAdapter teamAdapter;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String designation = "null";



    public MyTeam() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_team, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        //getting designation
        databaseReference.child("Emp").child(firebaseUser.getUid()).child("designation")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            designation = dataSnapshot.getValue(String.class);
                            recyclerView = view.findViewById(R.id.team_rView);
                            teamAdapter = new TeamAdapter(teamList,getContext(),designation,getFragmentManager());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(teamAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




        getTeamList();

        return view;
    }

    public void getTeamList(){
        databaseReference.child("Emp").child(firebaseUser.getUid()).child("team")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String currentUserTeamName = dataSnapshot.getValue(String.class);
                            ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("My Team - "+currentUserTeamName);
                            if(currentUserTeamName != null){
                                databaseReference.child("Teams").child(currentUserTeamName)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Teams teams = null;
                                                if(dataSnapshot.exists()){
                                                    teamList.clear();
                                                    for(DataSnapshot data : dataSnapshot.getChildren()){
                                                        teams = data.getValue(Teams.class);
                                                        teamList.add(teams);
                                                    }

                                                    teamAdapter.notifyDataSetChanged();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }

}
