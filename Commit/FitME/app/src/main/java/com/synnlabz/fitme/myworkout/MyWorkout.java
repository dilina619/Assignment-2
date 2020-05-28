package com.synnlabz.fitme.myworkout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnlabz.fitme.MainActivity;
import com.synnlabz.fitme.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyWorkout extends AppCompatActivity {

    TextView viewClock;
    ImageButton start, pause, reset;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes;
    private boolean mTimerRunning;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mWorkoutAdapter;
    private RecyclerView.LayoutManager mWorkoutLayoutManager;

    private FirebaseAuth mAuth;

    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_workout);

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Connection Failure")
                    .setMessage("Please Connect to the Internet")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        viewClock = (TextView)findViewById(R.id.clock);
        start = (ImageButton) findViewById(R.id.btnplay);
        pause = (ImageButton)findViewById(R.id.btnpause);
        reset = (ImageButton)findViewById(R.id.btnstop);

        handler = new Handler() ;

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mWorkoutLayoutManager = new LinearLayoutManager(MyWorkout.this);
        mRecyclerView.setLayoutManager(mWorkoutLayoutManager);

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView.setLayoutManager(mWorkoutLayoutManager);
        mWorkoutAdapter = new workoutAdapter(getDataSetWorkouts(), MyWorkout.this); //ischatfalse
        mRecyclerView.setAdapter(mWorkoutAdapter);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause.setVisibility(View.VISIBLE);
                start.setVisibility(View.GONE);
                startTimer();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause.setVisibility(View.GONE);
                start.setVisibility(View.VISIBLE);
                reset.setVisibility(View.VISIBLE);
                pauseTimer();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;

                pauseTimer();

                pause.setVisibility(View.GONE);
                reset.setVisibility(View.GONE);
                start.setVisibility(View.VISIBLE);

                String mCount = String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds);
                viewClock.setText(mCount);
            }
        });

        getWorkout();
    }

    public void startTimer(){
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
        mTimerRunning = true;
        updateBtn();
    }

    public void pauseTimer(){
        TimeBuff= TimeBuff + MillisecondTime;
        handler.removeCallbacks(runnable);
        mTimerRunning = false;
        updateBtn();
    }

    private void updateBtn(){
        if (mTimerRunning){
            start.setEnabled(false);
            pause.setEnabled(true);
            reset.setEnabled(true);
        }else{
            start.setEnabled(true);
            pause.setEnabled(false);
            reset.setEnabled(true);
        }
    }

    public Runnable runnable = new Runnable() {

        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;

            String mCount = String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds);
            viewClock.setText(mCount);

            handler.postDelayed(this, 0);
        }
    };

    private void getWorkout() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("category");
        //DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("category");
        //Toast.makeText(MyWorkout.this,"Current Category : "+matchDb,Toast.LENGTH_SHORT).show();
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(MyWorkout.this,"Current datasnapshot : "+dataSnapshot.getValue(),Toast.LENGTH_SHORT).show();
                if (dataSnapshot.exists()){
                    //String key = dataSnapshot.getKey();
                    String cat = dataSnapshot.getValue().toString();
                    //Toast.makeText(MyWorkout.this," Key : "+cat,Toast.LENGTH_SHORT).show();
                    FetchMatchWorkout(cat);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void FetchMatchWorkout(String key) {
        String[] arr={"workout1", "workout2", "workout3"};
        Random r=new Random();
        int index = r.nextInt(arr.length);
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("PersonalWorkouts").child(key).child(arr[index]);
        userDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String name = null;
                    String sets = null;
                    String reps = null;

                    if(dataSnapshot.child("name").getValue()!=null){
                        name = dataSnapshot.child("name").getValue().toString();
                    }
                    if(dataSnapshot.child("sets").getValue()!=null){
                        sets = dataSnapshot.child("sets").getValue().toString();
                    }
                    if(dataSnapshot.child("reps").getValue()!=null){
                        reps = dataSnapshot.child("reps").getValue().toString();
                    }

                    WorkoutObject workout = new WorkoutObject(name , sets , reps);
                    resultsWorkouts.add(workout);
                    mWorkoutAdapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private ArrayList<WorkoutObject> resultsWorkouts = new ArrayList<WorkoutObject>();

    private List<WorkoutObject> getDataSetWorkouts() {
        return resultsWorkouts;
    }

    public void backToHome(View view) {
        startActivity(new Intent(MyWorkout.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }
}
