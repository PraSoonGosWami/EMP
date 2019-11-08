package com.example.ems.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ems.Model.Message;
import com.example.ems.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.ViewHolder> {
    private ArrayList<Message> messageAdapterList;
    private Context context;


    public DiscussionAdapter(ArrayList<Message> messageAdapterList, Context context) {
        this.messageAdapterList = messageAdapterList;
        this.context = context;
    }


    @NonNull
    @Override
    public DiscussionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_text_msg, viewGroup, false);
        return new DiscussionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DiscussionAdapter.ViewHolder holder, int i) {
        final Message list = messageAdapterList.get(i);

        Glide.with(context).
                load(list.getUri())
                .into(holder.image);
        if(list.getName().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()))
            holder.name.setText(list.getName()+" (You)");
        else
            holder.name.setText(list.getName());
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String format = s.format(new Date(list.getTimestamp()));
        holder.date.setText(format);
        holder.text.setText(list.getMsg());



    }

    @Override
    public int getItemCount() {
        return messageAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name, date, text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.chat_img);
            name = itemView.findViewById(R.id.chat_name);
            text = itemView.findViewById(R.id.chat_text);
            date = itemView.findViewById(R.id.chat_time);
        }

    }
}
