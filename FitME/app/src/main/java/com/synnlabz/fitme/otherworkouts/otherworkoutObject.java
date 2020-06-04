package com.synnlabz.fitme.otherworkouts;

public class otherworkoutObject {       //object
    private String name , sets , reps , rest;       //object variables

    public otherworkoutObject(String name , String sets , String reps , String rest){       //parameterizzed constructor
        this.name=name;
        this.sets=sets;
        this.reps=reps;
        this.rest=rest;
    }

    public String getName() {
        return name;
    }       //Getters and setters

    public void setName(String name) {
        this.name = name;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getRest() {
        return rest;
    }

    public void setRest(String rest) {
        this.rest = rest;
    }
}
