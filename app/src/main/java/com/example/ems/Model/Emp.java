package com.example.ems.Model;

public class Emp {
    private String fName;
    private String lName;
    private String email;
    private String phone;
    private String address;
    private String empid;
    private String designation;
    private boolean online;
    private boolean profileApproved;
    private String team;
    private String photoURI;
    private String uid;


    public Emp() {
    }

    public Emp(String fName, String lName, String email, String phone, String address, String empid, String designation, boolean online, boolean profileApproved, String team, String photoURI, String uid) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.empid = empid;
        this.designation = designation;
        this.online = online;
        this.profileApproved = profileApproved;
        this.team = team;
        this.photoURI = photoURI;
        this.uid = uid;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getEmpid() {
        return empid;
    }

    public String getDesignation() {
        return designation;
    }

    public boolean isOnline() {
        return online;
    }

    public boolean isProfileApproved() {
        return profileApproved;
    }

    public String getTeam(){
        return team;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public String getUid() {
        return uid;
    }
}
