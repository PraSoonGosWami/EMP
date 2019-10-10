package com.example.ems.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.ems.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Prasoon Goswami
 * on 06-10-19
 * This is the main activity class which holds side navigation and other fragments
 */

public class MainActivity extends AppCompatActivity {

    private Button logout;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.light);



        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,Auth.class));
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this,Auth.class));
            finish();
        }

    }
}
