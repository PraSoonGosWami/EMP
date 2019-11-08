package com.example.ems.Model;

public class Teams {
    private String name;
    private String post;
    private String photoURI;

    public Teams() {
    }

    public Teams(String name, String post, String photoURI) {
        this.name = name;
        this.post = post;
        this.photoURI = photoURI;
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
}

