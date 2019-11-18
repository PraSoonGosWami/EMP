package com.example.ems.Fragments;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.bumptech.glide.Glide;
import com.example.ems.Model.Emp;
import com.example.ems.R;
import com.example.ems.Utils.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements BSImagePicker.OnSingleImageSelectedListener{

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ImageView profileImage, profileBackgroundImageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView empidTextView;
    private TextView designattion;
    private TextView profileTeam;
    private TextView address;
    private FloatingActionButton floatingActionButton;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private String teamName;


    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        //Binding views---------
        profileImage = view.findViewById(R.id.profileImageView);
        floatingActionButton = view.findViewById(R.id.editProfileDetails);
        nameTextView = view.findViewById(R.id.profile_nameView);
        emailTextView = view.findViewById(R.id.profile_emailView);
        phoneTextView = view.findViewById(R.id.profile_phoneView);
        empidTextView = view.findViewById(R.id.profile_empid);
        designattion = view.findViewById(R.id.profile_designation);
        address = view.findViewById(R.id.profile_address);
        profileTeam = view.findViewById(R.id.profile_team);
        profileBackgroundImageView = view.findViewById(R.id.profileBackgroundImageView);

        progressDialog = new ProgressDialog(getContext(), ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        progressDialog.setMessage("Uploading..");

        if (firebaseUser != null) {
            String name = firebaseUser.getDisplayName();
            String email = firebaseUser.getEmail();
            Log.v("Username", name);
            nameTextView.setText(name);
            emailTextView.setText("Email: "+email);
            Glide.with(getContext())
                    .load(firebaseUser.getPhotoUrl())
                    .error(android.R.drawable.stat_notify_error)
                    .centerCrop()
                    .into(profileImage);
            Glide.with(getContext())
                    .load(firebaseUser.getPhotoUrl())
                    .centerCrop()
                    .into(profileBackgroundImageView);

            //getting user details
            getUserDetails();


            floatingActionButton.setOnClickListener(v -> {

                imagePicker();
            });

        }
        else {
            nameTextView.setText("No user name");
        }




        return view;
    }

    private void getUserDetails() {
        databaseReference.child("Emp").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Emp emp = dataSnapshot.getValue(Emp.class);
                            teamName = emp.getTeam();
                            phoneTextView.setText("Phone: "+emp.getPhone());
                            empidTextView.setText("Emp Id: "+emp.getEmpid());
                            profileTeam.setText("Team: "+emp.getTeam());
                            designattion.setText("Designation: "+emp.getDesignation());
                            address.setText("Address: "+emp.getAddress());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //add updates profile pic

    public void profilePic(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();




        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Glide.with(getContext())
                                .load(user.getPhotoUrl())
                                .centerCrop()
                                .into(profileImage);
                        Glide.with(getContext())
                                .load(user.getPhotoUrl())
                                .centerCrop()
                                .into(profileBackgroundImageView);
                        Helper.showSnackbar("Successful",getActivity(),Helper.SHORT);

                        databaseReference.child("Emp").child(firebaseUser.getUid()).child("photoURI").setValue(uri.toString());
                        databaseReference.child("Teams").child(teamName).child(firebaseUser.getUid()).child("photoURI").setValue(uri.toString());
                        progressDialog.dismiss();


                    }
                });
    }


    //opens image picker dialog
    public void imagePicker() {
        BSImagePicker picker = new BSImagePicker.Builder("com.invaderx.firebasetrigger.fileprovider")
                .setMaximumDisplayingImages(24)
                .setSpanCount(3)
                .setGridSpacing(Utils.dp2px(2))
                .setPeekHeight(Utils.dp2px(360))
                .setTag("A request ID")
                .build();
        picker.show(getChildFragmentManager(), "picker");
    }

    //returns file path
    @Override
    public void onSingleImageSelected(Uri uri, String tag) {


        progressDialog.show();
        storageReference.child(firebaseUser.getUid()).child("profilepic")
                .putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageReference.child(firebaseUser.getUid()).child("profilepic").getDownloadUrl().addOnSuccessListener(uri1 -> {
                        if (uri1 != null)
                            profilePic(uri1);
                        else
                            Toast.makeText(getContext(), "Technical Issue\tTry again", Toast.LENGTH_SHORT).show();

                    });

                })
                .addOnFailureListener(exception -> {
                    progressDialog.dismiss();

                })
                .addOnProgressListener(taskSnapshot -> {
                    //calculating progress percentage
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //displaying percentage in progress dialog
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                });
    }

}
