package com.example.ems.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ems.Adapters.ExpenseAdapter;
import com.example.ems.Model.Expense;
import com.example.ems.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyExpense extends Fragment {

    private TextView inflow,outflow;
    private RecyclerView recyclerView;
    private FloatingActionButton add,remove;
    private ExpenseAdapter expenseAdapter;
    private ArrayList<Expense> expenseList = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private float in;
    private float out;

    public MyExpense() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_expense, container, false);

        inflow = view.findViewById(R.id.inflow);
        outflow = view.findViewById(R.id.outflow);
        recyclerView = view.findViewById(R.id.expense_rView);
        add = view.findViewById(R.id.add_funds);
        remove = view.findViewById(R.id.remove_funds);

        expenseAdapter = new ExpenseAdapter(expenseList,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(expenseAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        databaseReference.child("Expense").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Expense expense = null;
                        if(dataSnapshot.exists()){
                            expenseList.clear();
                            in = 0.0f;
                            out = 0.0f;
                            for(DataSnapshot data: dataSnapshot.getChildren()){
                                expense = data.getValue(Expense.class);
                                if(expense.isType())
                                    out += expense.getAmount();
                                else
                                    in += expense.getAmount();
                                expenseList.add(expense);
                            }
                            Collections.reverse(expenseList);
                            expenseAdapter.notifyDataSetChanged();
                            inflow.setText("Inflow: ₹"+in);
                            outflow.setText("Outflow: ₹"+out);
                            setPieChart(in,out,view);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        add.setOnClickListener(v->{
            addFund();
        });

        remove.setOnClickListener(v->{
            removeFund();
        });




        return view;
    }

    public void addFund(){

        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.prompt_expense_input, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);


        final TextView heading = promptsView.findViewById(R.id.expense_heading);
        heading.setText("Inflow");
        final TextInputEditText amount = promptsView.findViewById(R.id.expense_amount);
        final TextInputEditText details = promptsView.findViewById(R.id.expense_details);
        final AppCompatButton submit = promptsView.findViewById(R.id.expense_submit);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


        submit.setOnClickListener(v->{
            String money = amount.getText().toString();
            String det = details.getText().toString();

            if(money.isEmpty()){
                amount.setError("Enter amount");
                amount.requestFocus();
                return;
            }
            if(det.isEmpty()){
                details.setError("Enter details");
                details.requestFocus();
                return;
            }
            submit.setEnabled(false);

            Long timestamp = System.currentTimeMillis();
            SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
            String format = s.format(new Date(timestamp));

            Expense expense = new Expense(Long.parseLong(money),
                    false,
                    format,
                    det,
                    timestamp);

            databaseReference.child("Expense").child(firebaseUser.getUid()).child(String.valueOf(timestamp))
                    .setValue(expense).addOnCompleteListener(task -> {
                if(task.isComplete()) {
                    Toast.makeText(getContext(), "Successfully Added", Toast.LENGTH_SHORT).show();

                }else
                    Toast.makeText(getContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                submit.setEnabled(true);
                alertDialog.dismiss();
            });



        });


    }

    public void removeFund(){

        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.prompt_expense_input, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);


        final TextView heading = promptsView.findViewById(R.id.expense_heading);
        heading.setText("Outflow");
        final TextInputEditText amount = promptsView.findViewById(R.id.expense_amount);
        final TextInputEditText details = promptsView.findViewById(R.id.expense_details);
        final AppCompatButton submit = promptsView.findViewById(R.id.expense_submit);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


        submit.setOnClickListener(v->{
            String money = amount.getText().toString();
            String det = details.getText().toString();

            if(money.isEmpty()){
                amount.setError("Enter amount");
                amount.requestFocus();
                return;
            }
            if(det.isEmpty()){
                details.setError("Enter details");
                details.requestFocus();
                return;
            }

            submit.setEnabled(false);

            Long timestamp = System.currentTimeMillis();
            SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
            String format = s.format(new Date(timestamp));

            Expense expense = new Expense(Long.parseLong(money),
                    true,
                    format,
                    det,
                    timestamp);

            databaseReference.child("Expense").child(firebaseUser.getUid()).child(String.valueOf(timestamp))
                    .setValue(expense).addOnCompleteListener(task -> {
                if(task.isComplete()) {
                    Toast.makeText(getContext(), "Successfully Added", Toast.LENGTH_SHORT).show();

                }else
                    Toast.makeText(getContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                submit.setEnabled(true);
                alertDialog.dismiss();
            });



        });

    }

    public void setPieChart(float in, float out, View view){
        PieChart pieChart;
        pieChart = view.findViewById(R.id.salary_chart);

        //setting up pie chart
        pieChart.setUsePercentValues(true);
        pieChart.setDescription(null);
        pieChart.animateXY(1200,1200);
        pieChart.setEntryLabelColor(R.color.black);
        pieChart.setEntryLabelTextSize(10f);
        pieChart.setCenterText("Balance\n₹"+(in-out));

        List<PieEntry> value = new ArrayList<>();

        value.add(new PieEntry(in,"Inflow"));
        value.add(new PieEntry(out,"Outflow"));

        PieDataSet pieDataSet = new PieDataSet(value,null);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

    }



}
