package com.jerrylu.supereasyvideoplayer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private SharedPreferences settings;
    private TextView PathTextView;
    private TextView PreviousFileTextView;
    private TextView PreviousPosTextView;
    private TextView CountTextView;
    private Button ConfigBtn;
    private Button PlayBtn;

    private String[] Permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int i = ContextCompat.checkSelfPermission(this,Permissions[0]);
            if (i!= PackageManager.PERMISSION_GRANTED)
                showPermissionDialog();
        }

        settings = getSharedPreferences("setting", Context.MODE_PRIVATE);
        PathTextView = (TextView)findViewById(R.id.PathTextView);
        PreviousFileTextView = (TextView)findViewById(R.id.PreviousFileTextView);
        PreviousPosTextView = (TextView)findViewById(R.id.PreviousPosTextView);
        CountTextView = (TextView)findViewById(R.id.CountTextView);
        PlayBtn = (Button)findViewById(R.id.PlayBtn);

        refreshTextView();

        PlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileInFolder = getFileList(new File(settings.getString("folderPath",getString(R.string.NoPathTip))));
                Intent toVideoPlay = new Intent(MainActivity.this,VideoPlayActivity.class);
                toVideoPlay.putExtra("fileList",(Serializable)fileInFolder);
                startActivity(toVideoPlay);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AboutItem:
                Toast.makeText(MainActivity.this,getString(R.string.MadeInfo),Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ConfigPathItem:
                Toast.makeText(MainActivity.this,getString(R.string.ConfigPathTip),Toast.LENGTH_LONG).show();
                Intent fileSelectIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileSelectIntent.setType("*/*");
                fileSelectIntent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(fileSelectIntent,1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        refreshTextView();
    }

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

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("folderPath",folderPath);
            editor.remove("currentFile");
            editor.remove("currentPos");
            editor.commit();
            refreshTextView();
        }
    }

    public List<File> getFileList(File fileFolder){
//        Log.i("fileinfo",fileFolder.getPath());
        File[] fileArray = fileFolder.listFiles();
//        Log.i("fileinfo",fileArray.toString());
        List<File> fileList = new ArrayList<File>();
        for(File f:fileArray){
            if(f.isFile() && (f.getPath().endsWith("mp4") || f.getPath().endsWith("flv"))) // it is a actually file and format is correct
                fileList.add(f);
//            Log.i("fileinfo",f.getPath().toString());
//            else // it is a folder
//                getFileList(f);
        }
        return fileList;
    }

    private void refreshTextView(){
        PathTextView.setText(getString(R.string.DirText)+":"+settings.getString("folderPath",getString(R.string.NoPathTip)));
        PreviousFileTextView.setText(getString(R.string.FileText)+":"+settings.getString("currentFile",""));
        PreviousPosTextView.setText(getString(R.string.PlayTimeText)+":"+revealPlayTime(settings.getString("currentPos","0")));
        try{
            fileInFolder = getFileList(new File(settings.getString("folderPath",getString(R.string.NoPathTip))));
            if(fileInFolder == null)
                throw new NullPointerException();
            CountTextView.setText(getString(R.string.CountText)+":"+revealPlayCount(fileInFolder,new File(settings.getString("currentFile","")))+" / "+String.valueOf(fileInFolder.size()));
        }catch (NullPointerException e){
            CountTextView.setText(getString(R.string.CountText)+":"+" ");
        }

    }

    private static String revealPlayTime(String orgTime){
        int t = Integer.parseInt(orgTime);
        return String.format("%d:%02d:%02d.%03d",t/(60*60*1000),(t%(60*60*1000))/(60*1000),(t%(60*1000)/1000),(t%1000));
    }

    private static int revealPlayCount(List<File> fileInFolder,File file){
        for(int i=0;i<fileInFolder.size();i++){
            if(fileInFolder.get(i).getPath().equals(file.getPath())){
                return i+1;
            }
        }
        return 1;
    }

    private void showPermissionDialog(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.PermissionAlertTitle))
                .setMessage(getString(R.string.PermissionAlertMessage))
                .setPositiveButton(getString(R.string.PermissionAlertTipsY), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton(getString(R.string.PermissionAlertTipsN), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this,getString(R.string.PermissionFailed),Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void startRequestPermission(){
        ActivityCompat.requestPermissions(this,Permissions,233);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == 233){
            Toast.makeText(MainActivity.this,getString(R.string.PermissionSucceed),Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this,getString(R.string.PermissionFailed),Toast.LENGTH_SHORT).show();
        }
    }
}
