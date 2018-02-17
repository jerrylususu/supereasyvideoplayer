package com.jerrylu.supereasyvideoplayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = settings.edit();

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

            String previousFile = settings.getString("currentFile","");
            int previousPos = Integer.parseInt(settings.getString("currentPos","0"));

            if(!previousFile.equals("")){
                fileInFolder = rebuildNewFileInFolderList(fileInFolder,new File(previousFile));
            }

            videoView.setVideoURI(Uri.parse(fileInFolder.get(0).getPath()));

            videoView.seekTo(previousPos);
//            Log.i("previouspos",String.valueOf(previousPos));
            videoView.setMediaController(new MediaController(this));
            savedInstanceState = null;
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
                    editor.putString("currentFile",fileInFolder.get(0).getPath());
//                    Log.i("putinfo",fileInFolder.get(0).getPath());
                    editor.commit();
                    videoView.start();
                } else {
                    // all played
                    Toast.makeText(VideoPlayActivity.this,getString(R.string.PlayEndInfo),Toast.LENGTH_SHORT).show();
                    Intent backToMain = new Intent(VideoPlayActivity.this,MainActivity.class);
                    startActivity(backToMain);
                }

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentPos",videoView.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putString("currentPos",String.valueOf(videoView.getCurrentPosition()));
        editor.commit();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        int previousPos = Integer.parseInt(settings.getString("currentPos","0"));
        videoView.seekTo(previousPos);
        videoView.start();
//        Log.i("previouspos",String.valueOf(previousPos));
    }

    public static List<File> rebuildNewFileInFolderList(List<File> fileList, File file){
        int fileNo = -1;
        for(int i=0;i<fileList.size();i++){
            if(fileList.get(i).getPath().equals(file.getPath())){
                fileNo = i;
            }
        }
        List<File> newList = new ArrayList<>();
        for(int i=fileNo;i<fileList.size();i++){
            newList.add(fileList.get(i));
        }
        return newList;
    }


}
