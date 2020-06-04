package com.synnlabz.fitme.mymealplan;

public class mealObject {       //Object class
    private String desc , heading , mealImage;

    public mealObject(String heading , String desc , String mealImage){     //parameterized cinstructor
        this.heading=heading;
        this.desc=desc;
        this.mealImage=mealImage;
    }

    public String getHeading() {        //getters and setters
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMealImage() {
        return mealImage;
    }

    public void setMealImage(String mealImage) {
        this.mealImage = mealImage;
    }
}
