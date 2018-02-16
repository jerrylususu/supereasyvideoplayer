package com.jerrylu.supereasyvideoplayer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button)findViewById(R.id.button1);
        final EditText inputText1 = (EditText)findViewById(R.id.textInput1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String name = inputText1.getText().toString();
//                Toast.makeText(MainActivity.this,String.format("Hello, %s!",name), Toast.LENGTH_SHORT).show();
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
            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            File file = new File(img_path);
            Toast.makeText(MainActivity.this, file.toString(), Toast.LENGTH_SHORT).show();

            Intent toVideoPlay = new Intent(MainActivity.this,VideoPlayActivity.class);
            toVideoPlay.putExtra("url",file.toString());
            startActivity(toVideoPlay);
        }
    }
}
