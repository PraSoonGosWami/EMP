package com.example.ems.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ems.Activity.Auth;
import com.example.ems.Activity.MainActivity;
import com.example.ems.Model.AddNotes;
import com.example.ems.Model.Expense;
import com.example.ems.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class NotesAdapter  extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private ArrayList<AddNotes> notesAdapterList;
    private Context context;


    public NotesAdapter(ArrayList<AddNotes> notesAdapterList , Context context) {
        this.notesAdapterList = notesAdapterList;
        this.context = context;
    }


    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_notes, viewGroup, false);
        return new NotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesAdapter.ViewHolder holder, int i) {
        final AddNotes list = notesAdapterList.get(i);

        holder.date.setText(list.getDate());
        holder.body.setText(list.getBody());
        holder.title.setText(list.getTitle());

    }

    @Override
    public int getItemCount() {
        return notesAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date, title, body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.nDate);
            title = itemView.findViewById(R.id.nTitle);
            body = itemView.findViewById(R.id.nBody);
        }

    }
}
