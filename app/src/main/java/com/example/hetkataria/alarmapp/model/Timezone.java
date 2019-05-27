package com.example.hetkataria.alarmapp.model;

public class Timezone {

    private String place;
    private String time;
    private String offset;
    private String amOrPm;
    private String ffs;

    public Timezone(String place, String time, String offset, String amOrPm, String ffs){
        this.amOrPm=amOrPm;
        this.time=time;
        this.offset=offset;
        this.place=place;
        this.ffs = ffs;
    }

    public String getAmOrPm() {
        return amOrPm;
    }
    public String getOffset() {
        return offset;
    }
    public String getPlace() {
        return place;
    }
    public String getTime() {
        return time;
    }

    public String getFfs() {
        return ffs;
    }

    public void setAmOrPm(String amOrPm) {
        this.amOrPm = amOrPm;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public void setFfs(String ffs){
        this.ffs = ffs;
    }
}
