package com.synnlabz.fitme.mymealplan;

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

public class MyMealPlan extends AppCompatActivity {

    private RecyclerView mRecyclerView;     //defining variables
    private RecyclerView.Adapter mMealAdapter;
    private RecyclerView.LayoutManager mMealLayoutManager;

    private FirebaseAuth mAuth;

    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meal_plan);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);     //initializing components
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mMealLayoutManager = new LinearLayoutManager(MyMealPlan.this);
        mRecyclerView.setLayoutManager(mMealLayoutManager);     //set recyclerview layout

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();       //get current users user id

        mRecyclerView.setLayoutManager(mMealLayoutManager);
        mMealAdapter = new mealAdapter(getDataSetMeals(), MyMealPlan.this); //ischatfalse
        mRecyclerView.setAdapter(mMealAdapter);

        getMealPlan();      //caling function
    }

    private void getMealPlan() {
        DatabaseReference mealDB = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("category");     //making firebase reference and access the data in child nodes
        mealDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){     //when child is available getting snapshot
                    String cat = dataSnapshot.getValue().toString();        //saving child value
                    FetchMatchMealPlan(cat);        //passing child value into function and calling.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void FetchMatchMealPlan(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("PersonalWorkouts").child(key).child("mealplan1");       //making firebase reference to access data
        userDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String heading = null;
                    String desc = null;
                    String image = null;

                    if(dataSnapshot.child("heading").getValue()!=null){         //accessing firebase child data
                        heading = dataSnapshot.child("heading").getValue().toString();
                    }
                    if(dataSnapshot.child("desc").getValue()!=null){
                        desc = dataSnapshot.child("desc").getValue().toString();
                    }
                    if(dataSnapshot.child("image").getValue()!=null){
                        image = dataSnapshot.child("image").getValue().toString();
                    }

                    mealObject meal = new mealObject(heading , desc , image);       //get data using mealObject
                    resultsMeals.add(meal);
                    mMealAdapter.notifyDataSetChanged();
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

    private ArrayList<mealObject> resultsMeals = new ArrayList<mealObject>();       //arraylist of mealobject

    private List<mealObject> getDataSetMeals() {
        return resultsMeals;
    }       //returning array

    public void backToHome(View view) {     //redirects to homepage
        startActivity(new Intent(MyMealPlan.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }
}
