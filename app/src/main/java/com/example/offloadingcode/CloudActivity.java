package com.example.offloadingcode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CloudActivity extends AppCompatActivity {


    String value="";
    long start,end,total;
    String connection="";
    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;

    TextView detail,result,tx1;
    Button submit;
    EditText input;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);

        detail=findViewById(R.id.connection);
        submit=findViewById(R.id.submit_cloud);
        input=findViewById(R.id.cloud_input);
        result=findViewById(R.id.result_c);
        tx1=findViewById(R.id.time_c);
        mQueue = Volley.newRequestQueue(this);

        intentFilterAndBroadcast();

        submit.setOnClickListener(v ->{

            value=input.getText().toString();
            Log.e("ans",value);
            System.out.println(value+": ans");


            jsonParse();


        });

    }

    private void jsonParse() {

        start= System.currentTimeMillis();

        String url="http://dmespring-env.eba-kirhbdqa.eu-west-1.elasticbeanstalk.com/api/test/"+value;

        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("ans",response.toString());
                        System.out.println(response+": ans");

                        try {
                            long number=response.getLong("input");

                            Log.e("number", String.valueOf(number));
                            System.out.println(number+": number");

                            result.setText(String.valueOf(number));

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        end= System.currentTimeMillis();
                        //Log.e("time", String.valueOf(end));
                        //System.out.println(start+": time");

                        total=(end-start)/1000;

                        tx1.setText("Time: "+total+" sec");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                end= System.currentTimeMillis();
                //Log.e("time", String.valueOf(end));
                total=(end-start)/1000;
                tx1.setText("Time: "+total+" sec");

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
            100000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


        mQueue.add(request);



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