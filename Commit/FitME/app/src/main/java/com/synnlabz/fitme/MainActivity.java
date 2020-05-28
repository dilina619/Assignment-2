package com.synnlabz.fitme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnlabz.fitme.healthtips.HealthTips;
import com.synnlabz.fitme.mymealplan.MyMealPlan;
import com.synnlabz.fitme.myworkout.MyWorkout;
import com.synnlabz.fitme.otherworkouts.OtherWorkouts;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mNameField , mTDEEField , mWeightField;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String userId , username, tdee, weight , profileImageUrl;

    private ImageView mProfileImage;;

    private TextView StepCounter;
    private double MagnitudePrevious = 0;
    private Integer stepCount = 0;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StepCounter = (TextView)findViewById(R.id.step);
        mNameField = (TextView)findViewById(R.id.username);
        mTDEEField = (TextView)findViewById(R.id.tdee);
        mWeightField = (TextView) findViewById(R.id.weight);
        mProfileImage = (ImageView) findViewById(R.id.profileImage);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();

        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        final Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event!=null){
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    double Magnitude = Math.sqrt(x*x + y*y + z*z);
                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;

                    if (MagnitudeDelta>6){
                        stepCount++;
                    }
                    StepCounter.setText(stepCount.toString());
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };sensorManager.registerListener(stepDetector,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        username = map.get("name").toString();
                        mNameField.setText(username);
                    }
                    if(map.get("tdee")!=null){
                        tdee = map.get("tdee").toString();
                        mTDEEField.setText(tdee);
                    }
                    if(map.get("weight")!=null){
                        weight = map.get("weight").toString();
                        mWeightField.setText(weight);
                    }
                    Glide.clear(mProfileImage);
                    if(map.get("profileImageUrl")!=null){
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.default_user).into(mProfileImage);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCOunt",stepCount);
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount",stepCount);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount",0);
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
