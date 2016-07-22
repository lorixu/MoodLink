package com.example.tim.moodlink;

/**
 * Created by Tim on 22/07/2016.
 */
public class LightData {

    private int id;
    private TimeAndDay timeStamp;
    private int luxValue;


    public LightData(TimeAndDay time, int luxValue){
        this.timeStamp = time;
        this.luxValue = luxValue;
    }

    public LightData(int day, int month, int year, int hour, int minute, int second, int luxValue){
        this.timeStamp = new TimeAndDay(day, month, year, hour, minute, second);
        this.luxValue = luxValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TimeAndDay getTimeStamp() {
        return timeStamp;
    }

    public void setTime(TimeAndDay time) {
        this.timeStamp = time;
    }

    public void setTime(int day, int month, int year, int hour, int minute, int second) {
        this.timeStamp = new TimeAndDay(day, month, year, hour, minute, second);
    }

    public int getLuxValue() {
        return luxValue;
    }

    public void setLuxValue(int luxValue) {
        this.luxValue = luxValue;
    }

    @Override
    public String toString() {
        return "LightData{" +
                "id=" + id +
                ", time=" + timeStamp +
                ", luxValue=" + luxValue +
                '}';
    }
}
