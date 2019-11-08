package com.example.ems.Model;

public class Expense {

    private float amount;
    private boolean type; //true - outflow || false - inflow
    private String date;
    private String details;
    private long timestamp;

    public Expense() {
    }

    public Expense(float amount, boolean type, String date, String details, long timestamp) {
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.details = details;
        this.timestamp = timestamp;
    }

    public float getAmount() {
        return amount;
    }

    public float getTimestamp() {
        return timestamp;
    }

    public boolean isType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }
}
