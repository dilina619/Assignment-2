package com.synnlabz.fitme.myworkout;

public class WorkoutObject {        //workout object class
    private String name , sets , reps , workoutImageUrl;        //variables

    public WorkoutObject(String name , String sets , String reps , String workoutImageUrl){     //parameterized constructor
        this.name=name;
        this.sets=sets;
        this.reps=reps;
        this.workoutImageUrl=workoutImageUrl;
    }

    public String getName() {
        return name;
    }       //getters and setters

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

    public String getWorkoutImageUrl() {
        return workoutImageUrl;
    }

    public void setWorkoutImageUrl(String workoutImageUrl) {
        this.workoutImageUrl = workoutImageUrl;
    }

}
