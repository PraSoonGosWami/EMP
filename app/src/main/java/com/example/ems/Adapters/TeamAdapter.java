package com.example.ems.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ems.Model.Teams;
import com.example.ems.R;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {
    private ArrayList<Teams> searchAdapterList;
    private Context context;


    public TeamAdapter(ArrayList<Teams> teamAdapterList, Context context) {
        this.searchAdapterList = teamAdapterList;
        this.context = context;
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
        holder.name.setText(list.getName());
        holder.post.setText(list.getPost());


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
