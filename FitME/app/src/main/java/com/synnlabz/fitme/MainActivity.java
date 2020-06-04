package com.synnlabz.fitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.Calendar;
import java.util.Map;

import static androidx.core.os.LocaleListCompat.create;

public class MainActivity extends AppCompatActivity {

    private TextView mNameField , mTDEEField , mWeightField;        //defining variables

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String userId , username, tdee, weight , profileImageUrl;

    private ImageView mProfileImage;;
    private Context context;

    private TextView StepCounter;
    private double MagnitudePrevious = 0;
    private Integer stepCount = 0;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StepCounter = (TextView)findViewById(R.id.step);        //initializing components
        mNameField = (TextView)findViewById(R.id.username);
        mTDEEField = (TextView)findViewById(R.id.tdee);
        mWeightField = (TextView) findViewById(R.id.weight);
        mProfileImage = (ImageView) findViewById(R.id.profileImage);

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);      //checking network connectivity
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
        } else {        //showing alert if theres is no network connection
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.MyDialog);
            dialog.setTitle("No Network Connection");
            dialog.setMessage("Please enable Network connection and GPS");
            dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        }

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();       //get user id from database
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();

        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        final Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 11); // For 1 PM or 2 PM
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event!=null){
                    float x = event.values[0];  //reading values from sensor
                    float y = event.values[1];
                    float z = event.values[2];

                    double Magnitude = Math.sqrt(x*x + y*y + z*z);      //formula for accelerometer
                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;

                    if (MagnitudeDelta>6){      //when magnitude changes steps count is increase
                        stepCount++;
                    }
                    StepCounter.setText(stepCount.toString());      //showing step count
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };sensorManager.registerListener(stepDetector,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void getUserInfo() {        //getting users infomation
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){      //retrieve values from database
                        username = map.get("name").toString();
                        mNameField.setText(username);
                    }
                    if(map.get("tdee")!=null){      //retrieve values from database
                        tdee = map.get("tdee").toString();
                        mTDEEField.setText(tdee);
                    }
                    if(map.get("weight")!=null){        //retrieve values from database
                        weight = map.get("weight").toString();
                        mWeightField.setText(weight);
                    }
                    Glide.clear(mProfileImage);     //retrieve values from database
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
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);      //on app pause,
        SharedPreferences.Editor editor = sharedPreferences.edit();     //accessing shared preferances
        editor.clear();
        editor.putInt("stepCOunt",stepCount);       //putting steps count to sharedpreferences
        editor.apply();     //apply
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);      //same applies here
        SharedPreferences.Editor editor = sharedPreferences.edit();     //same applies here
        editor.clear();
        editor.putInt("stepCount",stepCount);       //same applies here
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        stepCount = sharedPreferences.getInt("stepCount",0);        //same applies here
    }

    @Override
    public void onBackPressed() {
        //Do Nothing
    }

    public void logoutUser(View view) {         //redirecting pages
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this,Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void viewProfile(View view) {        //redirecting pages
        final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
        loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
            }
        },2500);
        startActivity(new Intent(MainActivity.this,Profile.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void viewHealthTips(View view) {     //redirecting pages
        startActivity(new Intent(MainActivity.this, HealthTips.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void goToMyWorkouts(View view) {     //redirecting pages
        startActivity(new Intent(MainActivity.this, MyWorkout.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void goToOtherWorkouts(View view) {      //redirecting pages
        startActivity(new Intent(MainActivity.this, OtherWorkouts.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }

    public void goToMyMealPlan(View view) {     //redirecting pages
        startActivity(new Intent(MainActivity.this, MyMealPlan.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }
}
