package com.example.tim.moodlink;

/**
 * Created by Tim on 22/07/2016.
 */
public class CameraData {

    private int id;
    private TimeAndDay timeStamp;
    private String path;


    public CameraData(TimeAndDay time, String path){
        this.timeStamp = time;
        this.path = path;
    }

    public CameraData(int day, int month, int year, int hour, int minute, int second, String path){
        this.timeStamp = new TimeAndDay(day, month, year, hour, minute, second);
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "CameraData{" +
                "id=" + id +
                ", timeStamp=" + timeStamp +
                ", path='" + path + '\'' +
                '}';
    }
}
