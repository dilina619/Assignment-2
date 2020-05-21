package com.synnlabz.fitme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class HealthTips extends YouTubeBaseActivity {

    private static final String TAG = "MainActivity";

    YouTubePlayerView mYouTubePlayerView1 , mYouTubePlayerView2 , mYouTubePlayerView3 , mYouTubePlayerView4 , mYouTubePlayerView5;
    Button btnPlay;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);

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
        startActivity(new Intent(HealthTips.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        return;
    }
}
