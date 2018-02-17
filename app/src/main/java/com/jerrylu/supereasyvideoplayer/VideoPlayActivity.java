package com.jerrylu.supereasyvideoplayer;

import android.content.Intent;
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
    private List<File> files;
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
            String file = getIntent.getStringExtra("url");
            String folder = file.substring(0,file.lastIndexOf('/'));
            Log.i("folder",folder);
            File folderFile = new File(folder);
            files = getFileList(folderFile);
            videoView.setVideoURI(Uri.parse(files.get(0).getPath()));
            videoView.setMediaController(new MediaController(this));
        }

        if(savedInstanceState !=null && savedInstanceState.getInt("currentPos") !=0){
            videoView.seekTo(savedInstanceState.getInt("currentPos"));
        }
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(files.size()>1){
                    Log.i("play","ready to play next");
                    files.remove(0);
                    videoView.setVideoURI(Uri.parse(files.get(0).getPath()));
                    Log.i("play",files.get(0).getPath());
                    videoView.start();
                } else {
                    Toast.makeText(VideoPlayActivity.this,"Play Ended!",Toast.LENGTH_SHORT);
                    Log.i("play","PlayENDED!");
                }

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentPos",videoView.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    public List<File> getFileList(File fileFolder){
        File[] fileArray = fileFolder.listFiles();
        List<File> fileList = new ArrayList<File>();
        for(File f:fileArray){
            Log.i("files",f.getPath());
            if(f.isFile()) // it is a actually file
                fileList.add(f);
            else // it is a folder
                getFileList(f);
        }
        return fileList;
    }
}
