package com.jerrylu.supereasyvideoplayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
import java.util.ArrayList;
import java.util.List;

public class VideoPlayActivity extends AppCompatActivity {

    private VideoView videoView;
    private List<File> fileInFolder;
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
            videoView = (VideoView)findViewById(R.id.video1);

            Intent getIntent = getIntent();
            fileInFolder = (List<File>) getIntent.getSerializableExtra("fileList");

            videoView.setVideoURI(Uri.parse(fileInFolder.get(0).getPath()));
            videoView.setMediaController(new MediaController(this));
        }

        if(savedInstanceState !=null && savedInstanceState.getInt("currentPos") !=0){
            videoView.seekTo(savedInstanceState.getInt("currentPos"));
        }
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(fileInFolder.size()>1){
                    fileInFolder.remove(0);
                    videoView.setVideoURI(Uri.parse(fileInFolder.get(0).getPath()));
                    videoView.start();
                } else {
                    Toast.makeText(VideoPlayActivity.this,"Play Ended!",Toast.LENGTH_SHORT);
                }

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentPos",videoView.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }


}
