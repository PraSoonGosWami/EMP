package com.example.ems.Model;

public class Emp {
    private String fName;
    private String lName;
    private String email;
    private String phone;
    private String address;
    private String empid;
    private String designation;
    private boolean isOnline;
    private boolean isProfileApproved;


    public Emp() {
    }

    public Emp(String fName, String lName, String email, String phone, String address, String empid, String designation, boolean isOnline, boolean isProfileApproved) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.empid = empid;
        this.designation = designation;
        this.isOnline = isOnline;
        this.isProfileApproved = isProfileApproved;
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
        return isOnline;
    }

    public boolean isProfileApproved() {
        return isProfileApproved;
    }
}
