package com.example.ems.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ems.Model.Attendance;
import com.example.ems.Model.Teams;
import com.example.ems.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private ArrayList<Attendance> attendanceAdapterList;
    private Context context;


    public AttendanceAdapter(ArrayList<Attendance> attendanceAdapterList, Context context) {
        this.attendanceAdapterList = attendanceAdapterList;
        this.context = context;
    }


    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_attendance, viewGroup, false);
        return new AttendanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceAdapter.ViewHolder holder, int i) {
        final Attendance list = attendanceAdapterList.get(i);

        holder.date.setText(list.getDate());
        holder.in.setText("In: "+list.getIn());
        holder.out.setText("Out: "+list.getOut());
        if(list.isMarked()){
            holder.mark.setText("Present");
            holder.mark.setTextColor(ContextCompat.getColor(context,R.color.green));
        }else {
            holder.mark.setText("Absent");
            holder.mark.setTextColor(ContextCompat.getColor(context,R.color.yellow));
        }


    }

    @Override
    public int getItemCount() {
        return attendanceAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date, mark, in, out;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.aDate);
            mark = itemView.findViewById(R.id.aMark);
            in = itemView.findViewById(R.id.aIn);
            out = itemView.findViewById(R.id.aOut);
        }

    }
}
