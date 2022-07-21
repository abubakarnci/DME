package com.example.offloadingcode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class LocalActivity extends AppCompatActivity {

    EditText ed1;
    TextView tx1;
    Button b1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        ed1=findViewById(R.id.input);
        b1=findViewById(R.id.submit_local);
        tx1=findViewById(R.id.result);

        b1.setOnClickListener(v ->{

            int input=Integer.parseInt(ed1.getText().toString());

            //fib(input);

            String ans=String.valueOf(fib(input));
            tx1.setText(ans);
            Log.e("ans",ans);
            System.out.println(ans+": ans");

        });

    }

    private int fib(int input) {

        if(input<=1){
            return input;
            //tx1.setText(String.valueOf(input));
        }
        else{

            return fib(input-1)+fib(input-2);

        }

    }

}