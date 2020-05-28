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

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.synnlabz.fitme.MainActivity;
import com.synnlabz.fitme.R;

public class HealthTips extends YouTubeBaseActivity {

    private static final String TAG = "MainActivity";

    YouTubePlayerView mYouTubePlayerView1 , mYouTubePlayerView2 , mYouTubePlayerView3 , mYouTubePlayerView4 , mYouTubePlayerView5;
    Button btnPlay;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);

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

        mYouTubePlayerView1 = (YouTubePlayerView)findViewById(R.id.youtubeplayer1);
        mYouTubePlayerView2 = (YouTubePlayerView)findViewById(R.id.youtubeplayer2);
        mYouTubePlayerView3 = (YouTubePlayerView)findViewById(R.id.youtubeplayer3);
        mYouTubePlayerView4 = (YouTubePlayerView)findViewById(R.id.youtubeplayer4);
        mYouTubePlayerView5 = (YouTubePlayerView)findViewById(R.id.youtubeplayer5);

        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("LsVgznawUy4");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        mYouTubePlayerView1.initialize(YouTubeConfig.getApiKey(),mOnInitializedListener);
    }

    public void backToHome(View view) {
        startActivity(new Intent(HealthTips.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }
}
