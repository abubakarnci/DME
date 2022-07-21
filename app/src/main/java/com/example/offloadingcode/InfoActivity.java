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
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class InfoActivity extends AppCompatActivity {


    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;
    TextView bStatus, bHealth, bLevel, memory, network;

    TextView textView ;
    ProcessBuilder processBuilder;
    String Holder = "";
    String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
    InputStream inputStream;
    Process process ;
    byte[] byteArry ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        bStatus= findViewById(R.id.bStatus);
        bHealth= findViewById(R.id.bHealth);
        bLevel= findViewById(R.id.bLevel);
        memory=findViewById(R.id.memory);
        network=findViewById(R.id.network);

        intentFilterAndBroadcast();

        //CPU

        textView = (TextView)findViewById(R.id.textView);

        byteArry = new byte[1024];

        try{
            processBuilder = new ProcessBuilder(DATA);

            process = processBuilder.start();

            inputStream = process.getInputStream();

            while(inputStream.read(byteArry) != -1){

                Holder = Holder + new String(byteArry);
            }

            inputStream.close();

        } catch(IOException ex){

            ex.printStackTrace();
        }

        textView.setText(Holder);
    }

    private void getMemoryInfo(){

        ActivityManager.MemoryInfo memoryInfo= new ActivityManager.MemoryInfo();
        ActivityManager activityManager=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        Runtime runtime= Runtime.getRuntime();

        StringBuilder builder=new StringBuilder();
        builder.append("Available Memory: ").append(memoryInfo.availMem).append("\n").
                append("Total Memory: ").append(memoryInfo.totalMem).append("\n").
                append("Runtime Max Memory: ").append(runtime.maxMemory()).append("\n").
                append("Runtime Total Memory: ").append(runtime.totalMemory()).append("\n").
                append("Runtime Free Memory: ").append(runtime.freeMemory()).append("\n");

        //return builder.toString();
        memory.setText(builder.toString());

    }

    private void intentFilterAndBroadcast() {

        intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        broadcastReceiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                getMemoryInfo();
                if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){

                    bLevel.setText(String.valueOf(intent.getIntExtra("level",0))+"%");

                    setHealth(intent);

                    setChargingStatus(intent);

                }
                if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
                    boolean noConnectivity=intent.getBooleanExtra(
                            ConnectivityManager.EXTRA_NO_CONNECTIVITY,false
                    );
                    if (noConnectivity){
                        network.setText("Disconnected");
                    }
                    else{
                        network.setText("Connected");
                    }
                }

            }
        };
    }

    private void setChargingStatus(Intent intent) {

        int status= intent.getIntExtra("status",-1);

        switch (status) {

            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                bStatus.setText("Unknown");
                break;
            case BatteryManager.BATTERY_STATUS_CHARGING:
                bStatus.setText("Charging");
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                bStatus.setText("Discharging");
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                bStatus.setText("Not Charging");
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                bStatus.setText("Full");
                break;
            default:
                bStatus.setText("null");

        }

    }

    private void setHealth(Intent intent) {

        int value= intent.getIntExtra("health",0);

        switch (value){

            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                bHealth.setText("Unknown");
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                bHealth.setText("Good");
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                bHealth.setText("Dead");
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                bHealth.setText("Overheat");
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                bHealth.setText("Over Voltage");
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                bHealth.setText("Unspecified Failure");
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                bHealth.setText("Cold");
                break;



        }
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