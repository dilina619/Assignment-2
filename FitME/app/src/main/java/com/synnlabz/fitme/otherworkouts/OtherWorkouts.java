package com.synnlabz.fitme.otherworkouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.synnlabz.fitme.MainActivity;
import com.synnlabz.fitme.R;

public class OtherWorkouts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_workouts);
    }

    public void backToHome(View view) {     //page redirects
        startActivity(new Intent(OtherWorkouts.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void getView(String key){        //get users choice
        Intent i = new Intent(OtherWorkouts.this, ShowOtherWorkout.class);
        i.putExtra("key", key);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
    }

    public void goToLegs(View view) {       //according to users selection activity is changing
        String key = "legs";
        getView(key);
    }

    public void goToShoulders(View view) {      //according to users selection activity is changing
        String key = "shoulders";
        getView(key);
    }

    public void goToAbs(View view) {        //according to users selection activity is changing
        String key = "abdominals";
        getView(key);
    }

    public void goToBac(View view) {        //according to users selection activity is changing
        String key = "back";
        getView(key);
    }

    public void goToArms(View view) {       //according to users selection activity is changing
        String key = "arms";
        getView(key);
    }

    public void goToChest(View view) {      //according to users selection activity is changing
        String key = "chest";
        getView(key);
    }
}
