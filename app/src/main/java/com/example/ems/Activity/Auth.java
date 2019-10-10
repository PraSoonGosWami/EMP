package com.example.ems.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ems.Fragments.SignIn;
import com.example.ems.R;
import com.example.ems.Utils.Helper;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Prasoon Goswami
 * on 07-10-19
 * This is an activity class which holds sign in and register fragments
 */

public class Auth extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        firebaseAuth = FirebaseAuth.getInstance();




    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser()!=null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        else
            Helper.swapFragments(new SignIn(),R.id.container_frame,getSupportFragmentManager());
    }


}
