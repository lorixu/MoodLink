package com.example.tim.moodlink;

/**
 * Created by Tim on 22/07/2016.
 */
public class LocationData {

    private int id;
    private TimeAndDay timeStamp;
    private double latitude;
    private double longitude;


    public LocationData(TimeAndDay time, double latitude, double longitude){
        this.timeStamp = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationData(int day, int month, int year, int hour, int minute, int second, double latitude, double longitude){
        this.timeStamp = new TimeAndDay(day, month, year, hour, minute, second);

        this.latitude = latitude;
        this.longitude = longitude;
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


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LocationData{" +
                "id=" + id +
                ", timeStamp=" + timeStamp +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
