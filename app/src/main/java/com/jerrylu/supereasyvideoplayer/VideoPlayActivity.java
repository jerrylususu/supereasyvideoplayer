package com.jerrylu.supereasyvideoplayer;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class VideoPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_play);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        Intent getIntent = getIntent();
        String file = getIntent.getStringExtra("url");
        VideoView videoView = (VideoView)findViewById(R.id.video1);
        videoView.setVideoURI(Uri.parse(file));
        videoView.start();
        Toast.makeText(VideoPlayActivity.this,"Wotest",Toast.LENGTH_SHORT);
    }
}
