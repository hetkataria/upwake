package com.example.hetkataria.alarmapp.adapter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.hetkataria.alarmapp.MySQLiteHelper;
import com.example.hetkataria.alarmapp.R;
import com.example.hetkataria.alarmapp.activity.MainActivity;
import com.example.hetkataria.alarmapp.classifier.ClassifierActivity;
import com.example.hetkataria.alarmapp.model.Alarm;

import java.io.IOException;

public class AlarmReceiver extends BroadcastReceiver {

    public static MediaPlayer mMediaPlayer;
    private NotificationManager notifManager;
    private Context context2;

    @Override
    public void onReceive(Context context, Intent intent){

        Log.d("ok","bless");

        final Context context1 = context;
        context2 = context;

        try {

            RingtoneManager ringtoneManager = new RingtoneManager(context1);
            Uri alarmRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

            mMediaPlayer = new MediaPlayer();

            mMediaPlayer.setDataSource(context1, alarmRingtoneUri);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mMediaPlayer.start();


        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ok","uh oh");
        }

        String Time = intent.getStringExtra("Time");
        Log.d("check","got the time");
        String amOrPm;

        if(Integer.parseInt(Time.split(":")[0]) <12){
            amOrPm = "AM";
        } else{
            amOrPm = "PM";
        }

        MySQLiteHelper db = new MySQLiteHelper(context1);

        Alarm alarm = new Alarm(Time,amOrPm);
        alarm.setOn(0);
        db.updateAlarm(alarm);


        showNotification();

        Intent i = new Intent(context.getApplicationContext(), ClassifierActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }

    public void showNotification(){

        final int NOTIFY_ID = 0; // ID of notification
        String id = "Notification"; // default_channel_id
        String title = "Notification"; // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)context2.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);


            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[] { 1000, 1000, 1000, 1000, 1000 });
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context2, id);
            intent = new Intent(context2,ClassifierActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context2, 0, intent, 0);
            builder.setContentTitle("UpWake Alarm")                            // required
                    .setSmallIcon(R.drawable.ic_baseline_alarm_24px)   // required
                    .setContentText("Your Alarm is here!")
                    .setAutoCancel(true)// required
                    .setContentIntent(pendingIntent);
        }
        else {
            builder = new NotificationCompat.Builder(context2, id);
            intent = new Intent(context2, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context2, 0, intent, 0);
            builder.setContentTitle("UpWake Alarm")                            // required
                    .setSmallIcon(R.drawable.ic_baseline_alarm_24px)   // required
                    .setContentText("Your Alarm is here!") // required
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }
    
}
