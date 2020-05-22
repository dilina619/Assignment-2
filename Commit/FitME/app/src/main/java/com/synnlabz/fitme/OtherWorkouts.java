package com.synnlabz.fitme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.synnlabz.fitme.mymealplan.MyMealPlan;

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
        getApplicationContext().startActivity(i);
    }

    public void goToLegs(View view) {
        String key = "6";
        getView(key);
    }

    public void goToShoulders(View view) {
    }

    public void goToAbs(View view) {
    }

    public void goToBac(View view) {
    }

    public void goToArms(View view) {
    }

    public void goToChest(View view) {
    }
}
