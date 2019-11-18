package com.example.ems.Model;

public class AddNotes {

    private String title;
    private String body;
    private String date;

    public AddNotes() {
    }

    public AddNotes(String title, String body, String date) {
        this.title = title;
        this.body = body;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }
}
