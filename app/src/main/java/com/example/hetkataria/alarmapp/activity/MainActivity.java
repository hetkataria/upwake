package com.example.hetkataria.alarmapp.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import android.widget.TimePicker;

import com.example.hetkataria.alarmapp.model.Alarm;
import com.example.hetkataria.alarmapp.adapter.AlarmAdapter;
import com.example.hetkataria.alarmapp.adapter.AlarmReceiver;
import com.example.hetkataria.alarmapp.MySQLiteHelper;
import com.example.hetkataria.alarmapp.R;
import com.example.hetkataria.alarmapp.TimePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private List<Alarm> alarmList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;
    private FloatingActionButton b1;
    private MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);

        alarmAdapter = new AlarmAdapter(alarmList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(alarmAdapter);

        b1 = findViewById(R.id.fab);
        b1.setOnClickListener(this);

        db = new MySQLiteHelper(this);

        for (Alarm alarm: db.getAllAlarms()){

            Log.d("ok",alarm.getTime());
        }
        if (db.getAllAlarms() != null) {
            alarmList.addAll(db.getAllAlarms());
            blessMethod();
            alarmAdapter.notifyDataSetChanged();
            for (Alarm alarm: db.getAllAlarms()){
                if (alarm.getOn()==1){
                    startAlarmForTime(alarm.getTime());
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                TimePickerFragment tp = new TimePickerFragment();
                tp.show(getFragmentManager(), "timePicker");
                break;
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        String pm;

        if (hour > 11) {
            pm = "PM";
        } else {
            pm = "AM";
        }

        String hourString;
        String minuteString;

        if (hour<10){
            hourString = "0"+ Integer.toString(hour);
        } else{
            hourString = Integer.toString(hour);
        }

        if (minute<10){
            minuteString = "0"+ Integer.toString(minute);
        } else{
            minuteString = Integer.toString(minute);
        }

        String alarmTime = hourString + ":" + minuteString;
        addAlarmData(alarmTime, pm);
    }

    private void addAlarmData(String time, String pm) {
        Alarm alarm = new Alarm(time, pm);

        db.addAlarm(alarm);
        alarmList.clear();
        alarmList.addAll(db.getAllAlarms());
        blessMethod();

        if (alarm.getOn() == 1){
            for (int i = 0; i < db.getAllAlarms().size(); i++){
                if (db.getAllAlarms().get(i).getTime().equals(time)){
                    recyclerView.smoothScrollToPosition(i);
                }
            }
        }
        startAlarmForTime(time);
        Log.d("ok",time);


        alarmAdapter.notifyDataSetChanged();
    }

    private void startAlarmForTime (String time){

        if((Integer.parseInt(time.split(":")[0]) >= Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) && Integer.parseInt(time.split(":")[1]) > Calendar.getInstance().get(Calendar.MINUTE)){

        String uniqueRequestcode = time.split(":")[0] + time.split(":")[1];
        int RequestCode = Integer.parseInt(uniqueRequestcode);

        AlarmManager alarmMgr = (AlarmManager)MainActivity.this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("Time",time);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(MainActivity.this, RequestCode, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, Integer.parseInt(time.split(":")[0]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
        calendar.set(Calendar.SECOND,0);

        Log.d("ok",calendar.toString());

        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        }
    }

    private void blessMethod(){
        for(Alarm newAlarm:alarmList){

            String oldTime = newAlarm.getTime();

            if(oldTime.substring(0,2).equals("00")){
                newAlarm.setTime("12"+oldTime.substring(2, oldTime.length()));
            } else if (oldTime.startsWith("0")){
                newAlarm.setTime(oldTime.substring(1,oldTime.length()));
            } else if (oldTime.startsWith("2") ||(oldTime.startsWith("1")&& Character.getNumericValue(oldTime.charAt(1))>2) ) {
                int firstTwo = Integer.parseInt(oldTime.substring(0, 2));
                firstTwo -= 12;
                String pls = Integer.toString(firstTwo) + oldTime.substring(2, oldTime.length());
                newAlarm.setTime(pls);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ok","okkkk");
        alarmList.clear();
        alarmList.addAll(db.getAllAlarms());
        blessMethod();
        alarmAdapter.notifyDataSetChanged();
    }
}
