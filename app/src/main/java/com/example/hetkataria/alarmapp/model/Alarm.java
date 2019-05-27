package com.example.hetkataria.alarmapp.model;

public class Alarm {

    private int id;
    private String time;
    private String am_or_pm;
    private int isOn;
    private boolean isVibrate;
    private String label;
    private boolean isRepeat;

    public Alarm(String time, String pm){
        this.time = time;
        this.am_or_pm = pm;
        this.isOn = 1;
        this.isVibrate = false;
        this.label = "";
        this.isRepeat = true;
        this.id = 0;
    }

    public void setTime(String time){
        this.time = time;
    }
    public void setAm_or_pm(String am_or_pm){
        this.am_or_pm = am_or_pm;
    }
    public void setVibrate(boolean isVibrate){
        this.isVibrate = isVibrate;
    }
    public void setLabel(String label){
        this.label = label;
    }
    public void setOn(int isOn){ this.isOn = isOn; }
    public void setRepeat(boolean isRepeat){
        this.isRepeat = isRepeat;
    }
    public void setId(int id){
        this.id = id;
    }


    public int getOn(){return isOn;}
    public boolean getVibrate(){return isVibrate;}
    public boolean getRepeat(){return isRepeat;}
    public String getLabel(){return label;}
    public String getAm_or_pm(){return am_or_pm;}
    public String getTime(){return time;}
    public int getId(){return id;}

}
