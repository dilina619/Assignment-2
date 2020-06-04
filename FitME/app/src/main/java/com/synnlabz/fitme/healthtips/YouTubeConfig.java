package com.synnlabz.fitme.healthtips;

public class YouTubeConfig {        //java object that saves youtube api key

    public YouTubeConfig(){
        //constructor
    }

    private static final String API_KEY="AIzaSyBCVWnURvEayv2yWgH9x4igNKDfnvGHhjY";      //youtube api key

    public static String getApiKey(){
        return API_KEY;
    }
}
