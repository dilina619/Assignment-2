package com.synnlabz.fitme.otherworkouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.synnlabz.fitme.R;

import java.util.ArrayList;
import java.util.List;

public class ShowOtherWorkout extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mOtherWorkoutAdapter;
    private RecyclerView.LayoutManager mOtherWorkoutLayoutManager;

    private FirebaseAuth mAuth;

    private String currentUserID;

    private TextView Heading;

    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_other_workout);

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

        Heading = (TextView)findViewById(R.id.heading);

        try {
            key = getIntent().getStringExtra("key");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Heading.setText(key);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mOtherWorkoutLayoutManager = new LinearLayoutManager(ShowOtherWorkout.this);
        mRecyclerView.setLayoutManager(mOtherWorkoutLayoutManager);

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView.setLayoutManager(mOtherWorkoutLayoutManager);
        mOtherWorkoutAdapter = new otherworkoutAdapter(getDataSetOtherWorkouts(), ShowOtherWorkout.this); //ischatfalse
        mRecyclerView.setAdapter(mOtherWorkoutAdapter);

        getOtherWorkouts(key);
    }

    private void getOtherWorkouts(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("OtherWorkouts").child(key);
        userDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String name = null;
                    String sets = null;
                    String reps = null;
                    String rest = null;

                    if(dataSnapshot.child("name").getValue()!=null){
                        name = dataSnapshot.child("name").getValue().toString();
                    }
                    if(dataSnapshot.child("sets").getValue()!=null){
                        sets = dataSnapshot.child("sets").getValue().toString();
                    }
                    if(dataSnapshot.child("reps").getValue()!=null){
                        reps = dataSnapshot.child("reps").getValue().toString();
                    }
                    if(dataSnapshot.child("rest").getValue()!=null){
                        rest = dataSnapshot.child("rest").getValue().toString();
                    }

                    otherworkoutObject otherworkout = new otherworkoutObject(name , sets , reps , rest);
                    resultsOtherWorkouts.add(otherworkout);
                    mOtherWorkoutAdapter.notifyDataSetChanged();
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

    private ArrayList<otherworkoutObject> resultsOtherWorkouts = new ArrayList<otherworkoutObject>();

    private List<otherworkoutObject> getDataSetOtherWorkouts() {
        return resultsOtherWorkouts;
    }

    public void backToHome(View view) {
        startActivity(new Intent(ShowOtherWorkout.this, OtherWorkouts.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }
}
