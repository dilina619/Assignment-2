package com.synnlabz.fitme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();        //handler object
        Runnable runnable = new Runnable() {        //running thread
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, Login.class);     //redirects to next page
                startActivity(intent);
                SplashScreen.this.finish();

            }
        };
        handler.postDelayed(runnable, 3000);        //running thread 3000 milis
    }
}
