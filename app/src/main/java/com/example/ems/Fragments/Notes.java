package com.example.ems.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ems.Adapters.NotesAdapter;
import com.example.ems.Model.AddNotes;
import com.example.ems.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notes extends Fragment {

    private FloatingActionButton addNotes;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private TextView noNotes;
    private ArrayList<AddNotes> arrayList = new ArrayList<>();
    private NotesAdapter notesAdapter;

    public Notes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notes, container, false);

        addNotes = view.findViewById(R.id.add_notes);
        recyclerView = view.findViewById(R.id.notes_rView);
        noNotes = view.findViewById(R.id.no_notes);

        noNotes.setVisibility(View.GONE);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        notesAdapter = new NotesAdapter(arrayList,getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(notesAdapter);

        addNotes.setOnClickListener(v->{
            addNewNote();
        });

        getAllNotes();



        return view;
    }

    public void getAllNotes(){
        databaseReference.child("Notes").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AddNotes notes = null;
                        if(dataSnapshot.exists()){
                            arrayList.clear();
                            for(DataSnapshot data : dataSnapshot.getChildren()){
                                notes = data.getValue(AddNotes.class);
                                arrayList.add(notes);
                            }

                            Collections.reverse(arrayList);

                        }
                        if(arrayList.size()==0)
                            noNotes.setVisibility(View.VISIBLE);
                        notesAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void addNewNote(){

        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.prompt_notes_input, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);


        final TextInputEditText title = promptsView.findViewById(R.id.notes_title);
        final TextInputEditText details = promptsView.findViewById(R.id.notes_details);
        final AppCompatButton done = promptsView.findViewById(R.id.notes_done);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


        done.setOnClickListener(v->{
            String titleText = title.getText().toString().trim();
            String det = details.getText().toString().trim();

            if(titleText.isEmpty()){
                title.setError("Enter title");
                title.requestFocus();
                return;
            }
            if(det.isEmpty()){
                details.setError("Enter details");
                details.requestFocus();
                return;
            }
            done.setEnabled(false);

            Long timestamp = System.currentTimeMillis();
            SimpleDateFormat s = new SimpleDateFormat("MMM dd,yyyy");
            String format = s.format(new Date(timestamp));

            AddNotes addNotes = new AddNotes(titleText,det,format);

            databaseReference.child("Notes").child(firebaseUser.getUid()).child(String.valueOf(timestamp))
                    .setValue(addNotes).addOnCompleteListener(task -> {
                if(task.isComplete()) {
                    Toast.makeText(getContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                    noNotes.setVisibility(View.GONE);

                }else
                    Toast.makeText(getContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                done.setEnabled(true);
                alertDialog.dismiss();
            });



        });


    }


}
