package com.synnlabz.fitme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.synnlabz.fitme.healthtips.HealthTips;
import com.synnlabz.fitme.mymealplan.MyMealPlan;
import com.synnlabz.fitme.myworkout.MyWorkout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        //Do Nothing
    }

    public void logoutUser(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this,Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void viewProfile(View view) {
        startActivity(new Intent(MainActivity.this,Profile.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void viewHealthTips(View view) {
        startActivity(new Intent(MainActivity.this, HealthTips.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void goToMyWorkouts(View view) {
        startActivity(new Intent(MainActivity.this, MyWorkout.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void goToOtherWorkouts(View view) {
        startActivity(new Intent(MainActivity.this, OtherWorkouts.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void goToMyMealPlan(View view) {
        startActivity(new Intent(MainActivity.this, MyMealPlan.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }
}
