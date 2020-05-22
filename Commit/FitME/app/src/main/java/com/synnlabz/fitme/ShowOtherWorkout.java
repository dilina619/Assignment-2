package com.synnlabz.fitme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.synnlabz.fitme.mymealplan.MyMealPlan;

public class ShowOtherWorkout extends AppCompatActivity {

    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_other_workout);

        try {
            //get intent to get account id
            key = getIntent().getStringExtra("key");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(),"Key is : "+key,Toast.LENGTH_SHORT).show();
    }

    public void backToHome(View view) {
        startActivity(new Intent(ShowOtherWorkout.this, OtherWorkouts.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }
}
