package com.example.offloadingcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CloudActivity extends AppCompatActivity {


    String connection="";
    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;

    TextView detail;
    Button submit;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);

        detail=findViewById(R.id.connection);
        submit=findViewById(R.id.submit_cloud);
        input=findViewById(R.id.cloud_input);

        intentFilterAndBroadcast();
    }

    private void intentFilterAndBroadcast() {

        intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        broadcastReceiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {


                if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
                    boolean noConnectivity=intent.getBooleanExtra(
                            ConnectivityManager.EXTRA_NO_CONNECTIVITY,false
                    );
                    if (noConnectivity){
                        //network.setText("Disconnected");
                        connection="Disconnected";
                        detail.setText("Internet not connected");
                        submit.setEnabled(false);
                        input.setEnabled(false);
                    }
                    else{
                        //network.setText("Connected");
                        connection="Connected";
                        detail.setText("");
                        submit.setEnabled(true);
                        input.setEnabled(true);
                    }
                }

            }
        };

    }


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver,intentFilter);
        //IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        //registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

}