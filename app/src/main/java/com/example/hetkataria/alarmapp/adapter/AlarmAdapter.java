package com.example.hetkataria.alarmapp.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.hetkataria.alarmapp.MySQLiteHelper;
import com.example.hetkataria.alarmapp.R;
import com.example.hetkataria.alarmapp.model.Alarm;

import java.util.Calendar;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.MyViewHolder> {

    private List<Alarm> alarmList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView time, ampm;
        public TextView label;
        public Switch aSwitch;
        public ImageView labelImage;
        public ImageView deleteIcon;
        public CheckBox checkBox;
        public TextView deletetv;

        public MyViewHolder(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.time);
            ampm = (TextView) view.findViewById(R.id.am_pm);
            label = (TextView) view.findViewById(R.id.label);
            aSwitch = view.findViewById(R.id.switchaz);
            labelImage = view.findViewById(R.id.delete_icon);
            deleteIcon = view.findViewById(R.id.real_delete_icon);
            checkBox = view.findViewById(R.id.checkbox_repeat);
            deletetv = (TextView) view.findViewById(R.id.deletetv);
        }
    }

    public AlarmAdapter(List<Alarm> alarmList, Context context) {
        this.alarmList = alarmList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_alarm, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Alarm alarm = alarmList.get(position);
        holder.time.setText(alarm.getTime());
        holder.ampm.setText(alarm.getAm_or_pm());
        holder.label.setText("Default (Bright Morning)");
        holder.aSwitch.setChecked(alarm.getOn()==1);
        holder.checkBox.setChecked(alarm.getRepeat());

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                MySQLiteHelper db = new MySQLiteHelper(context);

                String oldTime = alarmList.get(position).getTime();
                String amOrPm = alarmList.get(position).getAm_or_pm();

                if (amOrPm .equals("PM") && Integer.parseInt(oldTime.split(":")[0])<12){
                    oldTime = Integer.toString(Integer.parseInt(oldTime.split(":")[0])+12) + ":" + oldTime.split(":")[1];
                }
                else if (amOrPm .equals("AM") && Integer.parseInt(oldTime.split(":")[0])<10){
                    oldTime = "0"+oldTime;
                }else if(amOrPm.equals("AM")&& Integer.parseInt(oldTime.split(":")[0])==12){
                    oldTime = "00:"+oldTime.split(":")[1];
                }

                Log.d("ok",oldTime);

                db.deleteAlarm(oldTime);
                alarmList.remove(position);
                notifyDataSetChanged();

            }
        });

        holder.deletetv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                MySQLiteHelper db = new MySQLiteHelper(context);

                String oldTime = alarmList.get(position).getTime();
                String amOrPm = alarmList.get(position).getAm_or_pm();

                if (amOrPm .equals("PM") && Integer.parseInt(oldTime.split(":")[0])<12){
                    oldTime = Integer.toString(Integer.parseInt(oldTime.split(":")[0])+12) + ":" + oldTime.split(":")[1];
                }
                else if (amOrPm .equals("AM") && Integer.parseInt(oldTime.split(":")[0])<10){
                    oldTime = "0"+oldTime;
                }else if(amOrPm.equals("AM")&& Integer.parseInt(oldTime.split(":")[0])==12){
                    oldTime = "00:"+oldTime.split(":")[1];
                }

                Log.d("ok",oldTime);

                db.deleteAlarm(oldTime);
                alarmList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MySQLiteHelper db = new MySQLiteHelper(context);

                String oldoldTime = alarmList.get(position).getTime();
                String oldTime = alarmList.get(position).getTime();
                String amOrPm = alarmList.get(position).getAm_or_pm();
                Log.d("ok",oldTime);


                if (amOrPm .equals("PM") && Integer.parseInt(oldTime.split(":")[0])<12){
                    oldTime = Integer.toString(Integer.parseInt(oldTime.split(":")[0])+12) + ":" + oldTime.split(":")[1];
                }
                else if (amOrPm .equals("AM") && Integer.parseInt(oldTime.split(":")[0])<10){
                    oldTime = "0"+oldTime;
                } else if(amOrPm.equals("AM")&& Integer.parseInt(oldTime.split(":")[0])==12){
                    oldTime = "00:"+oldTime.split(":")[1];
                }


                alarmList.get(position).setTime(oldTime);

                if (isChecked){
                    alarmList.get(position).setOn(1);
                    db.updateAlarm(alarmList.get(position));
                    alarmList.get(position).setTime(oldoldTime);

                    if((Integer.parseInt(oldTime.split(":")[0]) >= Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) && Integer.parseInt(oldTime.split(":")[1]) > Calendar.getInstance().get(Calendar.MINUTE)) {

                        String uniqueRequestcode = oldTime.split(":")[0] + oldTime.split(":")[1];
                        int RequestCode = Integer.parseInt(uniqueRequestcode);

                        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(context, AlarmReceiver.class);
                        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, RequestCode, intent, 0);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR, Integer.parseInt(oldTime.split(":")[0]));
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(oldTime.split(":")[0]));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(oldTime.split(":")[1]));
                        calendar.set(Calendar.SECOND, 0);

                        Log.d("ok", calendar.toString());

                        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                    }


                } else{
                    String uniqueRequestcode = oldTime.split(":")[0] + oldTime.split(":")[1];
                    int RequestCode = Integer.parseInt(uniqueRequestcode);

                    Intent intent = new Intent(context, AlarmReceiver.class);
                    PendingIntent.getBroadcast(context,RequestCode,intent,0).cancel();

                    alarmList.get(position).setOn(0);
                    db.updateAlarm(alarmList.get(position));
                    alarmList.get(position).setTime(oldoldTime);

                }
                Log.d("ok", Integer.toString(alarmList.get(position).getOn()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}
