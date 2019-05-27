package com.example.hetkataria.alarmapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hetkataria.alarmapp.R;
import com.example.hetkataria.alarmapp.model.Timezone;

import java.util.List;

public class TimezoneAdapter extends RecyclerView.Adapter<TimezoneAdapter.MyViewHolder> {

    private List<Timezone> timezoneList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView placeName;
        public TextView hoursBehind;
        public TextView amOrPm;
        public TextView timeTV;


        public MyViewHolder(View view) {
            super(view);
            placeName = (TextView) view.findViewById(R.id.placeName);
            hoursBehind = (TextView) view.findViewById(R.id.hoursBehind);
            amOrPm = (TextView) view.findViewById(R.id.amOrPM);
            timeTV = (TextView) view.findViewById(R.id.timeTV);
        }
    }

    public TimezoneAdapter(List<Timezone> timezoneList) {
        this.timezoneList = timezoneList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_timezone, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Timezone timezone = timezoneList.get(position);
        holder.placeName.setText(timezone.getPlace());
        holder.hoursBehind.setText(timezone.getOffset());
        holder.amOrPm.setText(timezone.getAmOrPm());
        holder.timeTV.setText(timezone.getTime());

    }

    @Override
    public int getItemCount() {
        return timezoneList.size();
    }
}
