package com.example.ems.Fragments;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.ems.Model.Emp;
import com.example.ems.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tomlonghurst.expandablehinttext.ExpandableHintText;

import static com.example.ems.Utils.Helper.LONG;
import static com.example.ems.Utils.Helper.animateError;
import static com.example.ems.Utils.Helper.showSnackbar;

/**
 * Created by Prasoon Goswami
 * on 08-10-19
 * This is fragment class which performs registration function
 */

public class Register extends Fragment {

    private ExpandableHintText fname_register,lname_register,email_register,emp_register,psd_register,phone_register,address_register;
    private Button register;
    private ProgressBar progressBarReg;
    private RelativeLayout background ;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    public Register() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        fname_register = view.findViewById(R.id.fname_register);
        lname_register = view.findViewById(R.id.lname_register);
        email_register = view.findViewById(R.id.email_register);
        emp_register = view.findViewById(R.id.emp_register);
        psd_register = view.findViewById(R.id.psd_register);
        phone_register = view.findViewById(R.id.phone_register);
        register = view.findViewById(R.id.register);
        progressBarReg = view.findViewById(R.id.progressBarReg);
        address_register = view.findViewById(R.id.address_register);
        background = view.findViewById(R.id.dim_back2);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();


        progressBarReg.setVisibility(View.GONE);
        background.setAlpha(1f);
        background.setBackgroundResource(android.R.color.transparent);


        YoYo.with(Techniques.Landing)
                .duration(1200)
                .playOn(view.findViewById(R.id.reg));

        register.setOnClickListener(v->{
            register();

        });




        return  view;
    }

    public void register(){

        String fName = fname_register.getText().trim();
        String lName = lname_register.getText().trim();
        String email = email_register.getText().trim();
        String eid = emp_register.getText().trim();
        String psd = psd_register.getText().trim();
        String phone = phone_register.getText().trim();
        String address = address_register.getText().trim();


        if (fName.isEmpty()) {
            fname_register.setHintText("First name is required");
            fname_register.requestFocus();
            animateError(fname_register,700);
            return;
        }

        if (lName.isEmpty()) {
            lname_register.setHintText("Last name is required");
            lname_register.requestFocus();
            animateError(lname_register,700);
            return;
        }

        if (eid.isEmpty()) {
            emp_register.setHintText("Enter your Emp Id");
            emp_register.requestFocus();
            animateError(emp_register,700);
            return;
        }

        if (phone.isEmpty()) {
            phone_register.setHintText("Enter phone no.");
            phone_register.requestFocus();
            animateError(phone_register,700);
            return;
        }

        if (phone.length()!=10) {
            phone_register.setHintText("Invalid phone no");
            phone_register.requestFocus();
            animateError(phone_register,700);
            return;
        }

        if (email.isEmpty()) {
            email_register.setHintText("Email is required");
            email_register.requestFocus();
            animateError(email_register,700);
            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_register.setHintText("Please enter a valid email");
            email_register.requestFocus();
            animateError(email_register,700);
            return;
        }

        if (psd.isEmpty()) {
            psd_register.setHintText("Password is required");
            psd_register.requestFocus();
            animateError(psd_register,700);
            return;
        }

        if (psd.length() <6) {
            psd_register.setHintText("Password must contain at least 6 characters");
            psd_register.requestFocus();
            animateError(psd_register,700);
            return;
        }

        if (address.isEmpty()) {
            address_register.setHintText("Address is required");
            address_register.requestFocus();
            animateError(address_register,700);
            return;
        }

        if (address.length()<10) {
            address_register.setHintText("Please enter full address");
            address_register.requestFocus();
            animateError(address_register,700);
            return;
        }

        background.setAlpha(0.3f);
        background.setBackgroundColor(Color.parseColor("#99000000"));


        mAuth.createUserWithEmailAndPassword(email,psd).addOnCompleteListener(task -> {

            if(task.isComplete()){
                //setting display name
                new UserProfileChangeRequest.Builder().setDisplayName(fName+" "+lName).build();
                //creating user database
                databaseReference.child("Emp").child(mAuth.getUid())
                        .setValue(new Emp(fName,lName,email,phone,address,eid,"N/A",false,false))
                        .addOnCompleteListener(task1 -> {
                            if(task1.isComplete()) {
                                showSnackbar("Registered successfully\nWait for admin's approval", getActivity(), LONG);
                                mAuth.signOut();
                                getFragmentManager().popBackStackImmediate();

                            }
                            else{
                                showSnackbar("Something went wrong\nPlease try again",getActivity(),LONG);
                                mAuth.getCurrentUser().delete();

                            }

                            background.setAlpha(1f);
                            background.setBackgroundResource(android.R.color.transparent);

                        });
            }
            else{
                showSnackbar(task.getException().getMessage(),getActivity(),LONG);
            }


        });




    }

    public void addUserDetails(FirebaseUser user){

    }

}
