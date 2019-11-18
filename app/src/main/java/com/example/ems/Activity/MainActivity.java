package com.example.ems.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ems.Fragments.DashboardEmployee;
import com.example.ems.R;
import com.example.ems.Utils.AttendanceCreatorTask;
import com.example.ems.Utils.Helper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import net.glxn.qrgen.android.QRCode;

import java.util.concurrent.TimeUnit;


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
            //databaseReference.child("Emp").child(firebaseUser.getUid()).child("online").setValue(true);
            performBackgroundTask();

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

                            bottomText.setText("Welcome! "+firebaseUser.getDisplayName());
                            Helper.swapFragments(new DashboardEmployee(),R.id.container_frame_main,getSupportFragmentManager());
                            dialog.dismiss();

                        }
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
       /* if(firebaseUser!=null)
            databaseReference.child("Emp").child(firebaseUser.getUid()).child("online").setValue(false);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                showLogoutDialog();
                return true;
            case R.id.qrcode:
                //show qr code
                qrPrompt();
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

    public void performBackgroundTask(){

        PeriodicWorkRequest.Builder myWorkBuilder =
                new PeriodicWorkRequest.Builder(AttendanceCreatorTask.class, 12, TimeUnit.HOURS);

        PeriodicWorkRequest myWork = myWorkBuilder.build();
        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork("jobTag", ExistingPeriodicWorkPolicy.REPLACE, myWork);

    }



    public void qrPrompt() {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.prompt_qrcode, null);
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);



        final AppCompatButton checkin = promptsView.findViewById(R.id.checkin);
        final AppCompatButton checkout = promptsView.findViewById(R.id.checkout);
        final LinearLayout buttonlayout = promptsView.findViewById(R.id.buttons);
        final LinearLayout qrLayout = promptsView.findViewById(R.id.qrlayout);
        final TextView name = promptsView.findViewById(R.id.uname);
        final TextView empid = promptsView.findViewById(R.id.uempid);
        final AppCompatImageView qrimage = promptsView.findViewById(R.id.qrimage);

        checkin.setEnabled(true);
        checkout.setEnabled(true);
        buttonlayout.setVisibility(View.VISIBLE);
        qrLayout.setVisibility(View.GONE);

        // create alert dialog
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

        checkin.setOnClickListener(v->{
            checkin.setEnabled(false);
            checkout.setEnabled(false);
            generateQr("in",qrimage);
            buttonlayout.setVisibility(View.GONE);
            qrLayout.setVisibility(View.VISIBLE);

        });
        checkout.setOnClickListener(v->{
            checkin.setEnabled(false);
            checkout.setEnabled(false);
            generateQr("out",qrimage);
            buttonlayout.setVisibility(View.GONE);
            qrLayout.setVisibility(View.VISIBLE);
        });


        name.setText(firebaseUser.getDisplayName());

        databaseReference.child("Emp").child(firebaseUser.getUid()).child("empid")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String id = dataSnapshot.getValue(String.class);
                            empid.setText("Emp Id: "+id);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





    }

    private void generateQr(String type, AppCompatImageView imageView){
        Bitmap myBitmap = QRCode.from(firebaseUser.getUid()+"@"+type)
                .withSize(256, 256)
                .withErrorCorrection(ErrorCorrectionLevel.H)
                .bitmap();
        Glide.with(this)
                .load(myBitmap)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);
    }




}
