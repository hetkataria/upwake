package com.example.hetkataria.alarmapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        if (!(getActivity() instanceof TimePickerDialog.OnTimeSetListener)) {
            throw new IllegalStateException("Activity should implement OnTimeSetListener!");
        }

        TimePickerDialog.OnTimeSetListener timeSetListener =  (TimePickerDialog.OnTimeSetListener) getActivity();

        return new TimePickerDialog(getActivity(), timeSetListener, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }
}
