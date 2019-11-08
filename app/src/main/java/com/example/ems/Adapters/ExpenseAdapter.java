package com.example.ems.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ems.Model.Expense;
import com.example.ems.R;

import java.util.ArrayList;


public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private ArrayList<Expense> expenseAdapterList;
    private Context context;


    public ExpenseAdapter(ArrayList<Expense> expenseAdapterList , Context context) {
        this.expenseAdapterList = expenseAdapterList;
        this.context = context;
    }


    @NonNull
    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_expense, viewGroup, false);
        return new ExpenseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExpenseAdapter.ViewHolder holder, int i) {
        final Expense list = expenseAdapterList.get(i);

        holder.date.setText(list.getDate());
        holder.details.setText(list.getDetails());
        holder.amount.setText("₹"+list.getAmount());

        //outflow
        if(list.isType()){
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.yellow));
            holder.type.setText("Outflow");
        }else{
            holder.amount.setTextColor(ContextCompat.getColor(context,R.color.green));
            holder.type.setText("Inflow");
        }


    }

    @Override
    public int getItemCount() {
        return expenseAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView amount, type, date, details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.eMoney);
            type = itemView.findViewById(R.id.eType);
            date = itemView.findViewById(R.id.eDate);
            details = itemView.findViewById(R.id.eDetails);
        }

    }
}

