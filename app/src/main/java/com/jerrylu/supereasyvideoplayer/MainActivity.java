package com.jerrylu.supereasyvideoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
                String name = inputText1.getText().toString();
                Toast.makeText(MainActivity.this,String.format("Hello, %s!",name), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
