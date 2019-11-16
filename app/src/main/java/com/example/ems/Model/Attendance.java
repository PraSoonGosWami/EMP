package com.example.ems.Model;

public class Attendance {
    private String date;
    private long timestamp;
    private String in;
    private String out;
    private boolean marked;


    public Attendance() {
    }

    public Attendance(String date, long timestamp, String in, String out, boolean marked) {
        this.date = date;
        this.timestamp = timestamp;
        this.in = in;
        this.out = out;
        this.marked = marked;
    }

    public String getDate() {
        return date;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getIn() {
        return in;
    }

    public String getOut() {
        return out;
    }

    public boolean isMarked() {
        return marked;
    }
}
