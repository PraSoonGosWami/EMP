package com.example.ems.Model;

public class Attendance implements Comparable<Attendance>{
    private String date;
    private Long timestamp;
    private String in;
    private String out;
    private boolean marked;


    public Attendance() {
    }

    public Attendance(String date, Long timestamp, String in, String out, boolean marked) {
        this.date = date;
        this.timestamp = timestamp;
        this.in = in;
        this.out = out;
        this.marked = marked;
    }

    public String getDate() {
        return date;
    }

    public Long getTimestamp() {
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

    @Override
    public int compareTo(Attendance attendance) {
        return this.getTimestamp().compareTo(attendance.getTimestamp()) ;
    }
}
