package com.example.ems.Model;

public class Leaves {
    int sickLeave; //11
    int earnedLeave; //12
    int casualLeave; //11

    public Leaves() {
    }

    public Leaves(int sickLeave, int earnedLeave, int casualLeave) {
        this.sickLeave = sickLeave;
        this.earnedLeave = earnedLeave;
        this.casualLeave = casualLeave;
    }

    public int getSickLeave() {
        return sickLeave;
    }

    public int getEarnedLeave() {
        return earnedLeave;
    }

    public int getCasualLeave() {
        return casualLeave;
    }
}
