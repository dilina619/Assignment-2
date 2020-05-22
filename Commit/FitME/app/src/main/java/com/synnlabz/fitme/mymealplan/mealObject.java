package com.synnlabz.fitme.mymealplan;

public class mealObject {
    private String desc , heading;

    public mealObject(String heading , String desc){
        this.heading=heading;
        this.desc=desc;
    }

    public String getHeading() {
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
}
