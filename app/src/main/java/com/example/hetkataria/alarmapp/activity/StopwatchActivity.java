package com.example.hetkataria.alarmapp.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.airbnb.lottie.LottieAnimationView;
import com.example.hetkataria.alarmapp.R;
import com.example.hetkataria.alarmapp.classifier.CircularProgressIndicator;

public class StopwatchActivity extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;
    EditText tview;
    FloatingActionButton button;
    int i;
    int seconds;
    int counter;
    private NotificationManager notifManager;
    NumberPicker np;


    private static final CircularProgressIndicator.ProgressTextAdapter TIME_TEXT_ADAPTER = new CircularProgressIndicator.ProgressTextAdapter() {
        @Override
        public String formatText(double time) {
            return "";
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        getSupportActionBar().hide();

        tview = findViewById(R.id.tview);
        button = findViewById(R.id.timebutton);
        //lottieAnimationView = findViewById(R.id.lottie);
        final CircularProgressIndicator circularProgress = findViewById(R.id.circular_progress);

        circularProgress.setProgressTextAdapter(TIME_TEXT_ADAPTER);
        circularProgress.setDirection(CircularProgressIndicator.DIRECTION_CLOCKWISE);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                tview.setBackground(null);
                tview.setCursorVisible(false);

                seconds = Integer.parseInt(tview.getText().toString())*1000;
                i = 0;
                circularProgress.setProgress(i, seconds);

                    new CountDownTimer(seconds, 1000) {

                        public void onTick(long millisUntilFinished) {
                            tview.setText("" + ((millisUntilFinished / 1000)+1));

                            if (circularProgress.getProgress() < seconds){
                                i+=1000;
                                circularProgress.setProgress(i, seconds);
                            }
                        }
                        public void onFinish() {
                            tview.setText("");
                            tview.setHint("Done!");
                            circularProgress.setProgress(1, seconds);

                            showNotification();


                        }
                    }.start();

                    //float factor = (float) (5.0/Double.parseDouble(et.getText().toString()));

                    //lottieAnimationView.setSpeed(factor);
                    //lottieAnimationView.playAnimation();
                    //et.setText("");
            }
        });
    }

    public void showNotification(){
        Context context = StopwatchActivity.this;

        final int NOTIFY_ID = 0; // ID of notification
        String id = "Notification"; // default_channel_id
        String title = "Notification"; // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);

            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),att);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context,FirstActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle("UpWake Stopwatch")                            // required
                    .setSmallIcon(R.drawable.ic_baseline_alarm_24px)   // required
                    .setContentText("Your time is up!") // required
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle("UpWake Stopwatch")                            // required
                    .setSmallIcon(R.drawable.ic_baseline_alarm_24px)   // required
                    .setContentText("Your time is up!") // required
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

}

/*<com.airbnb.lottie.LottieAnimationView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:lottie_autoPlay="false"
        app:lottie_loop="false"
        app:lottie_fileName="okay.json"

        android:id="@+id/lottie"/>*/