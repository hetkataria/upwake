package com.example.hetkataria.alarmapp.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TabHost;

import com.example.hetkataria.alarmapp.R;

public class FirstActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Third tab");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        tab1.setIndicator("");
        tab1.setContent(new Intent(this,MainActivity.class));

        tab2.setIndicator("");
        tab2.setContent(new Intent(this,StopwatchActivity.class));

        tab3.setIndicator("");
        tab3.setContent(new Intent(this,TimerActivity.class));

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);

    }

}
