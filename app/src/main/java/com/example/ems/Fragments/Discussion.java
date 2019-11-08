package com.example.ems.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ems.Adapters.DiscussionAdapter;
import com.example.ems.Model.Emp;
import com.example.ems.Model.Message;
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
public class Discussion extends Fragment {


    private AppCompatEditText msg;
    private ImageView send;
    private RecyclerView recyclerView;
    private DiscussionAdapter discussionAdapter;
    private ArrayList<Message> list = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    public Discussion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discussion, container, false);

        msg = view.findViewById(R.id.msg);
        send = view.findViewById(R.id.send);
        recyclerView = view.findViewById(R.id.discussion_rView);
        discussionAdapter = new DiscussionAdapter(list,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(discussionAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        send.setOnClickListener(v->{
            addMessage();
        });

        getTeam();


        return view;
    }
    public void getTeam(){
        databaseReference.child("Emp").child(firebaseUser.getUid()).child("team")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String team = dataSnapshot.getValue(String.class);
                            if(team!=null)
                                getMessages(team);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getMessages(String team){
        databaseReference.child("Discussion").child(team)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Message message = null;
                        if(dataSnapshot.exists()){
                            list.clear();
                            for(DataSnapshot data : dataSnapshot.getChildren()){
                                message = data.getValue(Message.class);
                                list.add(message);
;                            }

                            discussionAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(list.size()-1);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void addMessage(){
        databaseReference.child("Emp").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Emp emp = dataSnapshot.getValue(Emp.class);
                            if(emp.getTeam()!=null){
                                String text = msg.getText().toString().trim();
                                if(text.isEmpty()){
                                    msg.setError("Message is blank");
                                    msg.requestFocus();
                                    return;
                                }
                                send.setEnabled(false);
                                long timestamp = System.currentTimeMillis();
                                Message message = new Message(text,timestamp,emp.getPhotoURI(),firebaseUser.getDisplayName());
                                databaseReference.child("Discussion").child(emp.getTeam()).child(String.valueOf(timestamp))
                                        .setValue(message).addOnCompleteListener(task -> {
                                            if(!task.isComplete())
                                                Toast.makeText(getContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            else
                                                msg.getText().clear();
                                            send.setEnabled(true);
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
