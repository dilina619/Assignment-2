package com.synnlabz.fitme.healthtips;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnlabz.fitme.MainActivity;
import com.synnlabz.fitme.R;
import com.synnlabz.fitme.myworkout.WorkoutObject;

import java.util.Map;
import java.util.Random;

public class HealthTips extends YouTubeBaseActivity {

    private static final String TAG = "MainActivity";

    YouTubePlayerView mYouTubePlayerView1;      //initializing variables
    YouTubePlayer.OnInitializedListener mOnInitializedListener1;
    private TextView mDesc;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String currentUserID , userId, url , desc ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);      //get connectivity status from system
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
        } else {        //when there is not network connection
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

        mAuth = FirebaseAuth.getInstance();     //get firebase instance
        userId = mAuth.getCurrentUser().getUid();
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();       //getting current users id
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);     //making firebase instance

        mYouTubePlayerView1 = (YouTubePlayerView)findViewById(R.id.youtubeplayer1);
        mDesc = (TextView)findViewById(R.id.tipdesc);

        getTips();      //calling getTips function


    }

    public void YoutubeInit(final String youtubeurl){       //youtube video player function , url is passed
        mOnInitializedListener1 = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(youtubeurl);        //loading youtube video with url
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        mYouTubePlayerView1.initialize(YouTubeConfig.getApiKey(),mOnInitializedListener1);      //initializing with YOutubeAPI
    }

    private void getTips() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("category");        //making firebase path
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String cat = dataSnapshot.getValue().toString();        //get child values
                    getTipInfo(cat);        //passing that value into getTipsInfo function
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getTipInfo(String val) {
        String[] arr={"healthtip1", "healthtip2"};      //array
        Random r=new Random();      //selecting one item randomly
        int index = r.nextInt(arr.length);
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("PersonalWorkouts").child(val).child(arr[index]).child("tip1");
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("url")!=null){       //get data from firebase database
                        url = map.get("url").toString();
                        YoutubeInit(url);

                    }
                    if(map.get("desc")!=null){      //get data from firebase database
                        desc = map.get("desc").toString();
                        mDesc.setText(desc);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    public void backToHome(View view) {     //when user press back btn this function will execute
        startActivity(new Intent(HealthTips.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }
}
