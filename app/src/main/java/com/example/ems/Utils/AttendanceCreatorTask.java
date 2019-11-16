package com.example.ems.Utils;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ems.Model.Attendance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AttendanceCreatorTask extends Worker {

    public AttendanceCreatorTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Long timestamp = System.currentTimeMillis();
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
        String format = s.format(new Date(timestamp));
        Attendance attendance = new Attendance(format,timestamp,"--:--","--:--",false);


        databaseReference.child("Attendance").child(user.getUid()).child(String.valueOf(timestamp))
                .setValue(attendance);
        return Result.success();
    }
}
