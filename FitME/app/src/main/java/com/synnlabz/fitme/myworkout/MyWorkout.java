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

    TextView viewClock;     //initializing variables
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

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);      //check network connectivity
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
        } else {
            new AlertDialog.Builder(this)       //giving alert when device has no network connection
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
        start = (ImageButton) findViewById(R.id.btnplay);       //initializing components
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

        start.setOnClickListener(new View.OnClickListener() {       //when user press start btn,
            @Override
            public void onClick(View view) {
                pause.setVisibility(View.VISIBLE);      //pause button will appear
                start.setVisibility(View.GONE);         //start button will disapprear
                startTimer();                           //timer start
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {       //when user press pause btn
            @Override
            public void onClick(View view) {
                pause.setVisibility(View.GONE);         //pause button will disapprear
                start.setVisibility(View.VISIBLE);      //start button will appear
                reset.setVisibility(View.VISIBLE);      //reset button will appear
                pauseTimer();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {       //when user press reset btn
            @Override
            public void onClick(View view) {
                MillisecondTime = 0L ;      //setting variables values
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;

                pauseTimer();           //timer pause

                pause.setVisibility(View.GONE);     //pause button will disapprear
                reset.setVisibility(View.GONE);     //reset button will disapprear
                start.setVisibility(View.VISIBLE);      //start button will apprear

                String mCount = String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds);      //setting time clock format
                viewClock.setText(mCount);      //reset the timer counter
            }
        });

        getWorkout();       //get workouts function
    }

    public void startTimer(){       //start timer function. when user clicks play button this function will execute
        StartTime = SystemClock.uptimeMillis();     //getting system clock
        handler.postDelayed(runnable, 0);
        mTimerRunning = true;       //set timer running state
        updateBtn();        //button update
    }

    public void pauseTimer(){       //pause timer function. when user clicks pause button this function will execute
        TimeBuff= TimeBuff + MillisecondTime;       //gett pause time
        handler.removeCallbacks(runnable);
        mTimerRunning = false;      //set timer running state
        updateBtn();        //button update
    }

    private void updateBtn(){       //updating button function
        if (mTimerRunning){     //when timer is running
            start.setEnabled(false);        //user cant press play button again
            pause.setEnabled(true);         //user can only click these buttons which are enabled true
            reset.setEnabled(true);
        }else{
            start.setEnabled(true);
            pause.setEnabled(false);        //user cant press pause button again
            reset.setEnabled(true);         //user can only click these buttons which are enabled true
        }
    }

    public Runnable runnable = new Runnable() {     //runnable thread

        public void run() {     //thread running
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;       //updating time
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);        //calculating miliseconds
            Minutes = Seconds / 60;     //converting milis to minutes and seconds
            Seconds = Seconds % 60;

            String mCount = String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds);      //setting printing format
            viewClock.setText(mCount);      //update the click timer

            handler.postDelayed(this, 0);
        }
    };

    private void getWorkout() {     //get workout function
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("category");        //setting firebase reference
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {       //check single database node
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){     //accesing nodes data
                    String cat = dataSnapshot.getValue().toString();        //get category childs value
                    FetchMatchWorkout(cat);     //passing that value into fetchmatchworkout function
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void FetchMatchWorkout(String key) {
        String[] arr={"workout1", "workout2", "workout3","workout4"};       //selecting random
        Random r=new Random();
        int index = r.nextInt(arr.length);
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("PersonalWorkouts").child(key).child(arr[index]);        //firebase reference
        userDb.addChildEventListener(new ChildEventListener() {         //retrieving values
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String name = null;
                    String sets = null;
                    String reps = null;
                    String workoutImageUrl = null;

                    if(dataSnapshot.child("name").getValue()!=null){        //retrieving values
                        name = dataSnapshot.child("name").getValue().toString();
                    }
                    if(dataSnapshot.child("sets").getValue()!=null){        //retrieving values
                        sets = dataSnapshot.child("sets").getValue().toString();
                    }
                    if(dataSnapshot.child("reps").getValue()!=null){        //retrieving values
                        reps = dataSnapshot.child("reps").getValue().toString();
                    }
                    if(dataSnapshot.child("image").getValue()!=null){       //retrieving values
                        workoutImageUrl = dataSnapshot.child("image").getValue().toString();
                    }

                    WorkoutObject workout = new WorkoutObject(name , sets , reps , workoutImageUrl);        //passing values into object
                    resultsWorkouts.add(workout);           //adding workouts objects into list
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

    private ArrayList<WorkoutObject> resultsWorkouts = new ArrayList<WorkoutObject>();      //list containing workout objects

    private List<WorkoutObject> getDataSetWorkouts() {
        return resultsWorkouts;
    }       //return workouts

    public void backToHome(View view) {     //redirects to homepage
        startActivity(new Intent(MyWorkout.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }
}
