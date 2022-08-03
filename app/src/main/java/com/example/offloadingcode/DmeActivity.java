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
import android.widget.TextView;

public class DmeActivity extends AppCompatActivity {

    String connection="";
    String memoryinfo;
    long avaMemory;
    int bLevel;
    String bStatus;
    String bHealth;

    TextView ans;
    Button cloud, local;

    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dme);

        cloud=findViewById(R.id.cloud);
        local=findViewById(R.id.mobile);

        ans=findViewById(R.id.ans);

        //connection="";
        memoryinfo="";
        avaMemory=0;
        bLevel=0;
        bStatus="";
        bHealth="";

        cloud.setOnClickListener(v -> {

            Intent intCloud=new Intent(DmeActivity.this, CloudActivity.class);
            startActivity(intCloud);

        });

        local.setOnClickListener(v -> {

            Intent intMobile=new Intent(DmeActivity.this, LocalActivity.class);
            startActivity(intMobile);

        });


        intentFilterAndBroadcast();



    }

    private void decision() {
        if(connection.equals("Disconnected")){
           ans.setText("Local");
            local.setEnabled(true);
            cloud.setEnabled(false);
        }
        else if (connection.equals("Connected")){
            //its in MB
            if(avaMemory>=2100) {
                if(bLevel>=50 && bHealth.equals("Good")&& bStatus.equals("Charging")) {
                    ans.setText("Local");
                    local.setEnabled(true);
                    cloud.setEnabled(false);
                }
                else if(bLevel>=50 && bHealth.equals("Good")&& bStatus.equals("Discharging")) {
                    ans.setText("Local");
                    local.setEnabled(true);
                    cloud.setEnabled(false);
                }
                else if(bLevel>=50 && bStatus.equals("Full")&& bHealth.equals("Good")){
                    ans.setText("Local");
                    local.setEnabled(true);
                    cloud.setEnabled(false);
                }
                else if(bLevel<50 && bStatus.equals("Charging")&& bHealth.equals("Good")){
                    ans.setText("Local");
                    local.setEnabled(true);
                    cloud.setEnabled(false);
                }

                else {
                    ans.setText("Cloud");
                    local.setEnabled(false);
                    cloud.setEnabled(true);
                }
            }
            else{
                ans.setText("Cloud");
                local.setEnabled(false);
                cloud.setEnabled(true);
            }
        }
        Log.e("Test",connection);
        System.out.println(connection+": connection");
        //ans.setText(onnection);
        System.out.println(bHealth+": bHealth");
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
        //memory.setText(builder.toString());
        memoryinfo=builder.toString();
        avaMemory= memoryInfo.availMem/1000000;
        //System.out.println(avaMemory+": memory");
        //ans.setText(avaMemory+": memory");

    }


    private void intentFilterAndBroadcast() {

        intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        broadcastReceiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {


                getMemoryInfo();
                if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){

                    bLevel=((intent.getIntExtra("level",0)));

                    setHealth(intent);

                    setChargingStatus(intent);

                }
                if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
                    boolean noConnectivity=intent.getBooleanExtra(
                            ConnectivityManager.EXTRA_NO_CONNECTIVITY,false
                    );
                    if (noConnectivity){
                        //network.setText("Disconnected");
                        connection="Disconnected";
                    }
                    else{
                        //network.setText("Connected");
                        connection="Connected";
                    }
                }

                decision();

            }
        };


    }

    private void setChargingStatus(Intent intent) {

        int status= intent.getIntExtra("status",-1);

        switch (status) {

            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                bStatus="Unknown";
                break;
            case BatteryManager.BATTERY_STATUS_CHARGING:
                bStatus="Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                bStatus="Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                bStatus="Not Charging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                bStatus="Full";
                break;
            default:
                bStatus="null";

        }

    }

    private void setHealth(Intent intent) {

        int value= intent.getIntExtra("health",0);

        switch (value){

            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                bHealth="Unknown";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                bHealth="Good";
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                bHealth="Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                bHealth="Overheat";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                bHealth="Over Voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                bHealth="Unspecified Failure";
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                bHealth="Cold";
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


}//end