package com.example.ems.Model;

public class Teams {
    private String name;
    private String post;
    private String photoURI;
    private String uid;

    public Teams() {
    }

    public Teams(String name, String post, String photoURI, String uid) {
        this.name = name;
        this.post = post;
        this.photoURI = photoURI;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getPost() {
        return post;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public String getUid() {
        return uid;
    }
}

