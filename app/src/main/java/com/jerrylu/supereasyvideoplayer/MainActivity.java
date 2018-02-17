package com.jerrylu.supereasyvideoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String fileSelected;
    private String folderPath;
    private List<File> fileInFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button)findViewById(R.id.button1);
        final EditText inputText1 = (EditText)findViewById(R.id.textInput1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent fileSelectIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileSelectIntent.setType("*/*");
            fileSelectIntent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(fileSelectIntent,1);
            }
        });
    }

    String path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor fileCursor = managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = fileCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            fileCursor.moveToFirst();
            fileSelected = fileCursor.getString(actual_image_column_index);
            folderPath = fileSelected.substring(0,fileSelected.lastIndexOf('/'));
            fileInFolder = getFileList(new File(folderPath));

            SharedPreferences settings = getSharedPreferences("setting", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("folderPath",folderPath);
            editor.commit();

            Intent toVideoPlay = new Intent(MainActivity.this,VideoPlayActivity.class);
            toVideoPlay.putExtra("fileList",(Serializable)fileInFolder);
            startActivity(toVideoPlay);
        }
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
