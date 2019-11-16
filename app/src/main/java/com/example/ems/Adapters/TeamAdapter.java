package com.example.ems.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ems.Fragments.MyAttendance;
import com.example.ems.Model.Teams;
import com.example.ems.R;
import com.example.ems.Utils.Helper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {
    private ArrayList<Teams> searchAdapterList;
    private Context context;
    private String designation;
    private FragmentManager fragmentManager;


    public TeamAdapter(ArrayList<Teams> teamAdapterList, Context context, String designation, FragmentManager fragmentManager) {
        this.searchAdapterList = teamAdapterList;
        this.context = context;
        this.designation = designation;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public TeamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_team, viewGroup, false);
        return new TeamAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TeamAdapter.ViewHolder holder, int i) {
        final Teams list = searchAdapterList.get(i);
        Glide.with(context).
        load(list.getPhotoURI())
        .into(holder.image);
        String name = list.getName();
        if(name.equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()))
            name += " (You)";
        holder.name.setText(name);
        holder.post.setText(list.getPost());

        if(designation.equals("MANAGER") || designation.equals("TEAM LEAD")){
            holder.itemView.setOnClickListener(v->{
                Bundle bundle = new Bundle();
                bundle.putString("UID",list.getUid());
                bundle.putString("NAME",list.getName());
                Helper.swapFragmentsWithBackStackAndBundle(new MyAttendance(),bundle,R.id.container_frame_main,fragmentManager);
            });
        }




    }

    @Override
    public int getItemCount() {
        return searchAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name, post;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.tImage);
            name = itemView.findViewById(R.id.tName);
            post = itemView.findViewById(R.id.tPost);
        }

    }
}
