package com.example.hetkataria.alarmapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hetkataria.alarmapp.R;
import com.example.hetkataria.alarmapp.model.Timezone;
import com.example.hetkataria.alarmapp.adapter.TimezoneAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TimerActivity extends AppCompatActivity {

    private FloatingActionButton button;
    private TextView tvClock;
    private Handler someHandler;
    private Runnable runnable;

    private List<Timezone> Timezonelist = new ArrayList<>();
    private RecyclerView recyclerView;
    private TimezoneAdapter timezoneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.timezoneRV);

        timezoneAdapter = new TimezoneAdapter(Timezonelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(timezoneAdapter);

        button = (FloatingActionButton) findViewById(R.id.placeButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(TimerActivity.this, PlacePickerActivity.class);
                startActivityForResult(i,1);
                someHandler.removeCallbacks(runnable);
            }
        });

        startHandler("GMT-4:00");

        tvClock = findViewById(R.id.tv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == 1){
                String placeName= data.getStringExtra("placeName");
                Log.d("ok",placeName);
                setNewTime(placeName);
            }
            if(resultCode == 2){
                startHandler("GMT-4:00");
            }
        }
    }

    private void setNewTime(String placeName){

        final String placeName2 = placeName;
        String newPlaceName = placeName.replace(" ","");
        String URL = "http://api.worldweatheronline.com/premium/v1/tz.ashx?key=54f97924e5b346a481e224157192705&format=JSON&q="+newPlaceName;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ok","alright");
                try{
                    JSONObject ok = response.getJSONObject("data");
                    JSONArray alright = ok.getJSONArray("time_zone");
                    JSONObject pls = alright.getJSONObject(0);
                    String GMToffset = pls.getString("utcOffset");
                    String currentTime = pls.getString("localtime").split(" ")[1];
                    Log.d("ok",GMToffset);

                    formatDate(GMToffset, placeName2, currentTime);

                }
                catch(JSONException e){ }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);

    }

    private void formatDate (String offset, String placeName, String currentTime){

        String finalString = offset;

        if (!offset.contains("-")){
            finalString = "+" + offset;
        }
        if (finalString.contains(".50")){
            finalString = finalString.replace(".50",":30");
        } else if (finalString.contains(".0")){
            finalString = finalString.replace(".0",":00");
        }

        Log.d("ok",finalString);

        final String finalfinalString = "GMT"+finalString;

        String amAndpm;

        if (Integer.parseInt(currentTime.split(":")[0])<13){
            amAndpm = "am";
        } else{
            amAndpm = "pm";
        }


        addToRecyclerView(placeName, currentTime, offset,amAndpm,finalfinalString);
    }

    private void addToRecyclerView(String placeName, String currentTime, String offset, String amOrPM, String ffs ){

        placeName = placeName.split(",")[0];

        Log.d("pls",offset);

        if (Double.parseDouble(offset)>-4.0 && offset.endsWith("50")){
            offset = Double.toString(Double.parseDouble(offset) + 4.0) + " hours ahead";
        } else if (Double.parseDouble(offset)==-4.0){
            offset ="";
        } else if(Double.parseDouble(offset)>-4.0 && !offset.endsWith("50")){
            offset = (int)(Double.parseDouble(offset)+4.0) + " hours ahead";
        } else if (Double.parseDouble(offset)<-4.0 && !offset.endsWith("50")){
            offset = (int)(-4.0 - Double.parseDouble(offset)) + " hours behind";
        } else {
            offset = Double.toString(-4.0 - Double.parseDouble(offset)) + " hours behind";
        }

        if (Integer.parseInt(currentTime.split(":")[0])>12){
            currentTime = Integer.toString(Integer.parseInt(currentTime.split(":")[0])-12) + ":"+currentTime.split(":")[1];
        }

        Log.d("pls",currentTime);
        if (currentTime.startsWith("00")){
            currentTime = "12" + ":" + currentTime.split(":")[1];
        }
        Log.d("pls",currentTime);
        if (currentTime.startsWith("0")){
            currentTime = currentTime.substring(1,currentTime.length());
        }

        Timezone timezone = new Timezone(placeName,currentTime,offset,amOrPM,ffs);
        Timezonelist.add(timezone);
        timezoneAdapter.notifyDataSetChanged();

        final String okay = ffs;

        /*if (Timezonelist.size()>1){
            updatePreviousTimezones();
        }*/

        startHandler(okay);

        /*someHandler = new Handler(getMainLooper());

        runnable = new Runnable() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(okay));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("HH:mm");
                date.setTimeZone(TimeZone.getTimeZone(okay));

                String localTime = date.format(currentLocalTime);

                if (Integer.parseInt(localTime.split(":")[0])>12){
                    localTime = Integer.toString(Integer.parseInt(localTime.split(":")[0])-12) + ":"+localTime.split(":")[1];
                }
                if (localTime.startsWith("0")){
                    localTime = localTime.substring(1,localTime.length());
                }

                TextView pl = recyclerView.getLayoutManager().getChildAt(Timezonelist.size()-1).findViewById(R.id.timeTV);
                pl.setText(localTime);

                someHandler.postDelayed(this, 1000);
            }
        };

        someHandler.postDelayed(runnable,10);*/


    }

    private void updatePreviousTimezones(){

    for (int i = 0; i < Timezonelist.size()-1; i ++){
        someHandler = new Handler(getMainLooper());

        final int ok = i;

        runnable = new Runnable() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(Timezonelist.get(ok).getFfs()));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("HH:mm");
                date.setTimeZone(TimeZone.getTimeZone(Timezonelist.get(ok).getFfs()));

                String localTime = date.format(currentLocalTime);

                if (Integer.parseInt(localTime.split(":")[0])>12) {
                    localTime = Integer.toString(Integer.parseInt(localTime.split(":")[0]) - 12) + ":" + localTime.split(":")[1];
                }
                if (localTime.startsWith("00")){
                    localTime = "12" + ":" + localTime.split(":")[1];
                }

                if (localTime.startsWith("0")){
                    localTime = localTime.substring(1,localTime.length());
                }


                TextView pl = recyclerView.getLayoutManager().getChildAt(ok).findViewById(R.id.timeTV);
                pl.setText(localTime);

                someHandler.postDelayed(this, 1000);
            }
        };

        someHandler.postDelayed(runnable,10);
    }

    }

    private void startHandler (final String okay){

        someHandler = new Handler(getMainLooper());

        runnable = new Runnable() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("HH:mm:ss");
                date.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));

                String localTime = date.format(currentLocalTime);

                if (Integer.parseInt(localTime.split(":")[0])>12) {
                    localTime = Integer.toString(Integer.parseInt(localTime.split(":")[0]) - 12) + ":" + localTime.split(":")[1] + ":" + localTime.split(":")[2];
                }
                if (localTime.startsWith("0")){
                    localTime = localTime.substring(1,localTime.length());
                }
                if (localTime.startsWith("00")){
                    localTime = "12" + ":" + localTime.split(":")[1];
                }
                tvClock.setText(localTime);

                for (int i = 0; i < Timezonelist.size()-1; i ++) {

                    Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone(Timezonelist.get(i).getFfs()));
                    Date currentLocalTime2 = cal2.getTime();
                    DateFormat date2 = new SimpleDateFormat("HH:mm");
                    date2.setTimeZone(TimeZone.getTimeZone(Timezonelist.get(i).getFfs()));

                    String localTime2 = date2.format(currentLocalTime2);


                    if (Integer.parseInt(localTime2.split(":")[0]) > 12) {
                        localTime2 = Integer.toString(Integer.parseInt(localTime2.split(":")[0]) - 12) + ":" + localTime2.split(":")[1];
                    }
                    if (localTime2.startsWith("00")){
                        localTime2 = "12" + ":" + localTime2.split(":")[1];
                    }

                    if (localTime2.startsWith("0")) {
                        localTime2 = localTime2.substring(1, localTime2.length());
                    }


                    TextView pl = recyclerView.getLayoutManager().getChildAt(i).findViewById(R.id.timeTV);
                    pl.setText(localTime2);

                }

                Calendar cal3 = Calendar.getInstance(TimeZone.getTimeZone(okay));
                Date currentLocalTime3 = cal3.getTime();
                DateFormat date3 = new SimpleDateFormat("HH:mm");
                date3.setTimeZone(TimeZone.getTimeZone(okay));

                String localTime3 = date3.format(currentLocalTime3);

                if (Integer.parseInt(localTime3.split(":")[0])>12){
                    localTime3 = Integer.toString(Integer.parseInt(localTime3.split(":")[0])-12) + ":"+localTime3.split(":")[1];
                }
                if (localTime3.startsWith("00")){
                    localTime3 = "12" + ":" + localTime3.split(":")[1];
                }
                if (localTime3.startsWith("0")){
                    localTime3 = localTime3.substring(1,localTime3.length());
                }

                if (Timezonelist.size()!=0){
                    TextView pl = recyclerView.getLayoutManager().getChildAt(Timezonelist.size()-1).findViewById(R.id.timeTV);
                    pl.setText(localTime3);
                }

                someHandler.postDelayed(this, 1000);
            }
        };

        someHandler.postDelayed(runnable,10);
    }

    private void startHandler(){

        someHandler = new Handler(getMainLooper());

        runnable = new Runnable() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("HH:mm:ss");
                date.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));

                String localTime = date.format(currentLocalTime);

                if (Integer.parseInt(localTime.split(":")[0])>12) {
                    localTime = Integer.toString(Integer.parseInt(localTime.split(":")[0]) - 12) + ":" + localTime.split(":")[1] + ":" + localTime.split(":")[2];
                }
                if (localTime.startsWith("0")){
                    localTime = localTime.substring(1,localTime.length());
                }
                if (localTime.startsWith("00")){
                    localTime = "12" + ":" + localTime.split(":")[1];
                }
                tvClock.setText(localTime);

                someHandler.postDelayed(this, 1000);
            }
        };

        someHandler.postDelayed(runnable,10);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadeinrtl, R.anim.fadeoutrtl);
    }

    @Override
    public void onStop(){
        super.onStop();
        startHandler();

    }
}




