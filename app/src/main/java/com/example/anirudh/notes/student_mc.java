package com.example.anirudh.notes;

public class student_mc {
    String name;
    String sem;
    String sub;
    String srn;
    String admin;



    public student_mc(String name, String sem, String sub, String srn,String admin) {
        this.name = name;
        this.sem = sem;
        this.sub = sub;
        this.srn = srn;
        this.admin=admin;
    }

    public String getName() {
        return name;
    }

    public String getSem() {
        return sem;
    }

    public String getSub() {
        return sub;
    }

    public String getSrn() {
        return srn;
    }
    public String getAdmin() {
        return admin;
    }


}
