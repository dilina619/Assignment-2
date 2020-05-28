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

    public void backToHome(View view) {
        startActivity(new Intent(OtherWorkouts.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void getView(String key){
        Intent i = new Intent(OtherWorkouts.this, ShowOtherWorkout.class);
        i.putExtra("key", key);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
    }

    public void goToLegs(View view) {
        String key = "legs";
        getView(key);
    }

    public void goToShoulders(View view) {
        String key = "shoulders";
        getView(key);
    }

    public void goToAbs(View view) {
        String key = "abdominals";
        getView(key);
    }

    public void goToBac(View view) {
        String key = "back";
        getView(key);
    }

    public void goToArms(View view) {
        String key = "arms";
        getView(key);
    }

    public void goToChest(View view) {
        String key = "chest";
        getView(key);
    }
}
