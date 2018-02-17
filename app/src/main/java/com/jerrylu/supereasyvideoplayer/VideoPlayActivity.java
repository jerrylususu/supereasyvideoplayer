package com.jerrylu.supereasyvideoplayer;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class VideoPlayActivity extends AppCompatActivity {

    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide title bar and status bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_play);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        // get video path and play
        if(videoView == null){
            Intent getIntent = getIntent();
            String file = getIntent.getStringExtra("url");
            videoView = (VideoView)findViewById(R.id.video1);
            videoView.setVideoURI(Uri.parse(file));
            videoView.setMediaController(new MediaController(this));
        }

        if(savedInstanceState !=null && savedInstanceState.getInt("currentPos") !=0){
            videoView.seekTo(savedInstanceState.getInt("currentPos"));
        }
        videoView.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentPos",videoView.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }
}
