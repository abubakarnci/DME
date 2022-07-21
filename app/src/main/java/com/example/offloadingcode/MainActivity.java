package com.example.offloadingcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    Button cloud, local, dme, info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cloud=findViewById(R.id.cloud);
        local=findViewById(R.id.mobile);
        dme=findViewById(R.id.dme);
        info=findViewById(R.id.info);

        cloud.setOnClickListener(v -> {

            Intent intCloud=new Intent(MainActivity.this, CloudActivity.class);
            startActivity(intCloud);

        });

        local.setOnClickListener(v -> {

            Intent intMobile=new Intent(MainActivity.this, LocalActivity.class);
            startActivity(intMobile);

        });


        dme.setOnClickListener(v -> {

            Intent intDem=new Intent(MainActivity.this, SplashActivity.class);
            startActivity(intDem);

        });

        info.setOnClickListener(v -> {

            Intent intInfo=new Intent(MainActivity.this, InfoActivity.class);
            startActivity(intInfo);

        });

    }
}