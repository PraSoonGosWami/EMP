package com.example.ems.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ems.Fragments.DashboardEmployee;
import com.example.ems.R;
import com.example.ems.Utils.Helper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private TextView bottomText;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0f);
        getSupportActionBar().setTitle("EMS");
        getSupportActionBar().setSubtitle("Dashboard");

        firebaseAuth = FirebaseAuth.getInstance();
        bottomText = findViewById(R.id.welcome);

        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(firebaseUser!=null) {
            splashScreen();
            checkApprovedStatus();
            //set user online
            databaseReference.child("Emp").child(firebaseUser.getUid()).child("online").setValue(true);
        }

    }

    public void checkApprovedStatus(){
        databaseReference.child("Emp").child(firebaseUser.getUid()).child("profileApproved")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            boolean profileApproved = dataSnapshot.getValue(Boolean.class);
                            if(!profileApproved){
                                firebaseAuth.signOut();
                                Toast.makeText(MainActivity.this, "Your profile has not yet been approved by the admin", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this,Auth.class));
                                finish();

                            }

                            getDesignation();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getDesignation(){

        databaseReference.child("Emp").child(firebaseUser.getUid()).child("designation")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String designation = dataSnapshot.getValue(String.class);

                            if(designation.equals("MANAGER")) {
                                //set manager
                            }
                            else {
                                // set emp
                            }
                            bottomText.setText("Welcome! "+firebaseUser.getDisplayName());
                            Helper.swapFragments(new DashboardEmployee(),R.id.container_frame_main,getSupportFragmentManager());


                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(this,Auth.class));
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //set user offline
        if(firebaseUser!=null)
            databaseReference.child("Emp").child(firebaseUser.getUid()).child("online").setValue(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                showLogoutDialog();
                return true;
            case R.id.qrmenu:
                //show qr code
                Toast.makeText(this, "QR CODE", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showLogoutDialog(){
        new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_DARK)
                .setTitle("End Session")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    databaseReference.child("Emp").child(firebaseUser.getUid()).child("online").setValue(false);
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,Auth.class));
                    finish();
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_logout)
                .setCancelable(true)
                .show();


    }

    public void splashScreen(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View v = factory.inflate(R.layout.splash_screen, null);
        dialog = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.splash_screen);
        dialog.setCancelable(true);
        dialog.show();
    }
}
