package com.example.ems.Model;

public class Message {
    private String msg;
    private long timestamp;
    private String uri;
    private String name;


    public Message() {
    }

    public Message(String msg, long timestamp, String uri, String name) {
        this.msg = msg;
        this.timestamp = timestamp;
        this.uri = uri;
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }
}

