package com.synnlabz.fitme.myworkout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mWorkoutAdapter;
    private RecyclerView.LayoutManager mWorkoutLayoutManager;

    private FirebaseAuth mAuth;

    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_workout);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mWorkoutLayoutManager = new LinearLayoutManager(MyWorkout.this);
        mRecyclerView.setLayoutManager(mWorkoutLayoutManager);

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView.setLayoutManager(mWorkoutLayoutManager);
        mWorkoutAdapter = new workoutAdapter(getDataSetWorkouts(), MyWorkout.this); //ischatfalse
        mRecyclerView.setAdapter(mWorkoutAdapter);

        getWorkout();
    }

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
